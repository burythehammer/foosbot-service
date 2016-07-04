package com.foosbot.service.match;

import com.foosbot.service.players.FoosballPlayer;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

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

    public static FoosballMatch createNewMatch(final CreateNewFoosballMatchDTO createNewFoosballMatchDTO) {
        final FoosballMatch foosballMatch = new FoosballMatch();
        foosballMatch.setId(UUID.randomUUID().toString());
        foosballMatch.setReporter(createNewFoosballMatchDTO.reporter);
        foosballMatch.setResults(createNewFoosballMatchDTO.results);
        foosballMatch.timestamp = Instant.now().toString();
        return foosballMatch;
    }
}
