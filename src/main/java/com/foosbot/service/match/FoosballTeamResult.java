package com.foosbot.service.match;


import com.foosbot.service.players.FoosballPlayer;
import lombok.Data;

import java.util.Set;

@Data
public class FoosballTeamResult {

    public Set<FoosballPlayer> players;
    public int score;

}
