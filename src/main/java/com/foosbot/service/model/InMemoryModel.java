package com.foosbot.service.model;


import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;

import java.time.Instant;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class InMemoryModel implements Model {

    private Map<UUID, FoosballMatch> matches = new HashMap<>();

    final static Comparator<FoosballMatch> byDate = (m1, m2) -> Long.compare(
            Instant.parse(m1.timestamp).toEpochMilli(), Instant.parse(m2.timestamp).toEpochMilli());

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
                .sorted(byDate)
                .collect(toList());
    }

    @Override
    public UUID addMatchResult(final FoosballPlayer reporter, final Set<FoosballTeamResult> results) {
        final UUID uuid = UUID.randomUUID();
        final FoosballMatch match = new FoosballMatch(uuid, reporter, results, Instant.now().toString());
        matches.put(uuid, match);
        return uuid;
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
