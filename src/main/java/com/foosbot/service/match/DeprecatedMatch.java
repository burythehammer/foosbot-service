package com.foosbot.service.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foosbot.service.handlers.Validates;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Set;


@Data
@AllArgsConstructor
@JsonIgnoreProperties(value = {"valid"})
public class DeprecatedMatch implements Validates {

    public FoosballPlayer reporter;
    public Set<TeamResult> results;
    public Instant timestamp;

    public boolean isValid() {
        return reporter.isValid()
                && !results.isEmpty()
                && timestamp != null;
    }
}

