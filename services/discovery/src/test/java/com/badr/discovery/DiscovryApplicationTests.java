package com.badr.discovery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// --- CORRECTION : Ajouter cette annotation ---
@ActiveProfiles("test")
@SpringBootTest
class DiscoveryApplicationTests {

	@Test
	void contextLoads() {
	}

}
