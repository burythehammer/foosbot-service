package com.foosbot.service.handlers;


import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.TeamResult;
import com.foosbot.service.model.Model;
import com.foosbot.service.model.players.FoosballPlayer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

public class GetMatchHandlerTest {

    private static final UUID uuid = UUID.fromString("728084e8-7c9a-4133-a9a7-f2bb491ef436");

    private static final FoosballPlayer PLAYER_1 = FoosballPlayer.of("@matthew");
    private static final FoosballPlayer PLAYER_2 = FoosballPlayer.of("@mark");
    private static final FoosballPlayer PLAYER_3 = FoosballPlayer.of("@luke");
    private static final FoosballPlayer PLAYER_4 = FoosballPlayer.of("@john");
    private static final FoosballPlayer REPORTER = FoosballPlayer.of("@mary");


    @Test
    public void getExistingMatch() throws Exception {

        final TeamResult result1 = new TeamResult(ImmutableSet.of(PLAYER_1, PLAYER_2), 5);
        final TeamResult result2 = new TeamResult(ImmutableSet.of(PLAYER_3, PLAYER_4), 10);

        final Set<TeamResult> results = ImmutableSet.of(result1, result2);
        final String timestamp = "2016-07-07T12:07:45.098Z";

        final FoosballMatch foosballMatch = new FoosballMatch(uuid, REPORTER, results, timestamp);
        final EmptyPayload payload = new EmptyPayload();

        final Model model = EasyMock.createMock(Model.class);
        expect(model.getMatchResult(uuid)).andReturn(Optional.of(foosballMatch));
        replay(model);

        final GetMatchHandler handler = new GetMatchHandler(model);

        final String expectedOutput =
                "{\n" +
                "  \"uuid\" : \"728084e8-7c9a-4133-a9a7-f2bb491ef436\",\n" +
                "  \"reporter\" : {\n" +
                "    \"name\" : \"@mary\"\n" +
                "  },\n" +
                "  \"results\" : [ {\n" +
                "    \"players\" : [ {\n" +
                "      \"name\" : \"@matthew\"\n" +
                "    }, {\n" +
                "      \"name\" : \"@mark\"\n" +
                "    } ],\n" +
                "    \"score\" : 5\n" +
                "  }, {\n" +
                "    \"players\" : [ {\n" +
                "      \"name\" : \"@luke\"\n" +
                "    }, {\n" +
                "      \"name\" : \"@john\"\n" +
                "    } ],\n" +
                "    \"score\" : 10\n" +
                "  } ],\n" +
                "  \"timestamp\" : \"2016-07-07T12:07:45.098Z\"\n" +
                "}";


        final Answer answer = handler.process(payload, ImmutableMap.of(":uuid", uuid.toString()));
        assertThat(answer).isEqualTo(new Answer(200, expectedOutput));

        verify(model);
    }

}