package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = { CassandraAutoConfiguration.class, CassandraDataAutoConfiguration.class })
@SpringBootApplication
public class SpringCassandraTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCassandraTestingApplication.class, args);
		System.out.println("! Application Started !");
	}
}
