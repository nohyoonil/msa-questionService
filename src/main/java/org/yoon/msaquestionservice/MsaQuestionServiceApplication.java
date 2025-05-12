package org.yoon.msaquestionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class MsaQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsaQuestionServiceApplication.class, args);
    }

}
