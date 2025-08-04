package org.bloomberg.fx_deals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FxDealsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FxDealsApplication.class, args);
    }

}
