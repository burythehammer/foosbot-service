package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.model.Model;

import java.util.Map;
import java.util.UUID;


public class DeleteMatchHandler extends AbstractRequestHandler<EmptyPayload> {
    public DeleteMatchHandler(final Model model) {
        super(EmptyPayload.class, model);
    }

    @Override
    protected Answer processImpl(final EmptyPayload value, final Map<String, String> urlParams) {

        if (!urlParams.containsKey(":uuid")) {
            throw new IllegalArgumentException();
        }

        final UUID uuid;

        try {
            uuid = UUID.fromString(urlParams.get(":uuid"));
        } catch (final IllegalArgumentException e) {
            return new Answer(404, urlParams.get(":uuid") + " not a valid UUID");
        }

        try {
            model.deleteMatch(uuid);
        } catch (final IllegalArgumentException e){
            return new Answer(404, "Match not found: " + uuid);
        }

        return Answer.ok(null);
    }
}
