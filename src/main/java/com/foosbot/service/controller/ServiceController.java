package com.foosbot.service.controller;

import spark.Request;
import spark.Response;

import static spark.Spark.*;


public class ServiceController {

    public static void routes() {

        final MatchService matchService = new MatchService();

        // test
        get("/hello", (request, response) -> "Hello World");

        // matches
        get("/match/:id", matchService::getMatchResult);
        post("/match/", "application/json", matchService::addMatchResult);
        post("/match/batch/", matchService::addMatchResult);
        delete("/match/:id", matchService::deleteMatch);

        // players
        get("/player/:name", ServiceController::getPlayerStats);

        // season
        get("/season/:name", ServiceController::getSeason);
        get("/season/:name/rank", ServiceController::getSeasonRank);
        post("/season/", ServiceController::createSeason);


        // Global rank
        get("/rank/", ServiceController::getGlobalRanks);

    }


    private static String getPlayerStats(final Request request, final Response response) {
        return "Getting stats for player " + request.params(":name");
    }


    private static String getSeasonRank(final Request request, final Response response) {
        return "Getting ranks for season " + request.params(":name");
    }

    private static String getGlobalRanks(final Request request, final Response response) {
        return "Getting stats for season " + request.params(":name");
    }

    private static String getSeason(final Request request, final Response response) {
        return "Getting stats for season " + request.params(":name");
    }

    private static String createSeason(final Request request, final Response response) {
        return "Creating new season";
    }
}
