package com.globo.efs.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globo.efs.entities.EventHistory;
import com.globo.efs.repositories.EventHistoryRepository;

@RestController
@RequestMapping(value = "/event-history")
public class EventHistoryResource {
	
	@Autowired
	private EventHistoryRepository eventHistoryRepository;
	
	@GetMapping
	public ResponseEntity<List<EventHistory>> getAllHistories() {
		List<EventHistory> histories = eventHistoryRepository.findAll();
		if (histories.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(histories);
	}
	
	@GetMapping("/{subscriptionId}")
	public ResponseEntity<List<EventHistory>> getAllHistoriesBySubscriptionId(@PathVariable final String subscriptionId) {
		List<EventHistory> histories = eventHistoryRepository.findBySubscriptionId(subscriptionId);
		if (histories.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(histories);
	}

}
