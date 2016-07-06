package com.foosbot.service.handlers;

import spark.Request;
import spark.Response;


public class FoosballRequestsHandler {

    public static void routes() {



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
