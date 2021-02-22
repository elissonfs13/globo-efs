package com.globo.efs.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Subscription")
@Getter
@AllArgsConstructor @NoArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class Subscription {
	
	@Id
	@Column(name = "id")
	private String id;
	
	@JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Status status;
	
	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	public void atualizaStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Subscription [id=" + id + ", status=" + status.getName() + "]";
	}

}
