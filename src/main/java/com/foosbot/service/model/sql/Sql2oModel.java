package com.foosbot.service.model.sql;


import com.beust.jcommander.internal.Lists;
import com.foosbot.service.CommandLineOptions;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.TeamResult;
import com.foosbot.service.model.Model;
import com.foosbot.service.model.players.FoosballPlayer;
import com.foosbot.service.model.players.PlayerStats;
import com.google.common.collect.Iterables;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Sql2oModel implements Model {

    public static final String RESULTS_TABLE = "results";
    private final Sql2o sql2o;

    private static final Logger logger = Logger.getLogger(Sql2oModel.class.getCanonicalName());


    public Sql2oModel(final Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public static Sql2oModel getSqlModel(final CommandLineOptions options) {
        final Sql2o sql2o = new Sql2o("jdbc:postgresql://" + options.dbHost + ":" + options.dbPort + "/" + options.database,
                options.dbUsername, options.dbPassword, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });


        return new Sql2oModel(sql2o);
    }

    @Override
    public void clean() {
        try {
            dropTable();
        } catch (final Sql2oException e) {
            logger.info("Could not drop results table - does not exist");
        }

        createResultsTable();
    }


    public void createResultsTable() {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("CREATE TABLE results(" +
                    "uuid     varchar(36) PRIMARY KEY," +
                    "reporter       varchar(50)," +
                    "team1p1        varchar(50)," +
                    "team1p2        varchar(50)," +
                    "team2p1        varchar(50)," +
                    "team2p2        varchar(50)," +
                    "team1score     int," +
                    "team2score     int," +
                    "timestamp      timestamp)").executeUpdate();
            conn.commit();
        }
    }


    @Override
    public String hello() {
        return "Hello, World";
    }

    @Override
    public Optional<FoosballMatch> getMatchResult(final UUID uuid) {

        try (Connection conn = sql2o.open()) {


            final List<FoosballMatchResultDTO> results = conn.createQuery("SELECT * FROM " + RESULTS_TABLE + " where uuid=:uuid")
                    .addParameter("uuid", uuid.toString())
                    .executeAndFetch(FoosballMatchResultDTO.class);

            if (results.size() == 0) {
                return Optional.empty();
            } else if (results.size() == 1) {
                return Optional.of(results.get(0).getResult());
            } else {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public List<FoosballMatch> getAllMatchResults() {
        try (Connection conn = sql2o.open()) {
            final List<FoosballMatchResultDTO> foosballMatchResultDTOs = conn.createQuery("select * from " + RESULTS_TABLE)
                    .executeAndFetch(FoosballMatchResultDTO.class);

            return foosballMatchResultDTOs.stream()
                    .map(FoosballMatchResultDTO::getResult)
                    .collect(Collectors.toList());
        }
    }


    @Override
    public UUID addMatchResult(final FoosballPlayer reporter, final Set<TeamResult> results) {


        final FoosballMatch matchResult = FoosballMatch.builder()
                .uuid(UUID.randomUUID())
                .reporter(reporter)
                .results(results)
                .timestamp(Instant.now().toString())
                .build();

        if (!matchResult.isValid()) {
            throw new IllegalArgumentException("Results are not valid");
        }


        try (Connection conn = sql2o.beginTransaction()) {

            final UUID matchUUID = UUID.randomUUID();
            final List<TeamResult> resultsList = Lists.newArrayList(results);

            final TeamResult team1Result = resultsList.get(0);
            final TeamResult team2Result = resultsList.get(1);

            final List<String> team1 = getPlayerNames(team1Result);
            final List<String> team2 = getPlayerNames(team2Result);

            conn.createQuery("insert into " + RESULTS_TABLE + "(uuid, reporter, team1p1, team1p2, team2p1, team2p2, team1score, team2score, timestamp) VALUES (:uuid, :reporter, :team1p1, :team1p2, :team2p1, :team2p2, :team1score, :team2score, :timestamp)")
                    .addParameter("uuid", matchUUID)
                    .addParameter("reporter", reporter.name)
                    .addParameter("team1p1", team1.get(0))
                    .addParameter("team1p2", team1.get(1))
                    .addParameter("team2p1", team2.get(0))
                    .addParameter("team2p2", team2.get(1))
                    .addParameter("team1score", team1Result.getScore())
                    .addParameter("team2score", team2Result.getScore())
                    .addParameter("timestamp", Timestamp.from(Instant.now()))
                    .executeUpdate();

            conn.commit();

            return matchUUID;
        }
    }

    @Override
    public void deleteMatch(final UUID uuid) throws IllegalArgumentException {

        final Optional<FoosballMatch> matchResult = getMatchResult(uuid);
        if (!matchResult.isPresent()) throw new IllegalArgumentException("Cannot find match with id: " + uuid);

        try (Connection conn = sql2o.beginTransaction()) {

            conn.createQuery("DELETE FROM " + RESULTS_TABLE + " where uuid=:uuid")
                    .addParameter("uuid", uuid.toString())
                    .executeUpdate();

            conn.commit();
        }
    }

    @Override
    public Optional<PlayerStats> getPlayerStats(String playerName) {

        List<FoosballMatch> playerGames = getPlayerGames(playerName);

        if (playerGames.isEmpty()) return Optional.empty();

        final PlayerStats playerStats = PlayerStats.builder()
                .lastMatch(Iterables.getLast(playerGames).uuid)
                .matchesPlayed(playerGames.size())
                .matchesWon(PlayerStats.getNumberOfGamesWon(playerName, playerGames))
                .matchesLost(PlayerStats.getNumberOfGamesLost(playerName, playerGames))
                .build();

        return Optional.of(playerStats);
    }


    private List<FoosballMatch> getPlayerGames(final String playerName) {

        return getAllMatchResults().stream()
                .filter(v -> v.playerInMatch(playerName))
                .sorted(compareByDate)
                .collect(Collectors.toList());

        // todo do this in sql

//        final List<FoosballMatchResultDTO> results;
//        try (Connection conn = sql2o.beginTransaction()) {
//
//            String queryText = "select * from " + RESULTS_TABLE +
//                    " where team1p1=:playerName or where team1p2=:playerName";
////                    "OR WHERE team2p1=:playerName " +
////                    "OR WHERE team2p2=:playerName";
//
//            results = conn.createQuery(queryText)
//                    .addParameter("playerName", playerName)
//                    .executeAndFetch(FoosballMatchResultDTO.class);
//
//            conn.commit();
//
//            if (results.isEmpty()) return Collections.emptyList();
//
//            return results.stream().map(FoosballMatchResultDTO::getResult).collect(Collectors.toList());
    }


    private void dropTable() throws Sql2oException {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("DROP TABLE " + RESULTS_TABLE).executeUpdate();
            conn.commit();
        }
    }


    private List<String> getPlayerNames(final TeamResult team1Result) {
        return team1Result.getPlayers().stream().map(p -> p.name).collect(Collectors.toList());
    }

//    @Override
//    public List<UUID> addMatchResults(final Set<DeprecatedMatch> matches) {
//        return null;
//    }
//
//    @Override
//    public void deleteMatch(final UUID uuid) {
//        try (Connection conn = sql2o.open()) {
//            conn.createQuery("delete from matches where uuid=:uuid")
//                    .addParameter("uuid", uuid.toString())
//                    .executeUpdate();
//        }
//    }
}
