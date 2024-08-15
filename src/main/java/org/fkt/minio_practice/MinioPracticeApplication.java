package org.fkt.minio_practice;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinioPracticeApplication {
	private static final Logger log = LoggerFactory.getLogger(MinioPracticeApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MinioPracticeApplication.class, args);
		log.info("Web Document Reference http://127.0.0.1:7788/swagger-ui/index.html");
		log.info("Server is Running on http://127.0.0.1:7788");
	}

}
