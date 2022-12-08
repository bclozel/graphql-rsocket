package com.example.graphqlrsocket;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("websocket")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GraphQlWebSocketTests {

	@LocalServerPort
	private int localPort;

	@Test
	void getProjectUsingWebSocket() {
		WebSocketClient webClient = new ReactorNettyWebSocketClient();
		WebSocketGraphQlClient graphQlClient = WebSocketGraphQlClient
				.builder("http://localhost:" + localPort + "/graphql", webClient).build();
		Mono<String> projectName = graphQlClient.documentName("project").retrieve("project.name").toEntity(String.class);
		StepVerifier.create(projectName).expectNext("Spring Boot").expectComplete().verify();
	}
}
