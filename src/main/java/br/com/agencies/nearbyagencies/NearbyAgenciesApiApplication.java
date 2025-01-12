package br.com.agencies.nearbyagencies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NearbyAgenciesApiApplication {

	private static final Logger logger = LoggerFactory.getLogger(NearbyAgenciesApiApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(NearbyAgenciesApiApplication.class, args);
		logger.info("Nearby Agencies API started successfully");
	}
}
