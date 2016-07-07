package com.foosbot.service;

import com.beust.jcommander.JCommander;
import com.foosbot.service.handlers.CreateMatchHandler;
import com.foosbot.service.handlers.GetMatchHandler;
import com.foosbot.service.handlers.HelloWorldHandler;
import com.foosbot.service.model.InMemoryModel;
import com.foosbot.service.model.Model;

import java.util.logging.Logger;

import static spark.Spark.get;
import static spark.Spark.post;

public class FoosballService {

    private static final Logger logger = Logger.getLogger(FoosballService.class.getCanonicalName());

    public static void main(String[] args) {

        CommandLineOptions options = new CommandLineOptions();
        new JCommander(options, args);

        logger.finest("Options.debug = " + options.debug);
        logger.finest("Options.database = " + options.database);
        logger.finest("Options.dbHost = " + options.dbHost);
        logger.finest("Options.dbUsername = " + options.dbUsername);
        logger.finest("Options.dbPort = " + options.dbPort);
        logger.finest("Options.servicePort = " + options.servicePort);

//        Model model = getSqlModel(options);
        Model model = getInMemoryModel();

        // test
        get("/hello", new HelloWorldHandler(model));

        // matches
        get("/match/:uuid", new GetMatchHandler(model));
        post("/match/", new CreateMatchHandler(model));

//        post("/match/batch/", new BatchMatchCreationHandler(model));
//        delete("/match/:uuid", new DeleteMatchHandler(model));
//
//        // players
//        get("/player/:name", new GetPlayerStatsHandler(model));
//
//        // season
//        get("/season/:name", new GetSeasonHandler(model));
//        get("/season/:name/rank", new GetSeasonRankHandler(model));
//        post("/season/", new NewSeasonHandler(model));
//
//
//        // Global rank
//        get("/rank/", new GetRankHandler(model));

    }

    private static Model getInMemoryModel() {
        return new InMemoryModel();
    }


}