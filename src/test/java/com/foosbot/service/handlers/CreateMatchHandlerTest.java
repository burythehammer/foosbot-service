package com.foosbot.service.handlers;


import com.foosbot.service.handlers.payloads.CreateMatchPayload;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.Model;
import com.foosbot.service.model.players.FoosballPlayer;
import com.github.javafaker.Faker;
import com.google.common.collect.Sets;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

public class CreateMatchHandlerTest {

    public static final UUID uuid = UUID.fromString("728084e8-7c9a-4133-a9a7-f2bb491ef436");
    private Faker faker = new Faker();

    @Test
    public void createMatchSuccessfully() throws Exception {
        final FoosballPlayer reporter = makeFoosballPlayer();
        final Set<FoosballTeamResult> results = getResults(10, 1);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isTrue();

        Model model = EasyMock.createMock(Model.class);
        expect(model.addMatchResult(reporter, results)).andReturn(uuid);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(201, uuid.toString()));

        verify(model);
    }


    @Test
    public void neitherTeamScore10() throws Exception {
        final FoosballPlayer reporter = makeFoosballPlayer();
        final Set<FoosballTeamResult> results = getResults(5, 5);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        Model model = EasyMock.createMock(Model.class);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void bothTeamsScore10() throws Exception {
        final FoosballPlayer reporter = makeFoosballPlayer();
        final Set<FoosballTeamResult> results = getResults(10, 10);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        Model model = EasyMock.createMock(Model.class);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void oneResult() throws Exception {
        final FoosballPlayer reporter = makeFoosballPlayer();
        final Set<FoosballTeamResult> results = Collections.singleton(makeTeamResult(5));

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        Model model = EasyMock.createMock(Model.class);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void threeResults() throws Exception {
        final FoosballPlayer reporter = makeFoosballPlayer();
        final Set<FoosballTeamResult> results = getResults(10, 5);
        results.add(makeTeamResult(5));

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        Model model = EasyMock.createMock(Model.class);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }


    @Test
    public void emptyPlayerSets() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer();
        final Set<FoosballTeamResult> results = getResults(5, 10);

        for (FoosballTeamResult result : results) {
            result.setPlayers(new HashSet<>());
        }

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        Model model = EasyMock.createMock(Model.class);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void emptyReporter() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer();
        final Set<FoosballTeamResult> results = getResults(5, 10);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        Model model = EasyMock.createMock(Model.class);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void nullScores() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer();
        final Set<FoosballTeamResult> results = null;

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        Model model = EasyMock.createMock(Model.class);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }


    @Test
    public void nullReporter() throws Exception {
        final FoosballPlayer reporter = null;
        final Set<FoosballTeamResult> results = getResults(5, 10);

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        Model model = EasyMock.createMock(Model.class);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }

    @Test
    public void nullPlayers() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer();
        final Set<FoosballTeamResult> results = getResults(5, 10);

        for (FoosballTeamResult result : results) {
            result.setPlayers(null);
        }

        final CreateMatchPayload createMatchPayload = new CreateMatchPayload();
        createMatchPayload.setReporter(reporter);
        createMatchPayload.setResults(results);

        assertThat(createMatchPayload.isValid()).isFalse();

        Model model = EasyMock.createMock(Model.class);
        replay(model);

        CreateMatchHandler handler = new CreateMatchHandler(model);

        assertThat(handler.process(createMatchPayload, Collections.emptyMap())).isEqualTo(new Answer(400));

        verify(model);
    }


    private Set<FoosballTeamResult> getResults(int score1, int score2) {
        return Sets.newHashSet(makeTeamResult(score1), makeTeamResult(score2));
    }

    private FoosballTeamResult makeTeamResult(int score) {
        final FoosballTeamResult foosballTeamResult = new FoosballTeamResult();
        foosballTeamResult.setScore(score);
        foosballTeamResult.setPlayers(Sets.newHashSet(makeFoosballPlayer(), makeFoosballPlayer()));
        return foosballTeamResult;
    }

    private FoosballPlayer makeFoosballPlayer() {
        final FoosballPlayer foosballPlayer = new FoosballPlayer();
        foosballPlayer.setName(faker.name().firstName());
        return foosballPlayer;
    }

//    NewPostPayload newPost = new NewPostPayload();
//    newPost.setTitle(""); // this makes the post invalid
//    newPost.setContent("Bla bla bla");
//    assertFalse(newPost.isValid());
//
//    Model model = EasyMock.createMock(Model.class);
//    replay(model);
//
//    PostsCreateHandler handler = new PostsCreateHandler(model);
//    assertEquals(new Answer(400), handler.process(newPost, Collections.emptyMap(), false));
//    assertEquals(new Answer(400), handler.process(newPost, Collections.emptyMap(), true));
//
//    verify(model);

}