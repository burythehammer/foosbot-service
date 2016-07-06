package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;

import java.util.Map;


public class HelloWorldHandler extends AbstractRequestHandler<EmptyPayload> {
    public HelloWorldHandler() {
        super(EmptyPayload.class, null);
    }

    @Override
    protected Answer processImpl(EmptyPayload value, Map<String, String> urlParams) {
        return Answer.ok("Hello World");
    }

}
