package com.foosbot.service;

import com.beust.jcommander.JCommander;
import com.foosbot.service.handlers.AddMatchHandler;
import com.foosbot.service.handlers.GetSingleMatchHandler;
import com.foosbot.service.handlers.HelloWorldHandler;
import com.foosbot.service.model.Model;
import com.foosbot.service.model.Sql2oModel;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.util.UUID;
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

        Model model = getSqlModel(options);

        // test
        get("/hello", new HelloWorldHandler(model));

        // matches
        get("/match/:uuid", new GetSingleMatchHandler(model));
        post("/match/", new AddMatchHandler(model));

//        post("/match/batch/", new AddManyMatchHandler(model));
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

    private static Sql2oModel getSqlModel(CommandLineOptions options) {
        Sql2o sql2o = new Sql2o("jdbc:postgresql://" + options.dbHost + ":" + options.dbPort + "/" + options.database,
                options.dbUsername, options.dbPassword, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        return new Sql2oModel(sql2o);
    }


}