package com.globo.efs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globo.efs.entities.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, String>{

}
