package com.ocp.ocp_finalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OcpFinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcpFinalProjectApplication.class, args);
    }

}
