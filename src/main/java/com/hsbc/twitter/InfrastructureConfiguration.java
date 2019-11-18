package com.hsbc.twitter;

import com.hsbc.twitter.infrastructure.memory.EnableInMemoryProviders;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@EnableWebFlux
@ComponentScan(basePackages = {"com.hsbc.twitter.adapter", "com.hsbc.twitter.domain"})
@EnableScheduling
@EnableInMemoryProviders
@Configuration
public class InfrastructureConfiguration implements WebFluxConfigurer {

}
