package com.globo.efs.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.globo.efs.entities.EventHistory;
import com.globo.efs.entities.Notification;
import com.globo.efs.entities.NotificationType;
import com.globo.efs.entities.Status;
import com.globo.efs.entities.Subscription;
import com.globo.efs.repositories.EventHistoryRepository;
import com.globo.efs.repositories.StatusRepository;
import com.globo.efs.repositories.SubscriptionRepository;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {
	
	private static final String ATIVA = "ATIVA";
	private static final String CANCELADA = "CANCELADA";
	private static final String GET_SUBSCRIPTION = "getSubscription";
	private static final String BUILD_EVENT_HISTORY = "buildEventHistory";
	private static final String SUBSCRIPTION_ID = "AAA001";
	private static final Integer STATUS_ATIVO = 1;
	private static final Integer STATUS_CANCELADO = 2;
	
	@InjectMocks
	private NotificationService notificationService;
	
	@Mock
	private SubscriptionRepository subscriptionRepository;
	
	@Mock
	private StatusRepository statusRepository;
	
	@Mock
	private EventHistoryRepository eventHistoryRepository;
	
	@Test
	public void getSubscriptionCreatedWithNotificationTypePurchasedTest() throws NoSuchMethodException, SecurityException, 
		IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Notification notification = getNotification(NotificationType.SUBSCRIPTION_PURCHASED);
		Subscription subscriptionResult = invokeGetSubscriptionMethod(notification);
		
		assertNotNull(subscriptionResult);
		assertEquals(ATIVA, subscriptionResult.getStatus().getName());
		assertEquals(SUBSCRIPTION_ID, subscriptionResult.getId());
		verify(statusRepository, times(1)).findById(STATUS_ATIVO);
		verify(statusRepository, times(0)).findById(STATUS_CANCELADO);
	}
	
	@Test
	public void getSubscriptionCreatedWithNotificationTypeCanceledTest() throws NoSuchMethodException, SecurityException, 
		IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Notification notification = getNotification(NotificationType.SUBSCRIPTION_CANCELED);
		Subscription subscriptionResult = invokeGetSubscriptionMethod(notification);
		
		assertNotNull(subscriptionResult);
		assertEquals(CANCELADA, subscriptionResult.getStatus().getName());
		assertEquals(SUBSCRIPTION_ID, subscriptionResult.getId());
		verify(statusRepository, times(0)).findById(STATUS_ATIVO);
		verify(statusRepository, times(1)).findById(STATUS_CANCELADO);
	}
	
	@Test
	public void getSubscriptionCreatedWithNotificationTypeRestartedTest() throws NoSuchMethodException, SecurityException, 
		IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Notification notification = getNotification(NotificationType.SUBSCRIPTION_RESTARTED);
		Subscription subscriptionResult = invokeGetSubscriptionMethod(notification);
		
		assertNotNull(subscriptionResult);
		assertEquals(ATIVA, subscriptionResult.getStatus().getName());
		assertEquals(SUBSCRIPTION_ID, subscriptionResult.getId());
		verify(statusRepository, times(1)).findById(STATUS_ATIVO);
		verify(statusRepository, times(0)).findById(STATUS_CANCELADO);
	}
	
	@Test
	public void getSubscriptionUpdatedWithNotificationTypeCanceledTest() throws NoSuchMethodException, SecurityException, 
		IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		final Optional<Subscription> subscriptionFound = Optional.of(getSubscription(new Status(STATUS_ATIVO, ATIVA)));	
		when(subscriptionRepository.findById(SUBSCRIPTION_ID)).thenReturn(subscriptionFound);
		
		Notification notification = getNotification(NotificationType.SUBSCRIPTION_CANCELED);
		Subscription subscriptionResult = invokeGetSubscriptionMethod(notification);
		
		assertNotNull(subscriptionResult);
		assertEquals(CANCELADA, subscriptionResult.getStatus().getName());
		assertEquals(SUBSCRIPTION_ID, subscriptionResult.getId());
		verify(statusRepository, times(0)).findById(STATUS_ATIVO);
		verify(statusRepository, times(1)).findById(STATUS_CANCELADO);
	}
	
	@Test
	public void getSubscriptionUpdatedWithNotificationTypeRestartedTest() throws NoSuchMethodException, SecurityException, 
		IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		final Optional<Subscription> subscriptionFound = Optional.of(getSubscription(new Status(STATUS_CANCELADO, CANCELADA)));	
		when(subscriptionRepository.findById(SUBSCRIPTION_ID)).thenReturn(subscriptionFound);
		
		Notification notification = getNotification(NotificationType.SUBSCRIPTION_RESTARTED);
		Subscription subscriptionResult = invokeGetSubscriptionMethod(notification);
		
		assertNotNull(subscriptionResult);
		assertEquals(ATIVA, subscriptionResult.getStatus().getName());
		assertEquals(SUBSCRIPTION_ID, subscriptionResult.getId());
		verify(statusRepository, times(1)).findById(STATUS_ATIVO);
		verify(statusRepository, times(0)).findById(STATUS_CANCELADO);
	}
	
	@Test
	public void buildEventHistoryTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException {
		
		Notification notification = getNotification(NotificationType.SUBSCRIPTION_PURCHASED);
		Subscription subscription = getSubscription(new Status(STATUS_ATIVO, ATIVA));
		
		EventHistory eventHistoryResult = invokeBuildEventHistoryMethod(notification, subscription);
		assertNotNull(eventHistoryResult);
		assertEquals(notification.getNotificationType(), eventHistoryResult.getType());
		assertEquals(subscription, eventHistoryResult.getSubscription());
	}

	private Notification getNotification(NotificationType type) {
		Notification notification = new Notification();
		notification.setNotificationType(type);
		notification.setSubscriptionId(SUBSCRIPTION_ID);
		return notification;
	}
	
	private Subscription getSubscription(Status status) {
		return new Subscription(SUBSCRIPTION_ID, status, null, null);
	}

	private Subscription invokeGetSubscriptionMethod(Notification notification)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
		final Optional<Status> statusCancelado = Optional.of(new Status(STATUS_CANCELADO, CANCELADA));	
		when(statusRepository.findById(STATUS_CANCELADO)).thenReturn(statusCancelado);
		
		final Optional<Status> statusAtivo = Optional.of(new Status(STATUS_ATIVO, ATIVA));	
		when(statusRepository.findById(STATUS_ATIVO)).thenReturn(statusAtivo);
		
		Method getSubscriptionMethod = NotificationService.class.getDeclaredMethod(GET_SUBSCRIPTION, Notification.class);
		getSubscriptionMethod.setAccessible(true);
		
		Subscription subscriptionResult = (Subscription)getSubscriptionMethod.invoke(notificationService, notification);
		return subscriptionResult;
	}

	private EventHistory invokeBuildEventHistoryMethod(Notification notification, Subscription subscription)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method buildEventHistoryMethod = NotificationService.class.getDeclaredMethod(BUILD_EVENT_HISTORY, Notification.class, Subscription.class);
		buildEventHistoryMethod.setAccessible(true);
		
		EventHistory eventHistoryResult = (EventHistory)buildEventHistoryMethod.invoke(notificationService, notification, subscription);
		return eventHistoryResult;
	}

}
