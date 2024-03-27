package com.nilknow.yifanerp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YifanErp2Application {

    public static void main(String[] args) {
        SpringApplication.run(YifanErp2Application.class, args);
    }

}
