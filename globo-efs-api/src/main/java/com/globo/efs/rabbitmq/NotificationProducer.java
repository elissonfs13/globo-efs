package com.globo.efs.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.globo.efs.entities.Notification;

@Component
public class NotificationProducer {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);
	
	@Value("${topic.exchange.name}")
    private String topicExchangeName;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

    public void send(final Notification notification) {
        logger.info("globo-efs-rabbitmq: NOTIFICATION send: " + notification.toString());
        rabbitTemplate.convertAndSend(topicExchangeName, "notifications.new", notification);
    }

}
