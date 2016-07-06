package com.foosbot.service.handlers.payloads;

import com.foosbot.service.handlers.Validates;


public class EmptyPayload implements Validates {

    @Override
    public boolean isValid() {
        return true;
    }
}
