package com.foosbot.service.handlers.payloads;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foosbot.service.handlers.Validates;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(value = {"valid"})
public class CreateMatchPayload implements Validates {

    public FoosballPlayer reporter;
    public Set<FoosballTeamResult> results;

    public boolean isValid() {
        return reporter != null &&
                reporter.isValid() &&
                results != null &&
                results.size() == 2 &&
                scoresValid() &&
                results.stream().allMatch(FoosballTeamResult::isValid);
    }

    private boolean scoresValid() {
        final Set<Integer> scores = getScores();
        return scores.contains(10) && nonMaxScoreExists(scores);
    }

    private boolean nonMaxScoreExists(final Set<Integer> scores) {
        return scores.stream().anyMatch(v -> v > 0 && v < 10);
    }

    private Set<Integer> getScores() {
        return results.stream()
                .map(FoosballTeamResult::getScore)
                .collect(Collectors.toSet());
    }

}
