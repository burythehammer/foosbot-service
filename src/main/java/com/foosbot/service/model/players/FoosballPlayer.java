package com.foosbot.service.model.players;


import com.foosbot.service.handlers.Validates;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FoosballPlayer implements Validates {

    public String name;

    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
