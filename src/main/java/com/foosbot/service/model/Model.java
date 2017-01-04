package com.foosbot.service.model;


import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.TeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import com.foosbot.service.model.players.PlayerStats;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Model {

    Comparator<FoosballMatch> compareByDate = Comparator.comparingLong(m -> Instant.parse(m.timestamp).toEpochMilli());

    String hello();

    Optional<FoosballMatch> getMatchResult(UUID uuid);

    List<FoosballMatch> getAllMatchResults();

    UUID addMatchResult(FoosballPlayer reporter, Set<TeamResult> results) throws IllegalArgumentException;

    void clean();

    //    List<UUID> addMatchResults(Set<DeprecatedMatch> matches);
//
    void deleteMatch(UUID uuid) throws IllegalArgumentException;

    Optional<PlayerStats> getPlayerStats(String playerName);

//
//    Optional<PlayerStats> getPlayerStats(UUID uuid);
//
//    // season
//    List<FoosballMatch> getAllSeasonMatches(String seasonName);
//
//    List<RankResult> getSeasonRank(String seasonName);
//
//    String startNewSeason();
//
//    // Global rank
//    List<RankResult> getRanks();


}
