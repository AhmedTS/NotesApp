package org.notes.service;

import jakarta.validation.Valid;
import org.notes.model.web.WebNote;
import org.notes.validation.ValidObjectId;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.Optional;

@Validated
public interface NoteService {
    Optional<WebNote> getNote(@ValidObjectId String noteId);
    Optional<WebNote> updateNote(@ValidObjectId String noteId, @Valid WebNote note);
    void deleteNote(@ValidObjectId String noteId);
    Map<String, Integer> getNoteStats(@ValidObjectId String noteId);
}
