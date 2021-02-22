package com.globo.efs.rabbitmq;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.globo.efs.entities.Notification;
import com.globo.efs.services.NotificationService;

@Component
public class NotificationConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);
	
	@Autowired
	private NotificationService notificationService;
	
	@RabbitListener(queues = {"${queue.notification.name}"}, containerFactory = "listenerContainerFactory")
    public void receive(final Notification notification) {
		logger.info("globo-efs-rabbitmq: NOTIFICATION received: " + notification.toString());
		notificationService.executeNotification(notification);
    }

}
