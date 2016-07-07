package com.foosbot.service.model;


@SuppressWarnings("OptionalGetWithoutIsPresent")
public class InMemoryModelTest extends ModelTest {


    @Override
    public InMemoryModel getModel() {
        return new InMemoryModel();
    }


}