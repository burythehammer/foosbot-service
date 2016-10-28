package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.CreateMatchPayload;
import com.foosbot.service.match.FoosballTeamResult;
import com.foosbot.service.model.Model;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class CreateMatchHandler extends AbstractRequestHandler<CreateMatchPayload> {
    public CreateMatchHandler(final Model model) {
        super(CreateMatchPayload.class, model);
    }

    @Override
    protected Answer processImpl(final CreateMatchPayload value, final Map<String, String> urlParams) {

        final Set<FoosballTeamResult> results = value.getResults();

        if (results.size() != 2) return new Answer(HttpStatus.BAD_REQUEST_400, "Only two results per game result");

        final UUID id = model.addMatchResult(value.getReporter(), results);
        return new Answer(HttpStatus.CREATED_201, id.toString());
    }
}
