package com.example.graphqlrsocket;

import java.net.URI;

import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.client.RSocketGraphQlClient;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("rsocketws")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GraphQlRSocketWsTests {

	@LocalServerPort
	private int localPort;

	@Test
	void getProjectUsingRSocket() {
		URI uri = URI.create("http://localhost:" + localPort + "/graphql");
		WebsocketClientTransport transport = WebsocketClientTransport.create(uri);
		RSocketGraphQlClient graphQlClient = RSocketGraphQlClient.builder().clientTransport(transport).build();
		Mono<String> projectName = graphQlClient.documentName("project").retrieve("project.name").toEntity(String.class);
		StepVerifier.create(projectName).expectNext("Spring Boot").expectComplete().verify();
	}
}
