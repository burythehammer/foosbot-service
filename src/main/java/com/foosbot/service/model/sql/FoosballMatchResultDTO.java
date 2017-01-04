package com.foosbot.service.model.sql;


import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.TeamResult;
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

    FoosballMatch getResult() {

        final Set<FoosballPlayer> team1players = ImmutableSet.of(FoosballPlayer.of(team1p1), FoosballPlayer.of(team1p2));
        final Set<FoosballPlayer> team2players = ImmutableSet.of(FoosballPlayer.of(team2p1), FoosballPlayer.of(team2p2));

        final TeamResult team1result = new TeamResult(team1players, team1score);
        final TeamResult team2result = new TeamResult(team2players, team2score);

        return new FoosballMatch(UUID.fromString(uuid), FoosballPlayer.of(reporter), ImmutableSet.of(team1result, team2result), timestamp.toInstant().toString());
    }
}
