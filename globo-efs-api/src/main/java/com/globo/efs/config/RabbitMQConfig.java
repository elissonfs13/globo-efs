package com.globo.efs.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	
	@Value("${queue.notification.name}")
    private String notificationQueue;
	
	@Value("${topic.exchange.name}")
    private String topicExchangeName;
	
	@Value("${routing.key}")
	private String rountingKey;
	
	@Bean
	public TopicExchange topicExchange() {
	    return new TopicExchange(topicExchangeName);
	}
	
	@Bean
	public Queue queue() {
	    return new Queue(notificationQueue);
	}
	
	@Bean
	public Binding binding() {
	    return BindingBuilder.bind(queue()).to(topicExchange()).with(rountingKey);
	}
	
	@Bean
	public SimpleRabbitListenerContainerFactory listenerContainerFactory(ConnectionFactory connectionFactory) {
	    final var factory = new SimpleRabbitListenerContainerFactory();
	    factory.setConnectionFactory(connectionFactory);
	    factory.setMessageConverter(messageConverter());
	    return factory;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
	    final var rabbitTemplate = new RabbitTemplate(connectionFactory);
	    rabbitTemplate.setMessageConverter(messageConverter());
	    return rabbitTemplate;
	}
	
	@Bean
	public MessageConverter messageConverter() {
	    return new Jackson2JsonMessageConverter();
	}

}
