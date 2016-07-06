package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.AddMatchPayload;
import com.foosbot.service.model.Model;

import java.util.Map;
import java.util.UUID;


public class AddMatchHandler extends AbstractRequestHandler<AddMatchPayload> {
    public AddMatchHandler(Model model) {
        super(AddMatchPayload.class, model);
    }

    @Override
    protected Answer processImpl(AddMatchPayload value, Map<String, String> urlParams) {
        UUID id = model.addMatchResult(value.getReporter(), value.getResults());
        return new Answer(201, id.toString());
    }
}
