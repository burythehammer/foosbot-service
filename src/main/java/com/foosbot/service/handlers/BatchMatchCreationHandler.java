package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.BatchMatchCreatePayload;
import com.foosbot.service.model.Model;

import java.util.Map;


public class BatchMatchCreationHandler extends AbstractRequestHandler<BatchMatchCreatePayload> {
    public BatchMatchCreationHandler(Model model) {
        super(BatchMatchCreatePayload.class, model);
    }

    @Override
    protected Answer processImpl(BatchMatchCreatePayload value, Map<String, String> urlParams) {
        return null;
    }
}
