package com.avanade.rpg.factories;

import com.avanade.rpg.enums.ActionType;
import org.springframework.stereotype.Component;

@Component
public class ActionFactory {
    public Action createAction(ActionType actionType) {
        return switch (actionType) {
            case ATTACK -> new AttackAction();
            case DEFEND -> new DefendAction();
        };
    }
}