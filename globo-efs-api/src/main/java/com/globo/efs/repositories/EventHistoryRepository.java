package com.globo.efs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globo.efs.entities.EventHistory;

public interface EventHistoryRepository extends JpaRepository<EventHistory, Integer> {
	
	List<EventHistory> findBySubscriptionId(String id);

}
