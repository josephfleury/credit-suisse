package com.credit.suisse.event.processor;

import com.credit.suisse.event.processor.processor.file.EventLogProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@Profile("prod")
@SpringBootApplication
@EnableTransactionManagement
public class EventProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventProcessorApplication.class, args);
	}

	@Autowired
	private EventLogProcessor eventLogProcessor;

	@Bean
	CommandLineRunner runner() {
		return args -> {
			eventLogProcessor.run();
		};
	}

}

