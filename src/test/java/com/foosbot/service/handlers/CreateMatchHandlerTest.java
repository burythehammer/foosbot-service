package com.foosbot.service.handlers;


import com.foosbot.service.handlers.payloads.CreateMatchPayload;
import com.foosbot.service.match.TeamResult;
import com.foosbot.service.model.Model;
import com.foosbot.service.model.players.FoosballPlayer;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

public class CreateMatchHandlerTest {

    private static final UUID uuid = UUID.fromString("728084e8-7c9a-4133-a9a7-f2bb491ef436");

    private static final FoosballPlayer BLANK_PLAYER = FoosballPlayer.of("");
    private static final FoosballPlayer PLAYER_1 = FoosballPlayer.of("@matthew");
    private static final FoosballPlayer PLAYER_2 = FoosballPlayer.of("@mark");
    private static final FoosballPlayer PLAYER_3 = FoosballPlayer.of("@luke");
    private static final FoosballPlayer PLAYER_4 = FoosballPlayer.of("@john");
    private static final FoosballPlayer REPORTER = FoosballPlayer.of("@mary");

    @Test
    public void createMatchSuccessfully() throws Exception {

        final TeamResult result1 = new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 5);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 10);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(REPORTER);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isTrue();

        final Model model = EasyMock.createMock(Model.class);
        expect(model.addMatchResult(REPORTER, results)).andReturn(uuid);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(201, uuid.toString()));

        verify(model);
    }


    @Test
    public void neitherTeamScore10() throws Exception {
        

        final int team1score = 5;
        final int team2score = 5;

        final Set<FoosballPlayer> team1 = ImmutableSet.of(PLAYER_1, PLAYER_2);
        final Set<FoosballPlayer> team2 = ImmutableSet.of(PLAYER_3, PLAYER_4);

        final TeamResult result1 = new TeamResult(team1, team1score);
        final TeamResult result2 = new TeamResult(team2, team2score);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(REPORTER);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }

    @Test
    public void bothTeamsScore10() throws Exception {

        final int team1score = 10;
        final int team2score = 10;

        final Set<FoosballPlayer> team1 = ImmutableSet.of(PLAYER_1, PLAYER_2);
        final Set<FoosballPlayer> team2 = ImmutableSet.of(PLAYER_3, PLAYER_4);

        final TeamResult result1 = new TeamResult(team1, team1score);
        final TeamResult result2 = new TeamResult(team2, team2score);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(REPORTER);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }

    @Test
    public void oneResult() throws Exception {

        final Set<FoosballPlayer> team1 = ImmutableSet.of(PLAYER_1, PLAYER_2);
        final Set<TeamResult> results = ImmutableSet.of(new TeamResult(team1, 5));

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setResults(results);
        createMatchPayload.setReporter(REPORTER);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }

    @Test
    public void threeResults() throws Exception {
        final FoosballPlayer player5 = FoosballPlayer.of("@abraham");
        final FoosballPlayer player6 = FoosballPlayer.of("@moses");

        final TeamResult result1 = new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 10);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 5);
        final TeamResult result3 = new TeamResult(ImmutableSet.of(player5, player6), 3);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2, result3);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(REPORTER);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }


    @Test
    public void emptyPlayerSets() throws Exception {

        final TeamResult result1 = new TeamResult(Collections.emptySet(), 10);
        final TeamResult result2 = new TeamResult(Collections.emptySet(), 5);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(REPORTER);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap()))
                .isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }

    @Test
    public void blankPlayers() throws Exception {

        final TeamResult result1 = new TeamResult(ImmutableSet.of(BLANK_PLAYER, BLANK_PLAYER), 10);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(BLANK_PLAYER, BLANK_PLAYER), 5);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(REPORTER);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap()))
                .isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }

    @Test
    public void emptyReporter() throws Exception {

        final TeamResult result1 = new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 5);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 10);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);


        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(BLANK_PLAYER);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }


    @Test
    public void nullScores() throws Exception {
        final Set<TeamResult> results = null;

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(REPORTER);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }


    @Test
    public void nullReporter() throws Exception {
        final FoosballPlayer reporter = null;

        final TeamResult result1 = new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 5);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 10);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);


        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }

    @Test
    public void nullPlayers() throws Exception {

        final TeamResult result1 = new TeamResult(Sets.newHashSet(null, null), 5);
        final TeamResult result2 = new TeamResult(Sets.newHashSet(null, null), 10);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(REPORTER);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400, "Invalid payload"));

        verify(model);
    }




}