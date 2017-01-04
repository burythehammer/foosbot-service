package com.foosbot.service.model;


import com.beust.jcommander.internal.Maps;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.TeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import com.foosbot.service.model.players.PlayerStats;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class InMemoryModel implements Model {

    private Map<UUID, FoosballMatch> matches = Maps.newHashMap();

    @Override
    public String hello() {
        return "Hello, World";
    }

    @Override
    public Optional<FoosballMatch> getMatchResult(final UUID uuid) {
        return Optional.ofNullable(matches.get(uuid));
    }

    @Override
    public List<FoosballMatch> getAllMatchResults() {
        return matches.values().stream()
                .sorted(compareByDate)
                .collect(toList());
    }

    @Override
    public UUID addMatchResult(final FoosballPlayer reporter, final Set<TeamResult> results) {
        final UUID uuid = UUID.randomUUID();


        final FoosballMatch matchResult = FoosballMatch.builder()
                .uuid(uuid)
                .reporter(reporter)
                .results(results)
                .timestamp(Instant.now().toString())
                .build();

        if (!matchResult.isValid()) {
            throw new IllegalArgumentException("");
        }

        matches.put(uuid, matchResult);
        return uuid;
    }

    @Override
    public void clean() {
        matches = Maps.newHashMap();
    }

    @Override
    public void deleteMatch(final UUID uuid) {
        final FoosballMatch removed = matches.remove(uuid);
        if (removed == null) throw new IllegalArgumentException("Match not found: " + uuid);
    }


    @Override
    public Optional<PlayerStats> getPlayerStats(final String playerName) {

        final List<FoosballMatch> playerMatches = getPlayerMatches(playerName);

        if (playerMatches.isEmpty()) return Optional.empty();
        else return Optional.of(PlayerStats.getPlayerStats(playerName, playerMatches));
    }


    private List<FoosballMatch> getPlayerMatches(final String playerName) {
        return matches.values().stream()
                .filter(v -> v.playerInMatch(playerName))
                .sorted(compareByDate)
                .collect(Collectors.toList());
    }


//    @Override
//    public List<UUID> addMatchResults(final Set<DeprecatedMatch> deprecatedMatches) {
//
//        return deprecatedMatches.stream().map(m -> {
//            final UUID uuid = UUID.randomUUID();
//            final FoosballMatch match = new FoosballMatch(uuid, m.reporter, m.results, m.timestamp.toString());
//            matches.put(uuid, match);
//            return uuid;
//        }).collect(toList());
//
//    }


}
