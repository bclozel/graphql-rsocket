package com.example.graphqlrsocket;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@SpringBootTest
@AutoConfigureGraphQlTester
class GraphQlTesterTests {

	private static final String DOCUMENT = """
			{
			  project(slug:"spring-boot") {
				name
			  }
			}
			""";

	@Autowired
	private GraphQlTester graphQlTester;

	@Test
	void getProject() {
		graphQlTester.document(DOCUMENT).execute().path("project.name").entity(String.class).isEqualTo("Spring Boot");
	}

}