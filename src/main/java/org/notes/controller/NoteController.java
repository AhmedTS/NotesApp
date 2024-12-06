package org.notes.controller;

import jakarta.validation.Valid;
import org.notes.exception.exceptions.NoteNotFoundException;
import org.notes.model.web.WebNote;
import org.notes.service.NoteService;
import org.notes.validation.ValidObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/notes/{noteId}")
@Validated
public class NoteController {

    // ----------------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------------

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }

    // ----------------------------------------------------------------------
    // Web methods
    // ----------------------------------------------------------------------

    //List notes
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<WebNote> getNote(@ValidObjectId @PathVariable String noteId){
        Optional<WebNote> note = noteService.getNote(noteId);
        if(note.isEmpty()){
            throw new NoteNotFoundException(noteId);
        }
        return ResponseEntity.ok(note.get());
    }

    //Delete Note
    @DeleteMapping("")
    @ResponseBody
    public ResponseEntity<Void> deleteNote(@ValidObjectId @PathVariable String noteId){
        noteService.deleteNote(noteId);
       return ResponseEntity.noContent().build();
    }

    //Update Note
    @PutMapping("")
    @ResponseBody
    public ResponseEntity<WebNote> updateNote(@ValidObjectId @PathVariable String noteId, @Valid @RequestBody WebNote note){
        Optional<WebNote> updatedNode = noteService.updateNote(noteId, note);
        if(updatedNode.isEmpty()){
            throw new NoteNotFoundException(noteId);
        }
        return ResponseEntity.ok(updatedNode.get());
    }

    //Get Node Stats
    @GetMapping("/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> getNoteStats(@ValidObjectId @PathVariable String noteId){
        Map<String, Integer> wordCountMap = noteService.getNoteStats(noteId);
        if(wordCountMap.isEmpty()){
            throw new NoteNotFoundException(noteId);
        }
        return ResponseEntity.ok(wordCountMap);
    }
}
