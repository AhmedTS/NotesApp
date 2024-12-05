package org.notes.notesapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.notes.model.Note;
import org.notes.model.Tags;
import org.notes.model.web.WebNote;
import org.notes.repository.NotesRepository;
import org.notes.testbase.TestContainersBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class NotesAppApplicationTests extends TestContainersBase {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private NotesRepository notesRepository;

   @Autowired
   private ObjectMapper objectMapper;

   Note testNote;

   @BeforeAll
   void setUp(){
        testNote = new Note(null,
                "Test Note",
                null,
                "Test note is a note",
                List.of(Tags.PERSONAL));
   }

    @Test
    @Order(1)
    void testGetNote_NotFound() throws Exception{
        String testId = ObjectId.get().toString(); //does not exist
        ResultActions response = mockMvc.perform(get("/api/v1/notes/{noteId}", testId));

        //Verify
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.description", is("Note not found")))
                .andExpect(jsonPath("$.details[0]",is("No note matching id "+testId)));
    }

   @Test
   @Order(2)
   void testCreateNote_Valid() throws Exception{
       WebNote webTestNote = new WebNote(null,
                                        "Test Note",
                                  null,
                                        "Test note is a note",
                                              List.of(Tags.PERSONAL));

       //Action
       ResultActions response = mockMvc.perform(post("/api/v1/notes")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(webTestNote)));

       //Verify
       List<String> compareTags = webTestNote.tags().stream().map(Enum::name).toList(); //json returns Tags as string
       response.andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title", is(webTestNote.title())))
               .andExpect(jsonPath("$.text", is(webTestNote.text())))
               .andExpect(jsonPath("$.tags", is(compareTags)))
               .andExpect(jsonPath("$.id", notNullValue()))
               .andExpect(jsonPath("$.createdDate", notNullValue()));
       testNote = notesRepository.findAll().get(0); //update testNote to get ID
   }

    @Test
    @Order(3)
    void testCreateNote_NotValid() throws Exception{
        WebNote webTestNote = new WebNote(null,
                "", //Empty title
                null,
                "Test note is a note",
                List.of(Tags.PERSONAL));

        //Action
        ResultActions response = mockMvc.perform(post("/api/v1/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(webTestNote)));

        //Assert results
        String titleError = "Title must not be blank.";
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("Validation Failed")))
                .andExpect(jsonPath("$.details").value(Matchers.contains(titleError)));
    }

    @Test
    @Order(4)
    void testGetNote_Valid() throws Exception{
        ResultActions response = mockMvc.perform(get("/api/v1/notes/{noteId}", testNote.getId()));

        //Verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testNote.getTitle())));
    }

    @Test
    @Order(5)
    void testGetNote_NotValid() throws Exception{
        String testId = "testId"; //invalid object id
        ResultActions response = mockMvc.perform(get("/api/v1/notes/{noteId}", testId));

        //Verify
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("Validation Failed")))
                .andExpect(jsonPath("$.details[0]",is("Invalid ObjectId")));
    }

    @Test
    @Order(6)
    void testUpdateNote_Valid() throws Exception{
        testNote.setTitle("Updated Test Note");
        ResultActions response = mockMvc.perform(put("/api/v1/notes/{noteId}", testNote.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testNote)));

        //Verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testNote.getTitle())));
    }

    @Test
    @Order(7)
    void testUpdateNote_NotExists() throws Exception{
        String testId = ObjectId.get().toString(); //does not exist
        ResultActions response = mockMvc.perform(put("/api/v1/notes/{noteId}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testNote)));

        //Verify
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.description", is("Note not found")))
                .andExpect(jsonPath("$.details[0]",is("No note matching id "+testId)));
    }

    @Test
    @Order(8)
    void testUpdateNote_NotValid() throws Exception{
        String testId = "testId"; //invalid object id
        ResultActions response = mockMvc.perform(put("/api/v1/notes/{noteId}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testNote)));

        //Verify
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("Validation Failed")))
                .andExpect(jsonPath("$.details[0]",is("Invalid ObjectId")));
    }

    @Test
    @Order(9)
    void testGetNoteStats() throws Exception{
       Map<String, Integer> expectedResult = new LinkedHashMap<>(){
           {
                put("note", 2);
                put("test", 1);
                put("is", 1);
                put("a", 1);
           }
       };
        ResultActions response = mockMvc.perform(get("/api/v1/notes/{noteId}/stats", testNote.getId()));

        //Verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @Order(10)
    void testDeleteNote() throws Exception{
        ResultActions response = mockMvc.perform(delete("/api/v1/notes/{noteId}", testNote.getId()));

        //Verify
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(11)
    void testGetNotes_NoFilter() throws Exception{
        List<Note> testNotes = getExampleNotes();
        notesRepository.saveAll(testNotes);

        ResultActions response = mockMvc.perform(get("/api/v1/notes")
                .param("page", "1") //2nd page
                .param("size", "2") //page size is 2
                .param("filter", "false")
                .param("tags", ""));

        //verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(2)))
                .andExpect(jsonPath("$.content[0].title", is("Note 1")))
                .andExpect(jsonPath("$.content[1].title", is("Note 3")));
    }

    @Test
    @Order(12)
    void testGetNotes_EmptyTags() throws Exception{
        ResultActions response = mockMvc.perform(get("/api/v1/notes")
                .param("page", "0") //1st page
                .param("size", "5") //page size is 5
                .param("filter", "true")
                .param("tags", ""));

        //verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Note 1")));
    }

    @Test
    @Order(13)
    void testGetNotes_BusinessImportantTags() throws Exception{
       //List<Tags> tags = List.of(Tags.IMPORTANT, Tags.BUSINESS);
        ResultActions response = mockMvc.perform(get("/api/v1/notes")
                .param("page", "0") //1st page
                .param("size", "5") //page size is 5
                .param("filter", "true")
                .param("tags", Tags.IMPORTANT.name(), Tags.BUSINESS.name()));

        //verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(2)))
                .andExpect(jsonPath("$.content[0].title", is("Note 5")))
                .andExpect(jsonPath("$.content[1].title", is("Note 3")));
    }

    @Test
    @Order(14)
    void testGetNotes_NotValid() throws Exception{
        //List<Tags> tags = List.of(Tags.IMPORTANT, Tags.BUSINESS);
        ResultActions response = mockMvc.perform(get("/api/v1/notes")
                .param("page", "-1") //invalid page
                .param("size", "0") //invalid size
                .param("filter", "true")
                .param("tags",""));

        //verify
        //Assert results
        String sizeError = "Size must be 1 or greater.";
        String pageError = "Page must be 0 or greater.";
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("Validation Failed")))
                .andExpect(jsonPath("$.details").value(Matchers.containsInAnyOrder(sizeError, pageError)));
    }

    @AfterAll
    void destroy(){
       notesRepository.deleteAll();
    }
}
