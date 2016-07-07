package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.CreateMatchPayload;
import com.foosbot.service.model.Model;

import java.util.Map;
import java.util.UUID;


public class CreateMatchHandler extends AbstractRequestHandler<CreateMatchPayload> {
    public CreateMatchHandler(final Model model) {
        super(CreateMatchPayload.class, model);
    }

    @Override
    protected Answer processImpl(final CreateMatchPayload value, final Map<String, String> urlParams) {
        final UUID id = model.addMatchResult(value.getReporter(), value.getResults());
        return new Answer(201, id.toString());
    }
}
