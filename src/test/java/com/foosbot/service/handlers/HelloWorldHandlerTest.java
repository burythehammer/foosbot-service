package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.model.Model;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;


public class HelloWorldHandlerTest {

    @Test
    public void helloWorld() throws Exception {
        Model model = EasyMock.createMock(Model.class);
        expect(model.hello()).andReturn("Hello World!");
        replay(model);

        HelloWorldHandler handler = new HelloWorldHandler(model);

        assertThat(handler.process(new EmptyPayload(), Collections.emptyMap()))
                .isEqualTo(new Answer(200, "Hello World!"));

        verify(model);
    }

}