package com.globo.efs.resources;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globo.efs.entities.Subscription;
import com.globo.efs.repositories.SubscriptionRepository;

@RestController
@RequestMapping(value = "/subscription")
public class SubscriptionResource {
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@GetMapping
	public ResponseEntity<List<Subscription>> getAllSubscriptions() {
		List<Subscription> subscriptions = subscriptionRepository.findAll();
		if (subscriptions.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(subscriptions);
	}
	
	@GetMapping("/{subscriptionId}")
	public ResponseEntity<Subscription> getSubscriptionById(@PathVariable final String subscriptionId) {
		Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
		if (optionalSubscription.isPresent()) {
			return ResponseEntity.ok().body(optionalSubscription.get());
		}
		return ResponseEntity.notFound().build();
	}

}
