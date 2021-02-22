package com.globo.efs.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
	
	@JsonProperty("notification_type")
	private NotificationType notificationType;
	
	@JsonProperty("subscription")
	private String subscriptionId;

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	@Override
	public String toString() {
		return "Notification [notificationType=" + notificationType + ", subscriptionId=" + subscriptionId + "]";
	}
	
}