package com.foosbot.service.model;


import com.foosbot.service.CommandLineOptions;

public class Sql2oModelTest extends ModelTest {


    @Override
    public Sql2oModel getModel() {
        final CommandLineOptions commandLineOptions = new CommandLineOptions();
        return Sql2oModel.getSqlModel(commandLineOptions);
    }


}