package com.foosbot.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foosbot.service.match.CreateNewFoosballMatchDTO;
import com.foosbot.service.match.FoosballMatch;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;


public class MatchService {

    Map<String, FoosballMatch> foosballMatches = new HashMap<>();


    public Object addMatchResult(final Request request, final Response response) {

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final CreateNewFoosballMatchDTO createNewFoosballMatchDTO = mapper.readValue(request.body(), CreateNewFoosballMatchDTO.class);

            if (!createNewFoosballMatchDTO.isValid()) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return "Not a valid foosball match dto: " + createNewFoosballMatchDTO;
            }

            final FoosballMatch match = FoosballMatch.createNewMatch(createNewFoosballMatchDTO);

            foosballMatches.put(match.id, match);

            response.status(HttpStatus.CREATED_201);
            response.type("application/json");

            return match;

        } catch (final Exception jpe) {
            response.status(HttpStatus.BAD_REQUEST_400);
            return "Error parsing foosball match dto: " + request.body();
        }
    }

    public Object getMatchResult(final Request request, final Response response) {
        final String id = request.params(":id");
        final FoosballMatch foosballMatch = foosballMatches.get(id);

        if (foosballMatch == null){
            response.status(HttpStatus.NOT_FOUND_404);
            return "Could not find match " + id;
        }

        response.status(HttpStatus.OK_200);
        response.type("application/json");

        return foosballMatch;
    }


    public String deleteMatch(final Request request, final Response response) {
        return "Deleting match " + request.params(":id");
    }

}
