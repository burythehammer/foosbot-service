package com.foosbot.service.model;


import com.beust.jcommander.internal.Maps;
import com.foosbot.service.match.FoosballMatchResult;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import com.foosbot.service.model.players.PlayerStats;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class InMemoryModel implements Model {

    private Map<UUID, FoosballMatchResult> matches = Maps.newHashMap();

    private final static Comparator<FoosballMatchResult> compareByDate = (m1, m2) -> Long.compare(
            Instant.parse(m1.timestamp).toEpochMilli(), Instant.parse(m2.timestamp).toEpochMilli());

    @Override
    public String hello() {
        return "Hello, World";
    }

    @Override
    public Optional<FoosballMatchResult> getMatchResult(final UUID uuid) {
        return Optional.ofNullable(matches.get(uuid));
    }

    @Override
    public List<FoosballMatchResult> getAllMatchResults() {
        return matches.values().stream()
                .sorted(compareByDate)
                .collect(toList());
    }

    @Override
    public UUID addMatchResult(final FoosballPlayer reporter, final Set<FoosballTeamResult> results) {
        final UUID uuid = UUID.randomUUID();
        final FoosballMatchResult match = new FoosballMatchResult(uuid, reporter, results, Instant.now().toString());
        matches.put(uuid, match);
        return uuid;
    }

    @Override
    public void clean() {
        matches = new HashMap<>();
    }

    @Override
    public void deleteMatch(final UUID uuid) {
        final FoosballMatchResult removed = matches.remove(uuid);
        if (removed == null) throw new IllegalArgumentException("Match not found: " + uuid);
    }


    @Override
    public Optional<PlayerStats> getPlayerStats(final String playerName) {

        final List<FoosballMatchResult> playerGames = getPlayerGames(playerName);

        if (playerGames.isEmpty()) return Optional.empty();

        final PlayerStats playerStats = PlayerStats.builder()
                .lastMatch(playerGames.get(0))
                .gamesPlayed(playerGames.size())
                .gamesWon(getNumberOfGamesWon(playerName))
                .gamesLost(getNumberOfGamesLost(playerName))
                .build();

        return Optional.of(playerStats);
    }



    private List<FoosballMatchResult> getPlayerGames(final String playerName) {
        return matches.values().stream()
                .filter(v -> v.playerInGame(playerName))
                .sorted(compareByDate)
                .collect(Collectors.toList());
    }

    private int getNumberOfGamesLost(final String playerName) {
        return (int) getPlayerGames(playerName).stream()
                .filter(game -> !game.didPlayerWin(playerName))
                .count();
    }


    private int getNumberOfGamesWon(final String playerName) {
        return (int) getPlayerGames(playerName).stream()
                .filter(game -> game.didPlayerWin(playerName))
                .count();
    }

//    @Override
//    public List<UUID> addMatchResults(final Set<DeprecatedMatch> deprecatedMatches) {
//
//        return deprecatedMatches.stream().map(m -> {
//            final UUID uuid = UUID.randomUUID();
//            final FoosballMatchResult match = new FoosballMatchResult(uuid, m.reporter, m.results, m.timestamp.toString());
//            matches.put(uuid, match);
//            return uuid;
//        }).collect(toList());
//
//    }


}
