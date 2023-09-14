package com.avanade.rpg.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.avanade.rpg.constants.HistoryRoutingKeys.HISTORY_BATTLE_RK;
import static com.avanade.rpg.constants.HistoryRoutingKeys.HISTORY_TURN_RK;

@Configuration
public class HistoryAMQPConfiguration {

    @Bean
    public Queue createBattleQueue() {
        return new Queue(HISTORY_BATTLE_RK, false);
    }

    @Bean
    public Queue createTurnQueue() {
        return new Queue(HISTORY_TURN_RK, false);
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }
}
