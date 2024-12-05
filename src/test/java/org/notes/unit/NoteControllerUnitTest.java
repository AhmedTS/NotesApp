package org.notes.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.notes.controller.NoteController;
import org.notes.model.Note;
import org.notes.model.Tags;
import org.notes.model.web.WebNote;
import org.notes.service.NoteService;
import org.notes.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    private Note testNote;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setUp(){
        testNote = new Note(ObjectId.get().toString(),
                            "Test Note",
                            LocalDate.now(),
                            "Note is a note",
                            List.of(Tags.IMPORTANT, Tags.BUSINESS));
    }

    @Test
    @Order(1)
    void testGetNote_Exists() throws Exception{
        //Precondition
        WebNote testWebNote = Helper.mapNoteToWebNote(testNote);
        given(noteService.getNote(Mockito.anyString())).willReturn(Optional.of(testWebNote));

        //Test controller
        ResultActions response = mockMvc.perform(get("/api/v1/notes/{noteId}", testNote.getId()));

        //Assert results
        List<String> compareTags = testNote.getTags().stream().map(Enum::name).toList(); //json returns Tags as string
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testNote.getTitle())))
                .andExpect(jsonPath("$.text", is(testNote.getText())))
                .andExpect(jsonPath("$.createdDate", is(testNote.getCreatedDate().toString())))
                .andExpect(jsonPath("$.tags", is(compareTags)));

    }

    @Test
    @Order(2)
    void testGetNote_NotExists() throws Exception{
        //Precondition
        given(noteService.getNote(Mockito.anyString())).willReturn(Optional.empty());

        //Test controller
        ResultActions response = mockMvc.perform(get("/api/v1/notes/{noteId}", testNote.getId()));

        //Assert results
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.description", is("Note not found")))
                .andExpect(jsonPath("$.details[0]",is("No note matching id "+testNote.getId())));
    }

    @Test
    @Order(3)
    void testGetNote_NotValid() throws Exception{
        String testId = "testId"; //Invalid note ID
        //Precondition
        given(noteService.getNote(Mockito.anyString())).willReturn(Optional.empty());

        //Test controller
        ResultActions response = mockMvc.perform(get("/api/v1/notes/{noteId}", testId));

        //Assert results
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("Validation Failed")))
                .andExpect(jsonPath("$.details[0]",is("Invalid ObjectId")));
    }

    @Test
    @Order(4)
    void testDeleteNote() throws Exception{
        //Precondition
        willDoNothing().given(noteService).deleteNote(testNote.getId());

        //Test controller
        ResultActions response = mockMvc.perform(delete("/api/v1/notes/{noteId}", testNote.getId()));

        //Assert results
        response.andDo(print())
                .andExpect(status().isNoContent());
        //ensures noteService is only called once by deleteById
        verify(noteService, times(1)).deleteNote(testNote.getId());
    }

    @Test
    @Order(5)
    void testUpdateNote_Exists() throws Exception{
        //Precondition
        WebNote testWebNote = Helper.mapNoteToWebNote(testNote);
        given(noteService.updateNote(Mockito.anyString(), Mockito.any(WebNote.class)))
                .willReturn(Optional.of(testWebNote));

        //Test controller
        ResultActions response = mockMvc.perform(put("/api/v1/notes/{noteId}", testNote.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testWebNote)));

        //Assert results
        List<String> compareTags = testNote.getTags().stream().map(Enum::name).toList(); //json returns Tags as string
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testNote.getTitle())))
                .andExpect(jsonPath("$.text", is(testNote.getText())))
                .andExpect(jsonPath("$.createdDate", is(testNote.getCreatedDate().toString())))
                .andExpect(jsonPath("$.tags", is(compareTags)));
    }

    @Test
    @Order(6)
    void testUpdateNote_NotExists() throws Exception{
        //Precondition
        WebNote testWebNote = Helper.mapNoteToWebNote(testNote);
        given(noteService.updateNote(Mockito.anyString(), Mockito.any(WebNote.class)))
                .willReturn(Optional.empty());

        //Test controller
        ResultActions response = mockMvc.perform(put("/api/v1/notes/{noteId}", testNote.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testWebNote)));

        //Assert results
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.description", is("Note not found")))
                .andExpect(jsonPath("$.details[0]",is("No note matching id "+testNote.getId())));
    }

    @Test
    @Order(7)
    void testGetNoteStats_Exists() throws Exception{
        //Precondition
        Map<String, Integer> wordCountMap = new LinkedHashMap<>(){
            {
                put("note", 2);
                put("is", 1);
                put("a", 1);
            }
        };
        given(noteService.getNoteStats(Mockito.anyString()))
                .willReturn(wordCountMap);

        //Test controller
        ResultActions response = mockMvc.perform(get("/api/v1/notes/{noteId}/stats", testNote.getId()));

        //Assert results
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(wordCountMap)));
    }

    @Test
    @Order(8)
    void testGetNoteStats_NotExists() throws Exception{
        //Precondition
        Map<String, Integer> emptyMap = new LinkedHashMap<>();
        given(noteService.getNoteStats(Mockito.anyString()))
                .willReturn(emptyMap);

        //Test controller
        ResultActions response = mockMvc.perform(get("/api/v1/notes/{noteId}/stats", testNote.getId()));

        //Assert results
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.description", is("Note not found")));
    }
}
