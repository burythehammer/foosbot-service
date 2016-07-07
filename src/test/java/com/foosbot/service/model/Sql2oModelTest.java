package com.foosbot.service.model;


@SuppressWarnings("OptionalGetWithoutIsPresent")
public class Sql2oModelTest extends ModelTest {


    @Override
    public InMemoryModel getModel() {
        return new InMemoryModel();
    }


}