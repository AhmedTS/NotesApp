package org.notes.unit;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.notes.model.Note;
import org.notes.model.Tags;
import org.notes.repository.NotesRepository;
import org.notes.testbase.TestContainersBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


class NotesRepositoryUnitTest extends TestContainersBase {

    private final NotesRepository notesRepository;
    List<Note> notes;

    @Autowired
    public NotesRepositoryUnitTest(NotesRepository notesRepository){
        this.notesRepository = notesRepository;
    }

    @BeforeAll
    public void setUp(){
        notesRepository.deleteAll();
        notes = getExampleNotes();
        notes = notesRepository.saveAll(notes); //update values with saved objects including ID
    }

    @Test
    @Order(1)
    void testFindAll(){
        List<Note> allNotes = notesRepository.findAll();
        assertEquals(notes.size(), allNotes.size(), "Counts should match");
        assertEquals(notes, allNotes, "Lists should match");
    }

    @Test
    @Order(2)
    void testFindAll_SortedByDate(){
        List<Note> actualSortedNotes = notes.stream()
                .sorted(Comparator.comparing(Note::getCreatedDate).reversed())
                .toList();
        Sort sortByNewest = Sort.by("createdDate").descending();
        Pageable sortedPageable = Pageable.unpaged(sortByNewest);
        List<Note> allNotesSorted = notesRepository.findAll(sortedPageable).getContent();
        assertEquals(actualSortedNotes.size(), allNotesSorted.size(), "Counts should match");
        assertEquals(actualSortedNotes, allNotesSorted, "Lists should match");
    }

    @Test
    @Order(3)
    void testFindTagsMatchingEmpty(){
        List<Note> notesWithEmptyTags = notes.stream().filter(n -> n.getTags().isEmpty()).collect(Collectors.toList());
        List<Note> testNotesWithEmptyTags = notesRepository.findNotesByTags(List.of(), Pageable.unpaged()).getContent();
        assertEquals(notesWithEmptyTags, testNotesWithEmptyTags, "Empty Tags Lists should match");
    }

    @Test
    @Order(4)
    void testFindTagsContainingAll(){
        List<Tags> testTags = List.of(Tags.IMPORTANT, Tags.BUSINESS);
        List<Note> notesWithBusAndImpTags = notes.stream()
                                                 .filter(n -> n.getTags().containsAll(testTags))
                                                 .toList();
        List<Note> testNoteWithBusAndImpTags = notesRepository.findNotesByTagsContainingAll(testTags, Pageable.unpaged()).getContent();
        assertEquals(notesWithBusAndImpTags, testNoteWithBusAndImpTags, "Notes should match");
    }

    @Test
    @Order(5)
    void testDeleteAll(){
        notesRepository.deleteAll();
        List<Note> allNotes = notesRepository.findAll();
        assertEquals(0, allNotes.size(), "Notes should be empty");
    }

}