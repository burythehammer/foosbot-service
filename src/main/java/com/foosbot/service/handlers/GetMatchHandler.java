package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.model.Model;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class GetMatchHandler extends AbstractRequestHandler<EmptyPayload> {
    public GetMatchHandler(final Model model) {
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

        final Optional<FoosballMatch> match = model.getMatchResult(uuid);

        if (!match.isPresent()) {
            return new Answer(404, "Match " + uuid + " not found");
        }

        return Answer.ok(dataToJson(match.get()));
    }
}
