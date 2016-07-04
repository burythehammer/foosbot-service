package com.foosbot.service.players;


import lombok.Data;

@Data
public class FoosballPlayer {

    public String name;


    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
