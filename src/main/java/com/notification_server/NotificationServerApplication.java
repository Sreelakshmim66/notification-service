package com.notification_server;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
public class NotificationServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(NotificationServerApplication.class, args);
	}
}

