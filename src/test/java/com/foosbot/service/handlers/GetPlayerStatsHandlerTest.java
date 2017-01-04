package com.foosbot.service.handlers;


import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.model.Model;
import com.foosbot.service.model.players.FoosballPlayer;
import com.foosbot.service.model.players.PlayerStats;
import com.google.common.collect.ImmutableMap;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class GetPlayerStatsHandlerTest {

    private static final FoosballPlayer PLAYER = FoosballPlayer.of("@matthew");
    private static final UUID uuid = UUID.fromString("71828403-28b8-41e0-9c01-b0a93518bf44");

    @Test
    public void getPlayerStats() throws Exception {

        PlayerStats playerStats = PlayerStats.builder()
                .matchesWon(5)
                .matchesLost(7)
                .matchesPlayed(12)
                .lastMatch(uuid)
                .build();

        final Model model = EasyMock.createMock(Model.class);
        expect(model.getPlayerStats(PLAYER.name)).andReturn(Optional.of(playerStats));
        replay(model);

        final GetPlayerStatsHandler handler = new GetPlayerStatsHandler(model);

        final String expectedOutput =
                "{\n" +
                        "  \"matchesWon\" : 5,\n" +
                        "  \"matchesLost\" : 7,\n" +
                        "  \"gamesPlayed\" : 12,\n" +
                        "  \"lastMatch\" : \"71828403-28b8-41e0-9c01-b0a93518bf44\"\n" +
                        "}";

        ImmutableMap<String, String> urlParams = ImmutableMap.of(":name", PLAYER.name);
        assertThat(handler.process(new EmptyPayload(), urlParams)).isEqualTo(new Answer(200, expectedOutput));

        verify(model);
    }

    @Test
    public void noGamesPlayed() throws Exception {

        final Model model = EasyMock.createMock(Model.class);
        expect(model.getPlayerStats(PLAYER.name)).andReturn(Optional.empty());
        replay(model);

        final GetPlayerStatsHandler handler = new GetPlayerStatsHandler(model);

        final String expectedOutput = "Player not found: @matthew";

        ImmutableMap<String, String> urlParams = ImmutableMap.of(":name", PLAYER.name);
        assertThat(handler.process(new EmptyPayload(), urlParams)).isEqualTo(new Answer(404, expectedOutput));

    }


}