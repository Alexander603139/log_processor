package io.github.bugrov.log_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogProcessorApplication.class, args);
	}

}
