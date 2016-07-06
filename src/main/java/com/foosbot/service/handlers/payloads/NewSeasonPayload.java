package com.foosbot.service.handlers.payloads;


import com.foosbot.service.handlers.Validates;
import lombok.Data;

@Data
public class NewSeasonPayload implements Validates {
    @Override
    public boolean isValid() {
        return false;
    }
}
