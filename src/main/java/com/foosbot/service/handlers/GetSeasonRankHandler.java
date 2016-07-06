package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.model.Model;

import java.util.Map;


public class GetSeasonRankHandler extends AbstractRequestHandler<EmptyPayload> {
    public GetSeasonRankHandler(Model model) {
        super(EmptyPayload.class, model);
    }

    @Override
    protected Answer processImpl(EmptyPayload value, Map<String, String> urlParams) {
        return null;
    }
}
