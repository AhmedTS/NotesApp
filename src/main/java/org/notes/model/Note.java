package org.notes.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "notes")
public class Note {

    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @NotBlank(message = "Title must not be blank.")
    private String title;
    private LocalDate createdDate;
    @NotBlank(message = "Text must not be blank.")
    private String text;
    private List<Tags> tags;

    public Note(String id, String title, LocalDate createdDate, String text, List<Tags> tags){
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.text = text;
        this.tags = tags;
    }

    //id is automatically generated
    @PersistenceCreator
    public Note(String title, LocalDate createdDate, String text, List<Tags> tags){
        this.title = title;
        this.createdDate = createdDate;
        this.text = text;
        this.tags = tags;
    }

    public String getId(){
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setCreatedDate(LocalDate createdDate){
        this.createdDate = createdDate;
    }

    public LocalDate getCreatedDate(){
        return createdDate;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public void setTags(List<Tags> tags){
        this.tags = tags;
    }

    public List<Tags> getTags(){
        return tags;
    }

    @Override
    public String toString() {
        return String.format("[id=%s, title=%s, createdDate=%s, tags=%s]", this.id, this.title, this.createdDate, this.tags);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Note noteObject))
            return false;
        return this.id.equals(noteObject.id)
                && this.title.equals(noteObject.title)
                && this.text.equals(noteObject.text)
                && this.tags.equals(noteObject.tags)
                && this.createdDate.equals(noteObject.createdDate);
    }

    @Override
    public int hashCode(){
        return toString().hashCode();
    }
}
