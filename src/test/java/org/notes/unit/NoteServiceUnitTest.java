package org.notes.unit;

import jakarta.validation.ConstraintViolationException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.notes.model.Note;
import org.notes.model.Tags;
import org.notes.model.web.WebNote;
import org.notes.repository.NotesRepository;
import org.notes.service.impl.NoteServiceImpl;
import org.notes.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteServiceUnitTest {

    @MockitoBean
    private NotesRepository notesRepository;

    @Autowired
    private NoteServiceImpl noteService;

    @Test
    @Order(1)
    void testGetNote_Exists(){
        String testId = ObjectId.get().toString();
        Note testNote = new Note(testId,
                "Test Note",
                LocalDate.now(),
                "note is just a note",
                List.of(Tags.IMPORTANT, Tags.PERSONAL));

        when(notesRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(testNote));

        Optional<WebNote> optionalWebNote = noteService.getNote(testId);
        assertTrue(optionalWebNote.isPresent());
        WebNote actualWebNote = optionalWebNote.get();
        Note actualNote = Helper.mapWebNoteToNote(actualWebNote);
        assertEquals(testNote, actualNote);
    }

    @Test
    @Order(2)
    void testGetNote_NotExists(){
        String testId = ObjectId.get().toString();
        when(notesRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Optional<WebNote> optionalWebNote = noteService.getNote(testId);
        assertTrue(optionalWebNote.isEmpty());
    }

    @Test
    @Order(3)
    void testGetNote_NotValid(){
        String testId = "testId"; //Invalid ObjectId
        when(notesRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ConstraintViolationException.class,
                () -> noteService.getNote(testId));
    }

    @Test
    @Order(4)
    void testUpdateNote_Exists(){
        Note testNote = new Note(ObjectId.get().toString(),
                "Test Note",
                LocalDate.now(),
                "note is just a note.",
                List.of(Tags.IMPORTANT, Tags.PERSONAL));
        WebNote testWNote = Helper.mapNoteToWebNote(testNote);
        when(notesRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(testNote));
        when(notesRepository.save(Mockito.any(Note.class))).thenReturn(testNote);

        Optional<WebNote> updatedWebNote = noteService.updateNote(testNote.getId(), testWNote);
        assertTrue(updatedWebNote.isPresent());
        assertEquals(testWNote, updatedWebNote.get());
    }

    @Test
    @Order(5)
    void testUpdateNote_NotExists(){
        Note testNote = new Note(ObjectId.get().toString(),
                "Test Note",
                LocalDate.now(),
                "note is just a note",
                List.of(Tags.IMPORTANT, Tags.PERSONAL));
        WebNote testWNote = Helper.mapNoteToWebNote(testNote);
        when(notesRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Optional<WebNote> updatedWebNote = noteService.updateNote(testNote.getId(), testWNote);
        assertTrue(updatedWebNote.isEmpty());
    }

    @Test
    @Order(6)
    void testUpdateNote_NotValid(){
        String testId = "testId";
        Note testNote = new Note(testId,
                "", //invalid title
                LocalDate.now(),
                "note is just a note",
                List.of(Tags.IMPORTANT, Tags.PERSONAL));
        WebNote testWNote = Helper.mapNoteToWebNote(testNote);
        when(notesRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ConstraintViolationException.class,
        () -> noteService.updateNote(testId, testWNote));
    }

    @Test
    @Order(7)
    void testDeleteNote(){
        String noteId = ObjectId.get().toString();
        willDoNothing().given(notesRepository).deleteById(noteId);

        noteService.deleteNote(noteId);

        //ensures noteRepository is only called once by deleteById
        verify(notesRepository, times(1)).deleteById(noteId);
    }

    @Test
    @Order(8)
    void testNodeStats_Exists(){
        Note testNote = new Note(ObjectId.get().toString(),
                "Test Note",
                LocalDate.now(),
                "This is just a sample note like any other note," +
                        " meant to test that the stats method is returning the note word count correctly. " +
                        "I really hope it works.",
                List.of(Tags.IMPORTANT, Tags.PERSONAL));
        //expected values by hand
        Map<String, Integer> expectedMap = new LinkedHashMap<>(){
            {
                put("note",3);
                put("is",2);
                put("the",2);
                put("this",1);
                put("just",1);
                put("a", 1);
                put("sample",1);
                put("like",1);
                put("any",1);
                put("other",1);
                put("meant",1);
                put("to",1);
                put("test",1);
                put("that",1);
                put("stats",1);
                put("method",1);
                put("returning",1);
                put("word",1);
                put("count",1);
                put("correctly",1);
                put("i",1);
                put("really",1);
                put("hope",1);
                put("it",1);
                put("works",1);
            }
        };
        when(notesRepository.findById(Mockito.anyString())).thenReturn(Optional.of(testNote));

        Map<String, Integer> actualMap = noteService.getNoteStats(testNote.getId());
        assertFalse(actualMap.isEmpty());
        assertEquals(expectedMap, actualMap);
    }

    @Test
    @Order(9)
    void testNodeStats_NotExists(){
        String noteId = ObjectId.get().toString();
        when(notesRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Map<String, Integer> wordCountMap = noteService.getNoteStats(noteId);
        assertTrue(wordCountMap.isEmpty());
    }
}
