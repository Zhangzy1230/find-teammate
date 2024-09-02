package com.zzy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Flux;
@ConfigurationProperties
public interface RouteDefinitionLocator {
	Flux<RouteDefinition> getRouteDefinitions();
}