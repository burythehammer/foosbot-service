package com.foosbot.service.model.sql;


import com.foosbot.service.match.FoosballMatchResult;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import com.google.common.collect.ImmutableSet;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Data
public class FoosballMatchResultDTO {

    public String uuid;
    public String reporter;
    public String team1p1;
    public String team1p2;
    public String team2p1;
    public String team2p2;
    public int team1score;
    public int team2score;
    public Timestamp timestamp;

    public FoosballMatchResult getResult() {

        final Set<FoosballPlayer> team1players = ImmutableSet.of(new FoosballPlayer(team1p1), new FoosballPlayer(team1p2));
        final Set<FoosballPlayer> team2players = ImmutableSet.of(new FoosballPlayer(team2p1), new FoosballPlayer(team2p2));

        final FoosballTeamResult team1result = new FoosballTeamResult(team1players, team1score);
        final FoosballTeamResult team2result = new FoosballTeamResult(team2players, team2score);

        return new FoosballMatchResult(UUID.fromString(uuid), new FoosballPlayer(reporter), ImmutableSet.of(team1result, team2result), timestamp.toInstant().toString());
    }
}
