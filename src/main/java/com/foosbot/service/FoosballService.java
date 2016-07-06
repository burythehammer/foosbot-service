package com.foosbot.service;

import com.beust.jcommander.JCommander;
import com.foosbot.service.handlers.FoosballRequestsHandler;
import com.foosbot.service.model.Model;
import com.foosbot.service.model.Sql2oModel;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;

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

        Sql2o sql2o = new Sql2o("jdbc:postgresql://" + options.dbHost + ":" + options.dbPort + "/" + options.database,
                options.dbUsername, options.dbPassword, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        Model model = new Sql2oModel();

        // test
        get("/hello", (request, response) -> "Hello World");

        // matches
        get("/match/:id", );
        post("/match/", "application/json", matchService::addMatchResult);
        post("/match/batch/", matchService::addMatchResult);
        delete("/match/:id", matchService::deleteMatch);

        // players
        get("/player/:name", FoosballRequestsHandler::getPlayerStats);

        // season
        get("/season/:name", FoosballRequestsHandler::getSeason);
        get("/season/:name/rank", FoosballRequestsHandler::getSeasonRank);
        post("/season/", FoosballRequestsHandler::createSeason);


        // Global rank
        get("/rank/", FoosballRequestsHandler::getGlobalRanks);

    }


}