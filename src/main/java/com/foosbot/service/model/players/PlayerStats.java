package com.foosbot.service.model.players;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PlayerStats {

    int gamesWon;
    int gamesLost;
    int gamesPlayed;
    UUID lastMatch;

    // TODO need ranking
    // float skillLevel;
    // FoosballMatch bestMatch;
    // FoosballMatch worstMatch;

}
