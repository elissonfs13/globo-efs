package com.globo.efs.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor @Builder
@Table(name = "Status", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name"})})
public class Status {
	
	@Id
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String name;

}
