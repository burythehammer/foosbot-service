package com.foosbot.service;

import com.beust.jcommander.Parameter;

public class CommandLineOptions {

    @Parameter(names = "--debug")
    boolean debug = false;

    @Parameter(names = {"--service-port"})
    public Integer servicePort = 4567;

    @Parameter(names = {"--database"})
    public String database = "oc_foosball_test";

    @Parameter(names = {"--db-host"})
    public String dbHost = "localhost";

    @Parameter(names = {"--db-username"})
    public String dbUsername = "foosball_service";

    @Parameter(names = {"--db-password"})
    public String dbPassword = "test";

    @Parameter(names = {"--db-port"})
    public Integer dbPort = 5432;
}

