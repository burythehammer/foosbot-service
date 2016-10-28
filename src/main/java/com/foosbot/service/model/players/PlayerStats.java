package com.foosbot.service.model.players;


import com.foosbot.service.match.FoosballMatchResult;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class PlayerStats {

    int skillLevel;
    int gamesWon;
    int gamesLost;
    int gamesPlayed;
    FoosballMatchResult lastMatch;

    // TODO need ranking
    // FoosballMatchResult bestMatch;
    // FoosballMatchResult worstMatch;

}
