package com.foosbot.service.model;


import com.foosbot.service.match.DeprecatedMatch;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import com.foosbot.service.model.players.PlayerStats;
import com.foosbot.service.model.season.RankResult;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.time.Instant;
import java.util.*;

public class Sql2oModel implements Model {

    private final Sql2o sql2o;

    public Sql2oModel(final Sql2o sql2o) {
        this.sql2o = sql2o;
    }


    @Override
    public String hello() {
        return "Hello World!";
    }

    @Override
    public Optional<FoosballMatch> getMatchResult(final UUID uuid) {

        try (Connection conn = sql2o.open()) {
            final List<FoosballMatch> matches = conn.createQuery("select * from matches where uuid=:match_uuid")
                    .addParameter("match_uuid", uuid)
                    .executeAndFetch(FoosballMatch.class);

            if (matches.size() == 0) {
                return Optional.empty();
            } else if (matches.size() == 1) {
                return Optional.of(matches.get(0));
            } else {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public List<FoosballMatch> getAllMatchResults() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from matches")
                    .executeAndFetch(FoosballMatch.class);
        }
    }

    @Override
    public UUID addMatchResult(final FoosballPlayer reporter, final Set<FoosballTeamResult> resultSet) {

        try (Connection conn = sql2o.beginTransaction()) {

            final UUID matchUUID = UUID.randomUUID();
            final List<FoosballTeamResult> results = new ArrayList<>(resultSet);

            final FoosballTeamResult team1Result = results.get(0);
            final FoosballTeamResult team2Result = results.get(1);

            conn.createQuery("insert into matches(match_uuid, reporter, team1, team2, team1score, team2score, timestamp) VALUES (:match_uuid, :title, :content, :date)")
                    .addParameter("match_uuid", matchUUID)
                    .addParameter("reporter", reporter.name)
                    .addParameter("team1", team1Result.getPlayers())
                    .addParameter("team2", team2Result.getPlayers())
                    .addParameter("team1score", team1Result.getScore())
                    .addParameter("team2score", team2Result.getScore())
                    .addParameter("timestamp", Instant.now())
                    .executeUpdate();

            conn.commit();
            return matchUUID;
        }

    }

    @Override
    public List<UUID> addMatchResults(final Set<DeprecatedMatch> matches) {
        return null;
    }

    @Override
    public void deleteMatch(final UUID uuid) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("delete from matches where match_uuid=:match_uuid")
                    .addParameter("match_uuid", uuid.toString())
                    .executeUpdate();
        }
    }


    @Override
    public Optional<PlayerStats> getPlayerStats(final UUID uuid) {
        return null;
    }

    @Override
    public List<FoosballMatch> getAllSeasonMatches(final String seasonName) {
        return null;
    }

    @Override
    public List<RankResult> getSeasonRank(final String seasonName) {
        return null;
    }

    @Override
    public String startNewSeason() {
        return null;
    }

    @Override
    public List<RankResult> getRanks() {
        return null;
    }
}
