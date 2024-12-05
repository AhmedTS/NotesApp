package org.notes.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.notes.model.Tags;
import org.notes.model.web.SimplifiedWebNote;
import org.notes.model.web.WebNote;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface NotesService {
    Page<SimplifiedWebNote> getAllNotes(boolean filter,
                                        List<Tags> tags,
                                        @Min(value = 0, message = "Page must be 0 or greater") int page,
                                        @Min(value = 1, message = "Size must be 1 or greater") int size);
    WebNote createNote(@Valid WebNote note);
}
