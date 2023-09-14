package com.avanade.rpg.repositories;

import com.avanade.rpg.entities.QTurn;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.payloads.requests.TurnFilterRequest;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TurnFilterRepository extends QuerydslRepositorySupport {

    @PersistenceContext
    private EntityManager entityManager;

    public TurnFilterRepository() {
        super(Turn.class);
    }

    public List<Turn> findByFilter(TurnFilterRequest filter) {
        QTurn turn = QTurn.turn;
        List<Predicate> predicates = new ArrayList<>();

        if (filter.status() != null) {
            predicates.add(turn.status.eq(filter.status()));
        }
        if (filter.battleId() != null) {
            predicates.add(turn.battle.id.eq(filter.battleId()));
        }

        return new JPAQueryFactory(entityManager)
                .selectFrom(turn)
                .where(predicates.toArray(new Predicate[0]))
                .fetch();
    }
}
