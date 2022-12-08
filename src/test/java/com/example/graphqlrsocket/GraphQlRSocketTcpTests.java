package com.example.graphqlrsocket;

import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.client.RSocketGraphQlClient;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("rsockettcp")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GraphQlRSocketTcpTests {

	@Test
	void getProjectUsingRSocket() {
		TcpClientTransport transport = TcpClientTransport.create(9191);
		RSocketGraphQlClient graphQlClient = RSocketGraphQlClient.builder().clientTransport(transport).build();
		Mono<String> projectName = graphQlClient.documentName("project").retrieve("project.name").toEntity(String.class);
		StepVerifier.create(projectName).expectNext("Spring Boot").expectComplete().verify();
	}
}
