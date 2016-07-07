package com.foosbot.service;

import com.beust.jcommander.Parameter;

public class CommandLineOptions {

    @Parameter(names = "--debug")
    boolean debug = false;

    @Parameter(names = {"--service-port"})
    public Integer servicePort = 4567;

    @Parameter(names = {"--database"})
    public String database = "blog";

    @Parameter(names = {"--db-host"})
    public String dbHost = "localhost";

    @Parameter(names = {"--db-username"})
    public String dbUsername = "blog_owner";

    @Parameter(names = {"--db-password"})
    public String dbPassword = "sparkforthewin";

    @Parameter(names = {"--db-port"})
    public Integer dbPort = 5432;
}

