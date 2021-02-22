package com.globo.efs.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.globo.efs.entities.EventHistory;
import com.globo.efs.entities.Notification;
import com.globo.efs.entities.NotificationType;
import com.globo.efs.entities.Status;
import com.globo.efs.entities.Subscription;
import com.globo.efs.repositories.EventHistoryRepository;
import com.globo.efs.repositories.StatusRepository;
import com.globo.efs.repositories.SubscriptionRepository;

@Service
public class NotificationService {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	private static final Integer STATUS_ATIVO = 1;
	private static final Integer STATUS_CANCELADO = 2;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private EventHistoryRepository eventHistoryRepository;

	public void executeNotification(final Notification notification) {
		Subscription subscription = subscriptionRepository.save(getSubscription(notification));
		eventHistoryRepository.save(buildEventHistory(notification, subscription));
	}
	
	private Subscription getSubscription(final Notification notification) {
		Optional<Subscription> optSubscription = subscriptionRepository.findById(notification.getSubscriptionId());
		if (optSubscription.isPresent()) {
			Subscription subscription = optSubscription.get();
			subscription.atualizaStatus(getSubscriptionStatus(notification.getNotificationType()));
			logger.info("globo-efs-api: SUBSCRIPTION updated: " + subscription.toString());
			return subscription;
		}
		return buildSubscription(notification);
	}
	
	private Subscription buildSubscription(final Notification notification) {
		Subscription subscription = Subscription.builder()
				.id(notification.getSubscriptionId())
				.status(getSubscriptionStatus(notification.getNotificationType()))
				.build();
		logger.info("globo-efs-api: SUBSCRIPTION created: " + subscription.toString());
		return subscription;
	}
	
	private EventHistory buildEventHistory(final Notification notification, final Subscription subscription) {
		EventHistory history = EventHistory.builder()
				.type(notification.getNotificationType())
				.subscription(subscription)
				.build();
		logger.info("globo-efs-api: EVENT HISTORY created: " + history.toString());
		return history;
	}

	private Status getSubscriptionStatus(final NotificationType type) {
		if (NotificationType.SUBSCRIPTION_CANCELED.equals(type)) {
			return statusRepository.findById(STATUS_CANCELADO).get();
		} else {
			return statusRepository.findById(STATUS_ATIVO).get();
		}
		
	}	

}
