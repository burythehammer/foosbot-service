package com.foosbot.service.model.players;


import com.foosbot.service.match.FoosballMatch;
import com.google.common.collect.Iterables;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PlayerStats {

    public int matchesWon;
    public int matchesLost;
    public int matchesPlayed;
    public UUID lastMatch;

    public static PlayerStats getPlayerStats(final String playerName, final List<FoosballMatch> matches){

        return PlayerStats.builder()
                .lastMatch(Iterables.getLast(matches).uuid)
                .matchesPlayed(matches.size())
                .matchesWon(getNumberOfGamesWon(playerName, matches))
                .matchesLost(getNumberOfGamesLost(playerName, matches))
                .build();

    }

    public static int getNumberOfGamesWon(final String playerName, List<FoosballMatch> playerGames) {
        return (int) playerGames.stream()
                .filter(game -> game.didPlayerWin(playerName))
                .count();
    }

    public static int getNumberOfGamesLost(final String playerName, List<FoosballMatch> playerGames) {
        return (int) playerGames.stream()
                .filter(game -> !game.didPlayerWin(playerName))
                .count();
    }

    // TODO need ranking
    // float skillLevel;
    // FoosballMatch bestMatch;
    // FoosballMatch worstMatch;

}
