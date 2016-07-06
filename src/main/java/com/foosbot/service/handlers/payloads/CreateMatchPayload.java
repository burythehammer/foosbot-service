package com.foosbot.service.handlers.payloads;


import com.foosbot.service.handlers.Validates;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Data
public class CreateMatchPayload implements Validates {

    public FoosballPlayer reporter;
    public Set<FoosballTeamResult> results;

    public boolean isValid() {
        return reporter != null &&
                reporter.isValid() &&
                resultsValid();
    }

    private boolean resultsValid() {
        final List<Integer> collect = results.stream()
                .map(FoosballTeamResult::getScore)
                .collect(toList());

        return collect.contains(10) && collect.stream().filter(v -> v > 0 && v < 10).collect(Collectors.toList()).size() == 1;
    }

}
