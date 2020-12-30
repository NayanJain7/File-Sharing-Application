package com.file_share.nayan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.file_share.nayan.pojo.FileStoragePojo;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({FileStoragePojo.class})
public class NayanApplication {

	public static void main(String[] args) {
		SpringApplication.run(NayanApplication.class, args);
	}

}
