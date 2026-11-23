package com.example.shop.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

@Service
public class OrderEventProducer {
    public static final String TOPIC = "orders.created";
    private static final Logger log =
            LoggerFactory.getLogger(OrderEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate,
                              JsonMapper jsonMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
    }

    public void send(OrderCreatedEvent event) {
        String json = jsonMapper.writeValueAsString(event);
        String key = String.valueOf(event.orderId());

        kafkaTemplate.send(TOPIC, key, json)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Не удалось отправить событие заказа {}",
                                event.orderId(), error);
                    } else {
                        log.info("Событие заказа {} отправлено: "
                                        + "раздел={}, смещение={}",
                                event.orderId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}