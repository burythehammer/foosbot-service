package com.foosbot.service.model;


import com.foosbot.service.match.CreateExistingFoosballMatchDTO;
import com.foosbot.service.match.CreateNewFoosballMatchDTO;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.players.PlayerStats;
import com.foosbot.service.season.RankResult;

import java.util.List;
import java.util.UUID;

public interface Model {

    String hello();

    FoosballMatch getMatchResult(UUID id);

    List<FoosballMatch> getAllMatchResults();

    String addMatchResult(CreateNewFoosballMatchDTO match);

    List<String> addMatchResults(List<CreateExistingFoosballMatchDTO> matches);

    String deleteMatch(String id);

    PlayerStats getPlayerStats(String id);

    // season
    List<FoosballMatch> getAllSeasonMatches(String seasonName);

    List<RankResult> getSeasonRank(String seasonName);

    String startNewSeason();

    // Global rank
    List<RankResult> getRanks();

}
