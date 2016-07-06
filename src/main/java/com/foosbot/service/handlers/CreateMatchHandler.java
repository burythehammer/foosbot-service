package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.CreateMatchPayload;
import com.foosbot.service.model.Model;

import java.util.Map;
import java.util.UUID;


public class CreateMatchHandler extends AbstractRequestHandler<CreateMatchPayload> {
    public CreateMatchHandler(Model model) {
        super(CreateMatchPayload.class, model);
    }

    @Override
    protected Answer processImpl(CreateMatchPayload value, Map<String, String> urlParams) {
        UUID id = model.addMatchResult(value.getReporter(), value.getResults());
        return new Answer(201, id.toString());
    }
}
