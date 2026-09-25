package com.guesscountry.game.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class GameRulesTest {
    @Test
    fun comboPointsFollowRequestedProgression() {
        assertEquals(100, GameRules.pointsForCombo(1))
        assertEquals(125, GameRules.pointsForCombo(2))
        assertEquals(150, GameRules.pointsForCombo(3))
        assertEquals(175, GameRules.pointsForCombo(4))
        assertEquals(200, GameRules.pointsForCombo(5))
        assertEquals(200, GameRules.pointsForCombo(8))
    }

    @Test
    fun wrongAnswerResetsCombo() {
        assertEquals(0, GameRules.nextCombo(4, correct = false))
        assertEquals(5, GameRules.nextCombo(4, correct = true))
    }

    @Test
    fun accuracyIsSafeForEmptyGames() {
        assertEquals(0, GameRules.accuracy(0, 0))
        assertEquals(50, GameRules.accuracy(5, 10))
        assertEquals(100, GameRules.accuracy(10, 10))
    }
}
