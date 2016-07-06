package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.model.Model;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class GetSingleMatchHandler extends AbstractRequestHandler<EmptyPayload> {
    public GetSingleMatchHandler(Model model) {
        super(EmptyPayload.class, model);
    }

    @Override
    protected Answer processImpl(EmptyPayload value, Map<String, String> urlParams) {

        if (!urlParams.containsKey(":uuid")) {
            throw new IllegalArgumentException();
        }

        UUID uuid;

        try {
            uuid = UUID.fromString(urlParams.get(":uuid"));
        } catch (IllegalArgumentException e) {
            return new Answer(404, "Match " + urlParams.get(":uuid") + " not a valid UUID");
        }

        Optional<FoosballMatch> match = model.getMatchResult(uuid);

        if (!match.isPresent()) {
            return new Answer(404, "Match " + uuid + " not found");
        }

        return Answer.ok(dataToJson(match.get()));
    }
}
