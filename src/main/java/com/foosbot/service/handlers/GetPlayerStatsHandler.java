package com.foosbot.service.handlers;

import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.model.Model;
import com.foosbot.service.model.players.PlayerStats;

import java.util.Map;
import java.util.Optional;


public class GetPlayerStatsHandler extends AbstractRequestHandler<EmptyPayload> {
    public GetPlayerStatsHandler(final Model model) {
        super(EmptyPayload.class, model);
    }

    @Override
    protected Answer processImpl(final EmptyPayload value, final Map<String, String> urlParams) {

        final String playerName = urlParams.get(":name");

        if (playerName == null) {
            throw new IllegalArgumentException();
        }

        final Optional<PlayerStats> playerStats = model.getPlayerStats(playerName);

        if (!playerStats.isPresent()) {
            return new Answer(404, "Player not found: " + playerName);
        }

        return Answer.ok(dataToJson(playerStats.get()));
    }
}
