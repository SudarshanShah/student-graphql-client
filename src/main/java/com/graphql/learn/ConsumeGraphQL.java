package com.graphql.learn;

import java.util.HashMap;
import java.util.Map;

import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class ConsumeGraphQL {

	// create WebClient object
	private WebClient webClient = WebClient.create();
	
	// get the HttpGraphQlClient object
	private HttpGraphQlClient httpGraphQlClient = HttpGraphQlClient.builder(webClient).build();
	
	// call the API as GraphQL client
	@GetMapping("/student/{sid}")
	public Student getStudentById(@PathVariable Integer sid) {
		Map<String, Object> map = new HashMap<>();
		map.computeIfAbsent("sid", value -> sid);
		
		var document = """
				query($sid : Int){
					getStudent(sid : $sid){
						sid, name, dept, favSport, classTeacher
					}
				}
				""";
		
		return httpGraphQlClient.mutate()
				.url("http://localhost:8091/graphql")
				.build()
				.document(document)
				.variables(map)
				.retrieve("getStudent")
				.toEntity(Student.class)
				.block();
	}
}
