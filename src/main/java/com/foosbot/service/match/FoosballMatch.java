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
import java.util.stream.Collectors;

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
                && results.stream().allMatch(TeamResult::isValid)
                && uniquePlayers()
                && !results.isEmpty()
                && timestamp != null;
    }

    private boolean uniquePlayers() {
        return getAllPlayers().size() == 4;
    }

    private Set<FoosballPlayer> getAllPlayers() {
        return results.stream().flatMap(r -> r.getPlayers().stream()).collect(Collectors.toSet());
    }

    public boolean playerInMatch(final String playerName) {
        return results.stream()
                .map(TeamResult::getPlayers)
                .flatMap(Collection::stream)
                .map(FoosballPlayer::getName)
                .anyMatch(n -> n.equals(playerName));
    }

    public boolean didPlayerWin(final String playerName) {
        if (!playerInMatch(playerName)) throw new IllegalArgumentException("Player " + playerName + " not in game");

        return results.stream()
                .filter(result -> result.getScore() == 10)
                .map(TeamResult::getPlayers)
                .flatMap(Collection::stream)
                .anyMatch(player -> player.name.equals(playerName));
    }


}
