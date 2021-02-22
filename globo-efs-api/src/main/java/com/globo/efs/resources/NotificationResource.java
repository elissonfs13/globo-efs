package com.globo.efs.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globo.efs.entities.Notification;
import com.globo.efs.rabbitmq.NotificationProducer;

@RestController
@RequestMapping(value = "/notification")
public class NotificationResource {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationResource.class);
	
	@Autowired
	private NotificationProducer notificationProducer;
	
	@PostMapping
	public ResponseEntity<Void> sendNotification(@RequestBody Notification notification) {
		logger.info("globo-efs-api: NOTIFICATION received: " + notification.toString());
		notificationProducer.send(notification);
		return ResponseEntity.ok().build();
	}

}
