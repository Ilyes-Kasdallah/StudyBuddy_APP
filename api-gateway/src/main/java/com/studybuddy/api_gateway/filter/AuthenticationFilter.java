package com.studybuddy.api_gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final WebClient webClient;

    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();

        // Allow login and register endpoints to bypass session validation
        if (requestPath.equals("/auth/login") || requestPath.equals("/auth/register") || requestPath.equals("/auth/validate")) {
            return chain.filter(exchange);
        }

        // Check for Session-Id in the headers
        String sessionId = exchange.getRequest().getHeaders().getFirst("session-id");

        if (sessionId == null || sessionId.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        return webClient.post()
                .uri(authServiceUrl + "/validate")
                .bodyValue(Collections.singletonMap("token", sessionId))
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("Session validation failed"))
                )
                // Change this part to handle the response properly
                .toBodilessEntity()  // Use toBodilessEntity() instead of bodyToMono(Void.class)
                .then(chain.filter(exchange))  // Use then() instead of flatMap
                .onErrorResume(error -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });

    }

    @Override
    public int getOrder() {
        return -1; // High priority for authentication
    }
}

