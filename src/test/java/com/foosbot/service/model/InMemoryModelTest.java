package com.foosbot.service.model;


public class InMemoryModelTest extends ModelTest {


    @Override
    public InMemoryModel getModel() {
        return new InMemoryModel();
    }


}