package com.intraron.api.config;


import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@Import({CorsConfig.class})
class ConfigTest {



}