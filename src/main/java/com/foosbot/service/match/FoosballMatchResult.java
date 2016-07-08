package com.foosbot.service.match;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foosbot.service.handlers.Validates;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@JsonIgnoreProperties(value = {"valid"})
@AllArgsConstructor
public class FoosballMatchResult implements Validates {
    public UUID uuid;
    public FoosballPlayer reporter;
    public Set<FoosballTeamResult> results;
    public String timestamp;

    public boolean isValid() {

        return uuid != null
                && reporter.isValid()
                && !results.isEmpty()
                && timestamp != null;
    }
}
