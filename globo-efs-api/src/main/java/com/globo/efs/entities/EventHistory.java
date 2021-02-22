package com.globo.efs.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EventHistory")
@Getter
@AllArgsConstructor @NoArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class EventHistory {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private NotificationType type;
	
	@JoinColumn(name = "subscription_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Subscription subscription;
	
	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Override
	public String toString() {
		return "EventHistory [type=" + type + ", subscription=" + subscription.getId() + "]";
	}

}
