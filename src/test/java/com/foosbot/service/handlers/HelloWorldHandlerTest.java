package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.model.Model;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Collections;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;


public class HelloWorldHandlerTest {

    @Test
    public void helloWorld() throws Exception {
        Model model = EasyMock.createMock(Model.class);
        replay(model);

        HelloWorldHandler handler = new HelloWorldHandler(model);
        assertEquals(new Answer(200, "Hello World"), handler.process(new EmptyPayload(), Collections.emptyMap()));
        verify(model);
    }

}