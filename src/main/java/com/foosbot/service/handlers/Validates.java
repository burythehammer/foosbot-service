package com.foosbot.service.handlers;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "valid" })
public interface Validates {


    boolean isValid();
}
