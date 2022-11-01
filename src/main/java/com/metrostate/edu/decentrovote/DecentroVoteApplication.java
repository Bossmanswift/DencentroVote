package com.metrostate.edu.decentrovote;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "DecentroVote API", version = "1.0.0", description = "Prototype UI For DencentroVote"))
public class DecentroVoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DecentroVoteApplication.class, args);
	}

}
