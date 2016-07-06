package com.foosbot.service.model;


import com.foosbot.service.handlers.payloads.ExistingFoosballMatchPayload;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import com.foosbot.service.model.players.PlayerStats;
import com.foosbot.service.model.season.RankResult;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Sql2oModel implements Model {

    private final Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;
    }


    @Override
    public String hello() {
        return null;
    }

    @Override
    public Optional<FoosballMatch> getMatchResult(UUID id) {
        return null;
    }

    @Override
    public List<FoosballMatch> getAllMatchResults() {
        return null;
    }

    @Override
    public UUID addMatchResult(FoosballPlayer reporter, Set<FoosballTeamResult> results) {
        return null;
    }

    @Override
    public List<UUID> addMatchResults(List<ExistingFoosballMatchPayload> matches) {
        return null;
    }

    @Override
    public UUID deleteMatch(String id) {
        return null;
    }

    @Override
    public Optional<PlayerStats> getPlayerStats(String id) {
        return null;
    }

    @Override
    public List<FoosballMatch> getAllSeasonMatches(String seasonName) {
        return null;
    }

    @Override
    public List<RankResult> getSeasonRank(String seasonName) {
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
