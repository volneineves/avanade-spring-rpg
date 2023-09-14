package com.avanade.rpg.factories;

import com.avanade.rpg.enums.ActionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionFactoryTest {

    private ActionFactory actionFactory;

    @BeforeEach
    void setUp() {
        actionFactory = new ActionFactory();
    }

    @Test
    @DisplayName("Should return an instance of AttackAction when ActionType is ATTACK")
    void shouldReturnInstanceOfAttackAction() {
        Action action = actionFactory.createAction(ActionType.ATTACK);
        assertTrue(action instanceof AttackAction, "The action should be an instance of AttackAction");
    }

    @Test
    @DisplayName("Should return an instance of DefendAction when ActionType is DEFEND")
    void shouldReturnInstanceOfDefendAction() {
        Action action = actionFactory.createAction(ActionType.DEFEND);
        assertTrue(action instanceof DefendAction, "The action should be an instance of DefendAction");
    }
}
