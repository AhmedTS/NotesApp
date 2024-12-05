package org.notes.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.notes.model.Tags;
import org.notes.model.web.SimplifiedWebNote;
import org.notes.model.web.WebNote;
import org.notes.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/notes")
@Validated
public class NotesController {

    // ----------------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------------

    private final NotesService notesService;

    @Autowired
    public NotesController(NotesService notesService){
        this.notesService = notesService;
    }

    // ----------------------------------------------------------------------
    // Web methods
    // ----------------------------------------------------------------------

    //List notes
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<Page<SimplifiedWebNote>> getNotes(@RequestParam(value = "filter", required = false, defaultValue = "false") boolean filter,
                                                            @RequestParam(value = "tags", required = false, defaultValue = "") List<Tags> tags,
                                                            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(value = 0, message="Page must be 0 or greater.") int page,
                                                            @RequestParam(value = "size", required = false, defaultValue = "5") @Min(value = 1, message="Size must be 1 or greater.") int size){
        Page<SimplifiedWebNote> notesList = notesService.getAllNotes(filter, tags, page, size);
        return ResponseEntity.ok(notesList);
    }

    //Create Note
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<WebNote> createNote(@Valid @RequestBody WebNote note){
        WebNote createdNote = notesService.createNote(note);
        return ResponseEntity.ok(createdNote);
    }

}
