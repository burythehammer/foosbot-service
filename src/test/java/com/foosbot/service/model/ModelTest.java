package com.foosbot.service.model;


import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.TeamResult;
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
import static org.assertj.core.api.Assertions.catchThrowable;

public abstract class ModelTest {


    private Model model;

    public abstract Model getModel();

    private static final FoosballPlayer PLAYER_1 = FoosballPlayer.of("@matthew");
    private static final FoosballPlayer PLAYER_2 = FoosballPlayer.of("@mark");
    private static final FoosballPlayer PLAYER_3 = FoosballPlayer.of("@luke");
    private static final FoosballPlayer PLAYER_4 = FoosballPlayer.of("@john");
    private static final FoosballPlayer REPORTER = FoosballPlayer.of("@mary");

    @Before
    public void setModel(){
        this.model = getModel();
        model.clean();
    }

    @Test
    public void hello() throws Exception {
        final String actual = model.hello();
        assertThat(actual).isEqualToIgnoringCase("Hello, World");
    }

    @Test
    public void getMatchResult() throws Exception {
       

        final TeamResult result1 = new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 5);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 10);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);

        final UUID matchId = model.addMatchResult(REPORTER, results);

        final long now = Instant.now().toEpochMilli();
        final Optional<FoosballMatch> searchResult = model.getMatchResult(matchId);
        assertThat(searchResult).isPresent();

        final FoosballMatch foosballMatch = searchResult.get();

        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(foosballMatch.getUuid()).isEqualTo(matchId);
        softly.assertThat(foosballMatch.getReporter()).isEqualTo(REPORTER);
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


        final TeamResult result1 = new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 5);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 10);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);
        final List<UUID> ids = Lists.newArrayList();

        final long now = Instant.now().toEpochMilli();

        for (int i = 0; i < 50; i++) {
            ids.add(model.addMatchResult(REPORTER, results));
        }

        final List<FoosballMatch> allMatchResults = model.getAllMatchResults();


        allMatchResults.forEach(foosballMatch -> {

            final SoftAssertions softly = new SoftAssertions();

            softly.assertThat(ids).contains(foosballMatch.getUuid());
            softly.assertThat(foosballMatch.getReporter()).isEqualTo(REPORTER);
            softly.assertThat(foosballMatch.getResults()).containsExactlyElementsOf(results);
            softly.assertThat(Instant.parse(foosballMatch.getTimestamp()).toEpochMilli()).isCloseTo(now, Offset.offset(1000L));

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

        final TeamResult result1 = new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 5);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 10);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);

        final UUID matchId = model.addMatchResult(REPORTER, results);

        final List<FoosballMatch> allMatchResults = model.getAllMatchResults();

        assertThat(allMatchResults).extracting(FoosballMatch::getUuid).contains(matchId);
    }


    @Test
    public void deleteMatch() throws Exception {

        final TeamResult result1 = new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 5);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 10);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);

        final UUID matchId = model.addMatchResult(REPORTER, results);

        model.deleteMatch(matchId);

        final List<FoosballMatch> allMatchResults = model.getAllMatchResults();
        assertThat(allMatchResults).isEmpty();

        final Optional<FoosballMatch> matchResult = model.getMatchResult(matchId);
        assertThat(matchResult).isNotPresent();
    }


    @Test
    public void deleteNonExistentMatch() throws Exception {
        final UUID uuid = UUID.randomUUID();
        final Throwable throwable = catchThrowable(() -> model.deleteMatch(uuid));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
