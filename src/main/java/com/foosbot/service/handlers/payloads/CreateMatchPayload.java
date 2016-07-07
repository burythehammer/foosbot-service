package com.foosbot.service.handlers.payloads;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foosbot.service.handlers.Validates;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Data
@JsonIgnoreProperties(value = { "valid" })
public class CreateMatchPayload implements Validates {

    public FoosballPlayer reporter;
    public Set<FoosballTeamResult> results;

    public boolean isValid() {
        return reporter != null &&
                reporter.isValid() &&
                results != null &&
                scoresValid() &&
                results.stream().allMatch(FoosballTeamResult::isValid);
    }

    private boolean scoresValid() {
        final List<Integer> scores = getScores();
        return scores.contains(10) && scores.stream().filter(v -> v > 0 && v < 10).collect(Collectors.toList()).size() == 1;
    }

    private List<Integer> getScores() {
        return results.stream()
                .map(FoosballTeamResult::getScore)
                .collect(toList());
    }

}
