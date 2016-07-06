package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.model.Model;

import java.util.Map;


public class HelloWorldHandler extends AbstractRequestHandler<EmptyPayload> {
    public HelloWorldHandler(Model model) {
        super(EmptyPayload.class, model);
    }

    @Override
    protected Answer processImpl(EmptyPayload value, Map<String, String> urlParams) {
        return Answer.ok(model.hello());
    }

}
