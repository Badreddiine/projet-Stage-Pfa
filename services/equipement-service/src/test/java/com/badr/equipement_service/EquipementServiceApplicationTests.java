package com.badr.equipement_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.cloud.config.enabled=false",
		"spring.cloud.config.import-check.enabled=false",
		"eureka.client.enabled=false",
		"eureka.client.register-with-eureka=false",
		"eureka.client.fetch-registry=false"
})
// Ajout de cette annotation pour activer le profil "test"
// et charger src/test/resources/application-test.properties
@ActiveProfiles("test")
class EquipementServiceApplicationTests {

	@Test
	void contextLoads() {
		// Ce test vérifiera si le contexte de l'application se charge correctement
		// avec la configuration de test (base de données H2).
	}
}
