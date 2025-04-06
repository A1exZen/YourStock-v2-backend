package org.example.yourstockv2backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class YourStockV2BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YourStockV2BackendApplication.class, args);
    }

}
