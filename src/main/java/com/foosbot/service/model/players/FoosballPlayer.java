package com.foosbot.service.model.players;


import com.foosbot.service.handlers.Validates;
import com.foosbot.service.match.FoosballMatch;
import com.foosbot.service.model.Model;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FoosballPlayer implements Validates {

    public String name;


    public boolean isValid() {
        return name != null && !name.isEmpty();
    }

    public static FoosballPlayer of(final String name) {
        return new FoosballPlayer(name);
    }

}
