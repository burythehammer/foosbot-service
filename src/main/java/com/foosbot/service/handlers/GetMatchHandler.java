package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.match.FoosballMatchResult;
import com.foosbot.service.model.Model;
import org.eclipse.jetty.http.HttpStatus;

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
            return new Answer(HttpStatus.NOT_FOUND_404, urlParams.get(":uuid") + " is not a valid UUID");
        }

        final Optional<FoosballMatchResult> match = model.getMatchResult(uuid);

        if (!match.isPresent()) {
            return new Answer(HttpStatus.NOT_FOUND_404, "Match \"" + uuid + "\" not found");
        }

        return Answer.ok(dataToJson(match.get()));
    }
}
