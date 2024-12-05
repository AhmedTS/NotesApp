package org.notes.repository;

import org.notes.model.Note;
import org.notes.model.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends MongoRepository<Note, String> {

    //Return notes where note.tags contains every tag value in the tags parameter
    @Query(value = "{ 'tags': { $all: ?0 } }")
    Page<Note> findNotesByTagsContainingAll(List<Tags> tags, Pageable pageable);
    //Return notes where note.tags is an exact match for tags parameter
    Page<Note> findNotesByTags(List<Tags> tags, Pageable pageable);
    long count();
}
