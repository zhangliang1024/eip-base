package com.eip.sample.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsWebApplication.class, args);
        System.getProperties().setProperty("mail.mime.splitlongparameters", "false");
    }

}
