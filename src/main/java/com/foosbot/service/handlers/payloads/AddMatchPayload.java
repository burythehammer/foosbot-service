package com.foosbot.service.handlers.payloads;


import com.foosbot.service.handlers.Validates;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.Data;

import java.util.Set;

@Data
public class AddMatchPayload implements Validates {

    public FoosballPlayer reporter;
    public Set<FoosballTeamResult> results;

    public boolean isValid() {
        return reporter.isValid() && results.size() == 2;
    }

}
