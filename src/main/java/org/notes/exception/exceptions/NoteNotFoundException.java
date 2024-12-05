package org.notes.exception.exceptions;

public class NoteNotFoundException extends RuntimeException{
    String noteId;
    public NoteNotFoundException(String noteId){
        this.noteId = noteId;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
