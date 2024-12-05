package org.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
//@EnableMongoRepositories(basePackages = "org.notes.repository")
//@ComponentScan(basePackages = "org.notes.*")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class NotesAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesAppApplication.class, args);
    }

}
