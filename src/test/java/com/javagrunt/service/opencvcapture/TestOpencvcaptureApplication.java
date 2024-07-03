package com.javagrunt.service.opencvcapture;

import org.springframework.boot.SpringApplication;

public class TestOpencvcaptureApplication {

	public static void main(String[] args) {
		SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
