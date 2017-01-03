package com.foosbot.service.match;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foosbot.service.handlers.Validates;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@JsonIgnoreProperties(value = {"valid"})
@AllArgsConstructor
@Builder
public class FoosballMatch implements Validates {
    public UUID uuid;
    public FoosballPlayer reporter;
    public Set<TeamResult> results;
    public String timestamp;

    public boolean isValid() {

        return uuid != null
                && reporter.isValid()
                && !results.isEmpty()
                && timestamp != null;
    }

    public boolean playerInGame(final String playerName) {
        return results.stream()
                .map(TeamResult::getPlayers)
                .flatMap(Collection::stream)
                .map(FoosballPlayer::getName)
                .anyMatch(n -> n.equals(playerName));
    }

    public boolean didPlayerWin(final String playerName) {
        if (!playerInGame(playerName)) throw new IllegalArgumentException("Player " + playerName + " not in game");

        return results.stream()
                .filter(result -> result.getScore() == 10)
                .map(TeamResult::getPlayers)
                .flatMap(Collection::stream)
                .anyMatch(player -> player.name.equals(playerName));
    }
}
