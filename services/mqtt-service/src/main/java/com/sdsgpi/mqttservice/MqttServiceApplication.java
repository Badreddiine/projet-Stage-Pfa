package com.sdsgpi.mqttservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MqttServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqttServiceApplication.class, args);
    }
}


