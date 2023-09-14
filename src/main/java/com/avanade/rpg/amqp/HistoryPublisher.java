package com.avanade.rpg.amqp;

import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.payloads.requests.HistoryBattleRequest;
import com.avanade.rpg.payloads.requests.HistoryTurnRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.avanade.rpg.constants.HistoryRoutingKeys.HISTORY_BATTLE_RK;
import static com.avanade.rpg.constants.HistoryRoutingKeys.HISTORY_TURN_RK;

@Component
public class HistoryPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    public HistoryPublisher(RabbitTemplate rabbitTemplate, ObjectMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public void processHistoryBattle(Battle battle) {
        HistoryBattleRequest request = new HistoryBattleRequest(battle);
        publishMessage(request, HISTORY_BATTLE_RK);
    }

    public void processHistoryTurn(Turn turn) {
        HistoryTurnRequest request = new HistoryTurnRequest(turn);
        publishMessage(request, HISTORY_TURN_RK);
    }

    private void publishMessage(Object request, String routingKey) {
        try {
            String json = serializeToJson(request);
            rabbitTemplate.convertAndSend(routingKey, json);
            LOGGER.info("Successfully published message: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("JSON processing error: {}", e.getMessage());
        } catch (AmqpException e) {
            LOGGER.error("RabbitMQ publishing error: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred: {}", e.getMessage());
        }
    }

    private String serializeToJson(Object request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }
}
