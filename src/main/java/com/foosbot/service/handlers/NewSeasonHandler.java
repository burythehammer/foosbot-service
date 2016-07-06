package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.NewSeasonPayload;
import com.foosbot.service.model.Model;

import java.util.Map;


public class NewSeasonHandler extends AbstractRequestHandler<NewSeasonPayload> {
    public NewSeasonHandler(Model model) {
        super(NewSeasonPayload.class, model);
    }

    @Override
    protected Answer processImpl(NewSeasonPayload value, Map<String, String> urlParams) {
        return null;
    }
}
