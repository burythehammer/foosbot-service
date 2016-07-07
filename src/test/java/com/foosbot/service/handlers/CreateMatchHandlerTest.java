package com.foosbot.service.handlers;


import com.foosbot.service.handlers.payloads.CreateMatchPayload;
import com.foosbot.service.match.FoosballTeamResult;
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

    @Test
    public void createMatchSuccessfully() throws Exception {

        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");

        final FoosballTeamResult result1 = new FoosballTeamResult(ImmutableSet.of(player1, player2), 5);
        final FoosballTeamResult result2 = new FoosballTeamResult(ImmutableSet.of(player3, player4), 10);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isTrue();

        final Model model = EasyMock.createMock(Model.class);
        expect(model.addMatchResult(reporter, results)).andReturn(uuid);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(201, uuid.toString()));

        verify(model);
    }


    @Test
    public void neitherTeamScore10() throws Exception {

        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");

        final int team1score = 5;
        final int team2score = 5;

        final Set<FoosballPlayer> team1 = ImmutableSet.of(player1, player2);
        final Set<FoosballPlayer> team2 = ImmutableSet.of(player3, player4);

        final FoosballTeamResult result1 = new FoosballTeamResult(team1, team1score);
        final FoosballTeamResult result2 = new FoosballTeamResult(team2, team2score);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void bothTeamsScore10() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");

        final int team1score = 10;
        final int team2score = 10;

        final Set<FoosballPlayer> team1 = ImmutableSet.of(player1, player2);
        final Set<FoosballPlayer> team2 = ImmutableSet.of(player3, player4);

        final FoosballTeamResult result1 = new FoosballTeamResult(team1, team1score);
        final FoosballTeamResult result2 = new FoosballTeamResult(team2, team2score);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void oneResult() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");

        final Set<FoosballPlayer> team1 = ImmutableSet.of(player1, player2);
        final Set<FoosballTeamResult> results = ImmutableSet.of(new FoosballTeamResult(team1, 5));

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setResults(results);
        createMatchPayload.setReporter(reporter);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void threeResults() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");
        final FoosballPlayer player5 = new FoosballPlayer("@abraham");
        final FoosballPlayer player6 = new FoosballPlayer("@moses");

        final FoosballTeamResult result1 = new FoosballTeamResult(ImmutableSet.of(player1, player2), 10);
        final FoosballTeamResult result2 = new FoosballTeamResult(ImmutableSet.of(player3, player4), 5);
        final FoosballTeamResult result3 = new FoosballTeamResult(ImmutableSet.of(player5, player6), 3);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2, result3);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }


    @Test
    public void emptyPlayerSets() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");

        final FoosballTeamResult result1 = new FoosballTeamResult(Collections.emptySet(), 10);
        final FoosballTeamResult result2 = new FoosballTeamResult(Collections.emptySet(), 5);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void blankPlayers() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("");
        final FoosballPlayer player2 = new FoosballPlayer("");
        final FoosballPlayer player3 = new FoosballPlayer("");
        final FoosballPlayer player4 = new FoosballPlayer("");

        final FoosballTeamResult result1 = new FoosballTeamResult(ImmutableSet.of(player1, player2), 10);
        final FoosballTeamResult result2 = new FoosballTeamResult(ImmutableSet.of(player3, player4), 5);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void emptyReporter() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");

        final FoosballTeamResult result1 = new FoosballTeamResult(ImmutableSet.of(player1, player2), 5);
        final FoosballTeamResult result2 = new FoosballTeamResult(ImmutableSet.of(player3, player4), 10);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);


        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }


    @Test
    public void nullScores() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final Set<FoosballTeamResult> results = null;

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }


    @Test
    public void nullReporter() throws Exception {
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");
        final FoosballPlayer reporter = null;

        final FoosballTeamResult result1 = new FoosballTeamResult(ImmutableSet.of(player1, player2), 5);
        final FoosballTeamResult result2 = new FoosballTeamResult(ImmutableSet.of(player3, player4), 10);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);


        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void nullPlayers() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");

        final FoosballTeamResult result1 = new FoosballTeamResult(Sets.newHashSet(null, null), 5);
        final FoosballTeamResult result2 = new FoosballTeamResult(Sets.newHashSet(null, null), 10);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        final Model model = EasyMock.createMock(Model.class);
        replay(model);

        final CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }




}