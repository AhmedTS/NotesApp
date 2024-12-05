package org.notes.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.notes.controller.NotesController;
import org.notes.model.Note;
import org.notes.model.Tags;
import org.notes.model.web.SimplifiedWebNote;
import org.notes.model.web.WebNote;
import org.notes.service.NotesService;
import org.notes.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotesController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotesControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotesService notesService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Note> testNotes;

    @BeforeAll
    void setUp(){
        Note note1 = new Note("Note 1", LocalDate.of(2024, 5, 5),"note is just a note",List.of());
        Note note2 = new Note("Note 2", LocalDate.of(2024, 6, 28), "Reminder: buy some eggs", List.of(Tags.PERSONAL));
        Note note3 = new Note("Note 3", LocalDate.of(2023, 9, 19), "Do not forget to send an email to danny tomorrow to agree on a strategy", List.of(Tags.BUSINESS,Tags.IMPORTANT));
        Note note4 = new Note("Note 4", LocalDate.of(2023, 4, 18), "Flight is on Wednesday", List.of(Tags.IMPORTANT));
        Note note5 = new Note("Note 5", LocalDate.of(2024, 10, 10), "Test note with all tags", List.of(Tags.IMPORTANT, Tags.BUSINESS, Tags.PERSONAL));
        testNotes = List.of(note1, note2, note3, note4, note5);
    }

    @Test
    @Order(1)
    void testCreateNote_Valid() throws Exception {
        //precondition
        WebNote testWebNote = new WebNote("testId",
                "Test Note",
                LocalDate.of(2024, 5, 11),
                "Note is a note",
                List.of(Tags.PERSONAL));
        given(notesService.createNote(Mockito.any(WebNote.class))).willReturn(testWebNote);

        //Test controller
        ResultActions response = mockMvc.perform(post("/api/v1/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testWebNote)));

        //Assert results
        List<String> compareTags = testWebNote.tags().stream().map(Enum::name).toList(); //json returns Tags as string
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testWebNote.title())))
                .andExpect(jsonPath("$.text", is(testWebNote.text())))
                .andExpect(jsonPath("$.createdDate", is(testWebNote.createdDate().toString())))
                .andExpect(jsonPath("$.tags", is(compareTags)));
    }

    @Test
    @Order(2)
    void testCreateNote_NotValid() throws Exception{
        //precondition
        WebNote testWebNote = new WebNote("testId",
                "", //blank title
                LocalDate.of(2024, 5, 11),
                "", //blank text
                List.of(Tags.PERSONAL));
        given(notesService.createNote(Mockito.any(WebNote.class))).willReturn(testWebNote);

        //Test controller
        ResultActions response = mockMvc.perform(post("/api/v1/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testWebNote)));

        //Assert results
        String textError = "Text must not be blank.";
        String titleError = "Title must not be blank.";
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("Validation Failed")))
                .andExpect(jsonPath("$.details").value(Matchers.containsInAnyOrder(textError, titleError)));
    }

    @Test
    @Order(3)
    void testGetNotes_NotValid() throws Exception{
        //precondition
        List<SimplifiedWebNote> simplifiedWebNotes = testNotes.stream()
                .map(Helper::mapNoteToSimplifiedWebNote)
                .toList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<SimplifiedWebNote> simplifiedWebNotesPage = new PageImpl<>(simplifiedWebNotes,pageable,simplifiedWebNotes.size());
        given(notesService.getAllNotes(Mockito.anyBoolean(),Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(simplifiedWebNotesPage);

        //Test controller
        ResultActions response = mockMvc.perform(get("/api/v1/notes")
                .param("page", "-1") //invalid page number
                .param("size", "0") //invalid size
                .param("filter", "false")
                .param("tags", ""));

        //Assert results
        String sizeError = "Size must be 1 or greater.";
        String pageError = "Page must be 0 or greater.";
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("Validation Failed")))
                .andExpect(jsonPath("$.details").value(Matchers.containsInAnyOrder(sizeError, pageError)));
    }

    @Test
    @Order(4)
    void testGetNotes_Valid() throws Exception{
        //precondition
        List<SimplifiedWebNote> simplifiedWebNotes = testNotes.stream()
                .map(Helper::mapNoteToSimplifiedWebNote)
                .toList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<SimplifiedWebNote> simplifiedWebNotesPage = new PageImpl<>(simplifiedWebNotes,pageable,simplifiedWebNotes.size());
        given(notesService.getAllNotes(Mockito.anyBoolean(),Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(simplifiedWebNotesPage);

        //Test controller
        ResultActions response = mockMvc.perform(get("/api/v1/notes")
                .param("page", "0")
                .param("size", "5")
                .param("filter", "false")
                .param("tags", ""));

        //Assert results
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(simplifiedWebNotesPage)));
    }
}
