package com.badr.gateway;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
@SpringBootTest
@ActiveProfiles("test")
class GatewayApplicationTests {

	@Test
	//@Disabled("Temporarily disabled while fixing context loading")
	void contextLoads() {
	}
}