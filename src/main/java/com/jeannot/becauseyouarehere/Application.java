package com.jeannot.becauseyouarehere;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jeannot.becauseyouarehere.dao.EmbeddedNeo4j;

@SpringBootApplication
public class Application {
	
	public static EmbeddedNeo4j neo4j = new EmbeddedNeo4j();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        try {
        	neo4j.createDb();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }
    
}
