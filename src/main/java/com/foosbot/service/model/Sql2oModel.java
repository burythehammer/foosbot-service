package com.foosbot.service.model;


import com.foosbot.service.match.CreateExistingFoosballMatchDTO;
import com.foosbot.service.match.CreateNewFoosballMatchDTO;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.players.PlayerStats;
import com.foosbot.service.season.RankResult;

import java.util.List;
import java.util.UUID;

public class Sql2oModel implements Model {

    @Override
    public String hello() {
        return null;
    }

    @Override
    public FoosballMatch getMatchResult(final UUID id) {
        return null;
    }

    @Override
    public List<FoosballMatch> getAllMatchResults() {
        return null;
    }

    @Override
    public String addMatchResult(final CreateNewFoosballMatchDTO match) {
        return null;
    }

    @Override
    public List<String> addMatchResults(final List<CreateExistingFoosballMatchDTO> matches) {
        return null;
    }

    @Override
    public String deleteMatch(final String id) {
        return null;
    }

    @Override
    public PlayerStats getPlayerStats(final String id) {
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
