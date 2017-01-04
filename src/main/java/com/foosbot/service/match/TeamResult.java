package com.foosbot.service.match;


import com.foosbot.service.handlers.Validates;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class TeamResult implements Validates {

    public Set<FoosballPlayer> players;
    public int score;

    @Override
    public boolean isValid() {
        return players != null &&
                !players.isEmpty() &&
                !players.contains(null) &&
                players.size() == 2 &&
                players.stream().allMatch(FoosballPlayer::isValid) &&
                score <= 10 &&
                score >= 0;
    }
}
