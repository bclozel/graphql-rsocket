package com.example.graphqlrsocket;

import java.net.URI;

import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.client.RSocketGraphQlClient;
import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class GraphQlRandomPortTests {

	private static final String DOCUMENT = """
			{
			  project(slug:"spring-boot") {
				name
			  }
			}
			""";

	@LocalServerPort
	private int localPort;

	@Test
	void getProjectUsingWebSocket() {
		WebSocketClient webClient = new ReactorNettyWebSocketClient();
		WebSocketGraphQlClient graphQlClient = WebSocketGraphQlClient
				.builder("http://localhost:" + localPort + "/graphql", webClient).build();
		Mono<String> projectName = graphQlClient.document(DOCUMENT).retrieve("project.name").toEntity(String.class);
		StepVerifier.create(projectName).expectNext("Spring Boot").expectComplete().verify();
	}

	@Test
	void getProjectUsingRSocket() {
		URI uri = URI.create("http://localhost:" + localPort + "/graphql");
		WebsocketClientTransport transport = WebsocketClientTransport.create(uri);
		RSocketGraphQlClient graphQlClient = RSocketGraphQlClient.builder().clientTransport(transport).build();
		Mono<String> projectName = graphQlClient.document(DOCUMENT).retrieve("project.name").toEntity(String.class);
		StepVerifier.create(projectName).expectNext("Spring Boot").expectComplete().verify();
	}

}
