package org.notes.service.impl;

import org.notes.model.Note;
import org.notes.model.web.WebNote;
import org.notes.repository.NotesRepository;
import org.notes.service.NoteService;
import org.notes.util.Helper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final NotesRepository notesRepository;

    public NoteServiceImpl(NotesRepository notesRepository){
        this.notesRepository = notesRepository;
    }

    @Override
    public Optional<WebNote> getNote(String noteId) {
        return notesRepository.findById(noteId).map(Helper::mapNoteToWebNote);
    }

    @Transactional
    @Override
    public Optional<WebNote> updateNote(String noteId, WebNote note){
        Optional<Note> optionalNote = notesRepository.findById(noteId);
        if(optionalNote.isEmpty())
            return Optional.empty();
        Note noteToUpdate = optionalNote.get();
        noteToUpdate.setTitle(note.title());
        noteToUpdate.setText(note.text());
        noteToUpdate.setTags(note.tags());
        return Optional.of(notesRepository.save(noteToUpdate)).map(Helper::mapNoteToWebNote);
    }

    @Transactional
    @Override
    public void deleteNote(String noteId){
        this.notesRepository.deleteById(noteId);
    }

    public Map<String, Integer> getNoteStats(String noteId){
        Map<String, Integer> wordCountMap = new LinkedHashMap<>();
        Optional<Note> optionalNote = notesRepository.findById(noteId);
        if(optionalNote.isEmpty())
            return wordCountMap; //Empty map indicates no note found, since note text can't be empty
        String noteText = optionalNote.get().getText();
        List<String> words = Helper.splitStringIntoWords(noteText);
        for(String word: words){
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0)+1); //increment count by 1
        }
        wordCountMap = Helper.sortMapByValue(wordCountMap);
        return wordCountMap;
    }

}
