package com.foosbot.service.match;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foosbot.service.handlers.Validates;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data @AllArgsConstructor
@JsonIgnoreProperties(value = { "valid" })
public class FoosballMatch implements Validates{
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
