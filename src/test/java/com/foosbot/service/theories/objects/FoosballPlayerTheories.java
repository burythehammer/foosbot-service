package com.foosbot.service.theories;

import com.foosbot.service.model.players.FoosballPlayer;
import org.junit.Test;

import static org.quicktheories.quicktheories.QuickTheory.qt;
import static org.quicktheories.quicktheories.generators.SourceDSL.strings;

public class SomeTests {

    @Test
    public void canCreateAnyFoosballPlayer() {
        qt().forAll(strings().allPossible().ofLengthBetween(1, 1000))
                .check(s -> FoosballPlayer.of(s).isValid());
    }





}