package com.foosbot.service.model;


import com.foosbot.service.handlers.payloads.ExistingFoosballMatchPayload;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import com.foosbot.service.model.players.PlayerStats;
import com.foosbot.service.model.season.RankResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Model {

    String hello();

    Optional<FoosballMatch> getMatchResult(UUID id);

    List<FoosballMatch> getAllMatchResults();

    UUID addMatchResult(FoosballPlayer reporter, Set<FoosballTeamResult> results);

    List<UUID> addMatchResults(List<ExistingFoosballMatchPayload> matches);

    UUID deleteMatch(String id);

    Optional<PlayerStats> getPlayerStats(String id);

    // season
    List<FoosballMatch> getAllSeasonMatches(String seasonName);

    List<RankResult> getSeasonRank(String seasonName);

    String startNewSeason();

    // Global rank
    List<RankResult> getRanks();

}
