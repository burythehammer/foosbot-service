package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.AddManyMatchPayload;
import com.foosbot.service.model.Model;

import java.util.Map;


public class AddManyMatchHandler extends AbstractRequestHandler<AddManyMatchPayload> {
    public AddManyMatchHandler(Model model) {
        super(AddManyMatchPayload.class, model);
    }

    @Override
    protected Answer processImpl(AddManyMatchPayload value, Map<String, String> urlParams) {
        return null;
    }
}
