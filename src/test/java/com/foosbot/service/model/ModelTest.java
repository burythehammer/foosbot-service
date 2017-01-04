package com.foosbot.service.model;


import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.TeamResult;
import com.foosbot.service.model.players.FoosballPlayer;
import com.foosbot.service.model.players.PlayerStats;
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

    public static final ImmutableSet<FoosballPlayer> TEAM_1 = ImmutableSet.of(PLAYER_1, PLAYER_2);
    public static final ImmutableSet<FoosballPlayer> TEAM_2 = ImmutableSet.of(PLAYER_3, PLAYER_4);

    private static final TeamResult RESULT_1 = new TeamResult(TEAM_1, 5);
    private static final TeamResult RESULT_2 = new TeamResult(TEAM_2, 10);

    public static final Set<TeamResult> RESULTS = ImmutableSet.of(RESULT_1, RESULT_2);

    @Before
    public void setModel() {
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

        final UUID matchId = model.addMatchResult(REPORTER, RESULTS);

        final long now = Instant.now().toEpochMilli();
        final Optional<FoosballMatch> searchResult = model.getMatchResult(matchId);
        assertThat(searchResult).isPresent();

        final FoosballMatch foosballMatch = searchResult.get();

        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(foosballMatch.getUuid()).isEqualTo(matchId);
        softly.assertThat(foosballMatch.getReporter()).isEqualTo(REPORTER);
        softly.assertThat(foosballMatch.getResults()).containsExactlyElementsOf(RESULTS);
        softly.assertThat(Instant.parse(foosballMatch.getTimestamp()).toEpochMilli())
                .isCloseTo(now, Offset.offset(100L));

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

        final List<UUID> ids = Lists.newArrayList();

        final long now = Instant.now().toEpochMilli();

        for (int i = 0; i < 50; i++) {
            ids.add(model.addMatchResult(REPORTER, RESULTS));
        }

        final List<FoosballMatch> allMatchResults = model.getAllMatchResults();


        allMatchResults.forEach(foosballMatch -> {

            final SoftAssertions softly = new SoftAssertions();

            softly.assertThat(ids).contains(foosballMatch.getUuid());
            softly.assertThat(foosballMatch.reporter).isEqualTo(REPORTER);
            softly.assertThat(foosballMatch.results).containsExactlyElementsOf(RESULTS);
            softly.assertThat(Instant.parse(foosballMatch.timestamp).toEpochMilli())
                    .isCloseTo(now, Offset.offset(1000L));

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

        final UUID matchId = model.addMatchResult(REPORTER, RESULTS);
        final List<FoosballMatch> allMatchResults = model.getAllMatchResults();
        assertThat(allMatchResults).extracting(FoosballMatch::getUuid).contains(matchId);
    }


    @Test
    public void deleteMatch() throws Exception {


        final UUID matchId = model.addMatchResult(REPORTER, RESULTS);

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

    @Test
    public void getPlayerStatsForSingleMatch() throws Exception {

        final UUID matchId = model.addMatchResult(REPORTER, RESULTS);
        PlayerStats playerStats = model.getPlayerStats(PLAYER_1.name)
                .orElseThrow(() -> new IllegalStateException("Player stats was not present from model"));

        assertThat(playerStats.matchesLost).isEqualTo(1);
        assertThat(playerStats.matchesPlayed).isEqualTo(1);
        assertThat(playerStats.matchesWon).isZero();
        assertThat(playerStats.lastMatch).isEqualTo(matchId);
    }


    @Test
    public void samePersonOnTwoTeams() throws Exception {

        Throwable throwable = catchThrowable(() ->
                model.addMatchResult(REPORTER, ImmutableSet.of(
                        new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 10),
                        new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_3), 5)))
        );

        assertThat(throwable)
                .as("Model should throw error if player on two teams")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void samePersonOnTeamTwice() throws Exception {

        Throwable throwable = catchThrowable(() ->
                model.addMatchResult(REPORTER, ImmutableSet.of(
                        new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_1), 10),
                        new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 5)))
        );

        assertThat(throwable)
                .as("Model should throw error if player on same team twice")
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void getPlayerStatsForManyMatches() throws Exception {

        Set<TeamResult> losingMatch = buildTestResult(1, 10);
        Set<TeamResult> winningMatch = buildTestResult(10, 3);

        for (int i = 0; i < 30; i++) {
            model.addMatchResult(REPORTER, losingMatch);
        }

        for (int i = 0; i < 30; i++) {
            model.addMatchResult(REPORTER, winningMatch);
        }

        model.addMatchResult(REPORTER, ImmutableSet.of(
                new TeamResult(ImmutableSet.of(REPORTER, PLAYER_2), 10),
                new TeamResult(TEAM_2, 5)));

        Thread.sleep(1); // otherwise it clashes with other results. Don't really need more than 1 millisecond precision for now.. I hope!

        final UUID lastMatch = model.addMatchResult(REPORTER, winningMatch);

        PlayerStats playerStats = model.getPlayerStats(PLAYER_1.name)
                .orElseThrow(() -> new IllegalStateException("Player stats was not present from model"));

        assertThat(playerStats.matchesLost).isEqualTo(30);
        assertThat(playerStats.matchesWon).isEqualTo(31);
        assertThat(playerStats.matchesPlayed).isEqualTo(61);
        assertThat(playerStats.lastMatch).isEqualTo(lastMatch);
    }

    private Set<TeamResult> buildTestResult(int team1Score, int team2Score) {
        return ImmutableSet.of(
                new TeamResult(TEAM_1, team1Score),
                new TeamResult(TEAM_2, team2Score));
    }

}
