package org.notes.service.impl;

import org.notes.model.Note;
import org.notes.model.Tags;
import org.notes.model.web.SimplifiedWebNote;
import org.notes.model.web.WebNote;
import org.notes.repository.NotesRepository;
import org.notes.service.NotesService;
import org.notes.util.Helper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;

    public NotesServiceImpl(NotesRepository notesRepository){
        this.notesRepository = notesRepository;
    }

    public WebNote createNote(WebNote webNote){
        Note note = Helper.mapWebNoteToNote(webNote);
        note.setCreatedDate(LocalDate.now());
        Note savedNote = notesRepository.save(note);
        return Helper.mapNoteToWebNote(savedNote);
    }

    public Page<SimplifiedWebNote> getAllNotes(boolean filter, List<Tags> tags, int page, int size){
        Sort sortByNewest = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sortByNewest);
        if(filter){
            if(tags.isEmpty()){ //match all empty tags
                return notesRepository.findNotesByTags(tags, pageable)
                        .map(Helper::mapNoteToSimplifiedWebNote);
            }
            //match notes containing all tags
            return notesRepository.findNotesByTagsContainingAll(tags, pageable)
                    .map(Helper::mapNoteToSimplifiedWebNote);
        }
        return notesRepository.findAll(pageable)
                .map(Helper::mapNoteToSimplifiedWebNote);
    }

}
