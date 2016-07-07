package com.foosbot.service.handlers;


import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.match.FoosballTeamResult;
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


    @Test
    public void getExistingMatch() throws Exception {
        final FoosballPlayer reporter = new FoosballPlayer("@mary");
        final FoosballPlayer player1 = new FoosballPlayer("@matthew");
        final FoosballPlayer player2 = new FoosballPlayer("@mark");
        final FoosballPlayer player3 = new FoosballPlayer("@luke");
        final FoosballPlayer player4 = new FoosballPlayer("@john");

        final FoosballTeamResult result1 = new FoosballTeamResult(ImmutableSet.of(player1, player2), 5);
        final FoosballTeamResult result2 = new FoosballTeamResult(ImmutableSet.of(player3, player4), 10);

        final Set<FoosballTeamResult> results = ImmutableSet.of(result1, result2);
        final String timestamp = "2016-07-07T12:07:45.098Z";

        final FoosballMatch foosballMatch = new FoosballMatch(uuid.toString(), reporter, results, timestamp);
        final EmptyPayload payload = new EmptyPayload();

        final Model model = EasyMock.createMock(Model.class);
        expect(model.getMatchResult(uuid)).andReturn(Optional.of(foosballMatch));
        replay(model);

        final GetMatchHandler handler = new GetMatchHandler(model);

        final String expectedOutput =
                "{\n" +
                "  \"id\" : \"728084e8-7c9a-4133-a9a7-f2bb491ef436\",\n" +
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