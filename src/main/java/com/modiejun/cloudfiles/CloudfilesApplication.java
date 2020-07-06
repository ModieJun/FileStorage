package com.modiejun.cloudfiles;

import com.modiejun.cloudfiles.FileUpload.StorageProperties;
import com.modiejun.cloudfiles.FileUpload.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class})
public class CloudfilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudfilesApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return(
                args -> {
                    storageService.deleteAll();
                    storageService.init();
                });
    }
}
