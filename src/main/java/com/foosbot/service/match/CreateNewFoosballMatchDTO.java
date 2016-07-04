package com.foosbot.service.match;

import com.foosbot.service.players.FoosballPlayer;
import lombok.Data;

import java.util.Set;

@Data
public class CreateNewFoosballMatchDTO {
    public FoosballPlayer reporter;
    public Set<FoosballTeamResult> results;

    public boolean isValid() {
        return reporter.isValid() && !results.isEmpty();
    }

}
