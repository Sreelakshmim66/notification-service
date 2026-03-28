package com.notification_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "eureka.client.enabled=false")
class NotificationServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
