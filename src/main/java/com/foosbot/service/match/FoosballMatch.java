package com.foosbot.service.match;


import com.foosbot.service.model.players.FoosballPlayer;
import lombok.Data;

import java.util.Set;

@Data
public class FoosballMatch {
    public String id;
    public FoosballPlayer reporter;
    public Set<FoosballTeamResult> results;
    public String timestamp;

    public boolean isValid() {

        return !id.isEmpty()
                && reporter.isValid()
                && !results.isEmpty()
                && timestamp != null;
    }
}
