package com.foosbot.service.handlers;


import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.model.Model;
import com.google.common.collect.ImmutableMap;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

public class DeleteMatchHandlerTest {

    private static final UUID uuid = UUID.fromString("728084e8-7c9a-4133-a9a7-f2bb491ef436");


    @Test
    public void deleteMatch() throws Exception {

        final EmptyPayload payload = new EmptyPayload();

        final Model model = EasyMock.createMock(Model.class);
        model.deleteMatch(uuid);
        EasyMock.expectLastCall().andVoid();
        replay(model);

        final DeleteMatchHandler handler = new DeleteMatchHandler(model);

        final Answer answer = handler.process(payload, ImmutableMap.of(":uuid", uuid.toString()));
        assertThat(answer).isEqualTo(new Answer(200, null));

        verify(model);
    }

    @Test
    public void deleteNonExistentMatch() throws Exception {

        final EmptyPayload payload = new EmptyPayload();

        final Model model = EasyMock.createMock(Model.class);
        model.deleteMatch(uuid);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
        replay(model);

        final DeleteMatchHandler handler = new DeleteMatchHandler(model);

        final String expectedOutput = "Match not found: " + uuid;

        final Answer answer = handler.process(payload, ImmutableMap.of(":uuid", uuid.toString()));
        assertThat(answer).isEqualTo(new Answer(404, expectedOutput));

        verify(model);
    }

}