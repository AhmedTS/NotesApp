package org.notes.exception.exceptions;

//Exception class to handle cases where noteId parameter doesn't exist in database
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
