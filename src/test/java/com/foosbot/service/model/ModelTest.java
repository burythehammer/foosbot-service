package com.foosbot.service.model;


import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import com.google.common.collect.ImmutableSet;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ModelTest {


    private Model model;

    public abstract Model getModel();

    @Before
    public void setModel(){
        this.model = getModel();
    }

    @Test
    public void hello() throws Exception {
        final String actual = model.hello();
        assertThat(actual).isEqualToIgnoringCase("Hello, World");
    }

    @Test
    public void getMatchResult() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");

        final FoosballTeamResult result1 = new FoosballTeamResult(ImmutableSet.of(player1, player2), 5);
        final FoosballTeamResult result2 = new FoosballTeamResult(ImmutableSet.of(player3, player4), 10);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);

        final UUID matchId = model.addMatchResult(reporter, results);

        final long now = Instant.now().toEpochMilli();
        final Optional<FoosballMatch> searchResult = model.getMatchResult(matchId);
        assertThat(searchResult).isPresent();

        final FoosballMatch foosballMatch = searchResult.get();

        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(foosballMatch.getUuid()).isEqualTo(matchId);
        softly.assertThat(foosballMatch.getReporter()).isEqualTo(reporter);
        softly.assertThat(foosballMatch.getResults()).containsExactlyElementsOf(results);
        softly.assertThat(Instant.parse(foosballMatch.getTimestamp()).toEpochMilli()).isCloseTo(now, Offset.offset(100L));

        softly.assertAll();
    }

    @Test
    public void getEmptyMatchResult() throws Exception {
        final UUID uuid = UUID.randomUUID();
        final Optional<FoosballMatch> matchResult = model.getMatchResult(uuid);
        assertThat(matchResult).isNotPresent();
    }

    @Test
    public void getAllMatchResults() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");

        final FoosballTeamResult result1 = new FoosballTeamResult(ImmutableSet.of(player1, player2), 5);
        final FoosballTeamResult result2 = new FoosballTeamResult(ImmutableSet.of(player3, player4), 10);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);
        final List<UUID> ids = Lists.newArrayList();

        final long now = Instant.now().toEpochMilli();

        for (int i = 0; i < 50; i++) {
            ids.add(model.addMatchResult(reporter, results));
        }

        final List<FoosballMatch> allMatchResults = model.getAllMatchResults();


        allMatchResults.forEach(foosballMatch -> {

            final SoftAssertions softly = new SoftAssertions();

            softly.assertThat(ids).contains(foosballMatch.getUuid());
            softly.assertThat(foosballMatch.getReporter()).isEqualTo(reporter);
            softly.assertThat(foosballMatch.getResults()).containsExactlyElementsOf(results);
            softly.assertThat(Instant.parse(foosballMatch.getTimestamp()).toEpochMilli()).isCloseTo(now, Offset.offset(100L));

            softly.assertAll();

        });

    }

    @Test
    public void getAllMatchResultsWhenEmpty() throws Exception {
        final List<FoosballMatch> allMatchResults = model.getAllMatchResults();
        assertThat(allMatchResults).isEmpty();
    }

    @Test
    public void addMatchResult() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");

        final FoosballTeamResult result1 = new FoosballTeamResult(ImmutableSet.of(player1, player2), 5);
        final FoosballTeamResult result2 = new FoosballTeamResult(ImmutableSet.of(player3, player4), 10);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);

        final UUID matchId = model.addMatchResult(reporter, results);

        final List<FoosballMatch> allMatchResults = model.getAllMatchResults();

        assertThat(allMatchResults).extracting(FoosballMatch::getUuid).contains(matchId);
    }

//    @Test
//    public void addMatchResults() throws Exception {
//
//    }

//    @Test
//    public void deleteMatch() throws Exception {
//
//    }
}
