package com.foosbot.service.model;


import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Model {

    String hello();

    Optional<FoosballMatch> getMatchResult(UUID uuid);

    List<FoosballMatch> getAllMatchResults();

    UUID addMatchResult(FoosballPlayer reporter, Set<FoosballTeamResult> results);

//    List<UUID> addMatchResults(Set<DeprecatedMatch> matches);
//
//    void deleteMatch(UUID id);
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
