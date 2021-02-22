package com.globo.efs.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.globo.efs.entities.EventHistory;
import com.globo.efs.entities.Notification;
import com.globo.efs.entities.NotificationType;
import com.globo.efs.entities.Subscription;


public class NotificationIntegrationTest extends APIIntegrationTest {
	
	private static final String ATIVA = "ATIVA";
	private static final String CANCELADA = "CANCELADA";
	private static final String SUBSCRIPTION_1 = "AAA001";
	private static final String SUBSCRIPTION_2 = "AAA002";
	private static final String SUBSCRIPTION_3 = "AAA003";
	private static final String URL_BASE_NOTIFICATION = "/notification";
	private static final String URL_BASE_SUBSCRIPTION = "/subscription";
	private static final String URL_BASE_EVENT_HISTORY = "/event-history";
	
	@Test
	public void sendNotificationsTest() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE_SUBSCRIPTION)).andExpect(status().isNotFound());
		mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE_EVENT_HISTORY)).andExpect(status().isNotFound());
		mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE_SUBSCRIPTION + "/" + SUBSCRIPTION_1)).andExpect(status().isNotFound());
		mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE_EVENT_HISTORY + "/" + SUBSCRIPTION_1)).andExpect(status().isNotFound());
		
		sendAndVerifyPostNotification(getNotification(NotificationType.SUBSCRIPTION_PURCHASED, SUBSCRIPTION_1));
		
		Thread.sleep(1000L);
		sendAndVerifyGetAllSubscriptions(1);
		sendAndVerifyGetAllEventHistories(1);
		
		sendAndVerifyPostNotification(getNotification(NotificationType.SUBSCRIPTION_PURCHASED, SUBSCRIPTION_2));
		sendAndVerifyPostNotification(getNotification(NotificationType.SUBSCRIPTION_PURCHASED, SUBSCRIPTION_3));
		sendAndVerifyPostNotification(getNotification(NotificationType.SUBSCRIPTION_CANCELED, SUBSCRIPTION_2));
		sendAndVerifyPostNotification(getNotification(NotificationType.SUBSCRIPTION_CANCELED, SUBSCRIPTION_3));
		sendAndVerifyPostNotification(getNotification(NotificationType.SUBSCRIPTION_RESTARTED, SUBSCRIPTION_3));
		
		Thread.sleep(1000L);
		sendAndVerifyGetAllSubscriptions(3);
		sendAndVerifyGetAllEventHistories(6);
		
		sendAndVerifyGetEventHistoriesBySubscriptionId(SUBSCRIPTION_1, 1);
		sendAndVerifyGetEventHistoriesBySubscriptionId(SUBSCRIPTION_2, 2);
		sendAndVerifyGetEventHistoriesBySubscriptionId(SUBSCRIPTION_3, 3);
		sendAndVerifyStatusGetSubscriptionsById(SUBSCRIPTION_1, ATIVA);
		sendAndVerifyStatusGetSubscriptionsById(SUBSCRIPTION_2, CANCELADA);
		sendAndVerifyStatusGetSubscriptionsById(SUBSCRIPTION_3, ATIVA);
		
	}
	
	private void sendAndVerifyPostNotification(Notification notification) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
	        	.post(URL_BASE_NOTIFICATION)
	        	.content(mapper.writeValueAsString(notification))
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk());
	}
	
	private void sendAndVerifyGetAllSubscriptions(int size) throws Exception {
		final String contentAsString = performMockMvcGet(URL_BASE_SUBSCRIPTION);
		List<Object> subscriptionsReturned = Arrays.asList(mapper.readValue(contentAsString, Subscription[].class));
		assertEquals(size, subscriptionsReturned.size());
	}
	
	private void sendAndVerifyStatusGetSubscriptionsById(String subscriptionId, String status) throws Exception {
		final String contentAsString = performMockMvcGet(URL_BASE_SUBSCRIPTION + "/" + subscriptionId);
		Subscription subscriptionReturned = mapper.readValue(contentAsString, Subscription.class);
		assertEquals(status, subscriptionReturned.getStatus().getName());
		assertEquals(subscriptionId, subscriptionReturned.getId());
		assertNotNull(subscriptionReturned.getCreatedAt());
		assertNotNull(subscriptionReturned.getUpdatedAt());
	}
	
	private void sendAndVerifyGetAllEventHistories(int size) throws Exception {
		final String contentAsString = performMockMvcGet(URL_BASE_EVENT_HISTORY);
		List<Object> historiesReturned = Arrays.asList(mapper.readValue(contentAsString, EventHistory[].class));
		assertEquals(size, historiesReturned.size());
	}
	
	private void sendAndVerifyGetEventHistoriesBySubscriptionId(String subscriptionId, int size) throws Exception {
		final String contentAsString = performMockMvcGet(URL_BASE_EVENT_HISTORY + "/" + subscriptionId);
		List<Object> historiesReturned = Arrays.asList(mapper.readValue(contentAsString, EventHistory[].class));
		assertEquals(size, historiesReturned.size());
	}
	
	private Notification getNotification(NotificationType type, String identifier) {
		Notification notification = new Notification();
		notification.setNotificationType(type);
		notification.setSubscriptionId(identifier);
		return notification;
	}

}
