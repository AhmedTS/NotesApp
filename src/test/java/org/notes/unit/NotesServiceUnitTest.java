package org.notes.unit;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.notes.model.Note;
import org.notes.model.Tags;
import org.notes.model.web.SimplifiedWebNote;
import org.notes.model.web.WebNote;
import org.notes.repository.NotesRepository;
import org.notes.service.impl.NotesServiceImpl;
import org.notes.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;


import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotesServiceUnitTest {
    @MockitoBean
    private NotesRepository notesRepository;

    @Autowired
    private NotesServiceImpl notesService;

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
    void testCreateNote(){
        //precondition
        Note testNote = new Note("testID",
                "Test Note",
                LocalDate.now(),
                "note is just a note",
                List.of(Tags.IMPORTANT, Tags.PERSONAL));
        WebNote testWebNote = Helper.mapNoteToWebNote(testNote);
        given(notesRepository.save(Mockito.any(Note.class))).willReturn(testNote);

        //test service
        WebNote actualWebNote = notesService.createNote(testWebNote);

        //assert results
        assertNotNull(actualWebNote);
        assertEquals(actualWebNote, testWebNote);
    }

    @Test
    @Order(2)
    void testCreateNote_NotValid(){
        //precondition
        Note testNote = new Note("testID",
                "",
                LocalDate.now(),
                "note is just a note",
                List.of(Tags.IMPORTANT, Tags.PERSONAL));
        WebNote testWebNote = Helper.mapNoteToWebNote(testNote);
        given(notesRepository.save(Mockito.any(Note.class))).willReturn(testNote);

        //test service
        assertThrows(ConstraintViolationException.class,
                () -> notesService.createNote(testWebNote));

    }

    @Test
    @Order(3)
    void testGetNotes_NotValid(){
        //parameters
        boolean filter = false;
        List<Tags> tags = List.of();
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Note> testNotesPage = new PageImpl<>(testNotes, pageable, testNotes.size());
        given(notesRepository.findAll(Mockito.any(Pageable.class)))
                .willReturn(testNotesPage);

        //test service
        int invalidPage = -1;
        int invalidSize = 0;
        assertThrows(ConstraintViolationException.class,
                () -> notesService.getAllNotes(filter, tags, invalidPage, invalidSize));

    }

    @Test
    @Order(4)
    void testGetNotes_NoFilter(){
        //parameters
        boolean filter = false;
        List<Tags> tags = List.of();
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Note> testNotesPage = new PageImpl<>(testNotes, pageable, testNotes.size());
        Page<SimplifiedWebNote> testWebNotesPages = testNotesPage.map(Helper::mapNoteToSimplifiedWebNote);
        given(notesRepository.findAll(Mockito.any(Pageable.class)))
                .willReturn(testNotesPage);

        //Test service
        Page<SimplifiedWebNote> actualWebNotes = notesService.getAllNotes(filter, tags, page, size);

        //assert results
        assertTrue(actualWebNotes.hasContent());
        assertEquals(testWebNotesPages.getContent(), actualWebNotes.getContent());

    }

    @Test
    @Order(5)
    void testGetNotes_FilterEmptyTags(){
        //parameters
        boolean filter = true;
        List<Tags> tags = List.of();
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Note> testNotesPage = new PageImpl<>(testNotes, pageable, testNotes.size());
        Page<SimplifiedWebNote> testWebNotesPages = testNotesPage.map(Helper::mapNoteToSimplifiedWebNote);
        given(notesRepository.findNotesByTags(Mockito.anyList(), Mockito.any(Pageable.class)))
                .willReturn(testNotesPage);

        //Test service
        Page<SimplifiedWebNote> actualWebNotes = notesService.getAllNotes(filter, tags, page, size);

        //assert results
        assertTrue(actualWebNotes.hasContent());
        assertEquals(testWebNotesPages.getContent(), actualWebNotes.getContent());
    }

    @Test
    @Order(6)
    void testGetNotes_FilterNonEmptyTags(){
        //parameters
        boolean filter = true;
        List<Tags> tags = List.of(Tags.PERSONAL);
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Note> testNotesPage = new PageImpl<>(testNotes, pageable, testNotes.size());
        Page<SimplifiedWebNote> testWebNotesPages = testNotesPage.map(Helper::mapNoteToSimplifiedWebNote);
        given(notesRepository.findNotesByTagsContainingAll(Mockito.anyList(), Mockito.any(Pageable.class)))
                .willReturn(testNotesPage);

        //Test service
        Page<SimplifiedWebNote> actualWebNotes = notesService.getAllNotes(filter, tags, page, size);

        //assert results
        assertTrue(actualWebNotes.hasContent());
        assertEquals(testWebNotesPages.getContent(), actualWebNotes.getContent());
    }
}
