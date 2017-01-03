package com.foosbot.service.handlers.payloads;

import com.foosbot.service.match.TeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.Data;

import java.util.Set;

@Data
public class ExistingFoosballMatchPayload {
    public FoosballPlayer reporter;
    public Set<TeamResult> results;
    public String timestamp;

    public boolean isValid() {
        return reporter.isValid()
                && !results.isEmpty()
                && timestamp != null;
    }

}
