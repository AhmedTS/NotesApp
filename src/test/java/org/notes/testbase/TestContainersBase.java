package org.notes.testbase;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.notes.model.Note;
import org.notes.model.Tags;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestContainersBase {

    protected static MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer("mongo");
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.database", () -> "testdb");
        registry.add("spring.data.mongodb.port",() -> String.valueOf(mongoDBContainer.getExposedPorts().get(0)));
        System.out.println("NEW CONFIGURATION DONE\n");
    }

    protected List<Note> getExampleNotes(){
        Note note1 = new Note("Note 1", LocalDate.of(2024, 5, 5),"note is just a note",List.of());
        Note note2 = new Note("Note 2", LocalDate.of(2024, 6, 28), "Reminder: buy some eggs", List.of(Tags.PERSONAL));
        Note note3 = new Note("Note 3", LocalDate.of(2023, 9, 19), "Do not forget to send an email to danny tomorrow to agree on a strategy", List.of(Tags.BUSINESS,Tags.IMPORTANT));
        Note note4 = new Note("Note 4", LocalDate.of(2023, 4, 18), "Flight is on Wednesday", List.of(Tags.IMPORTANT));
        Note note5 = new Note("Note 5", LocalDate.of(2024, 10, 10), "Test note with all tags", List.of(Tags.IMPORTANT, Tags.BUSINESS, Tags.PERSONAL));
        return List.of(note1, note2, note3, note4, note5);
    }

//    @Override
//    public void beforeAll(ExtensionContext context) throws Exception {
//        //mongoDBContainer = ;
//        mongoDBContainer.start();
//        setProperties(mongoDBContainer);
//    }
}
