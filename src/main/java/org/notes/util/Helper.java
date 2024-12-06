package org.notes.util;

import org.notes.model.Note;
import org.notes.model.web.SimplifiedWebNote;
import org.notes.model.web.WebNote;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Contains various Helper methods
public class Helper {

    /**
     * Split a sentence into lowercase words
     *
     * @param  sentence   sentence to split
     * @return list of words in sentence
     */
    public static List<String> splitStringIntoWords(String sentence){
        String regex = "[a-zA-Z]+(?:[-'][a-zA-Z]+)*+"; //matches words that can contain hyphens or apostrophes
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sentence);
        List<String> words = new ArrayList<>();
        while(matcher.find()) {
            words.add(matcher.group().toLowerCase());
        }
        return words;
    }

    /**
     * Sorts Map by descending order of value. Uses default comparator of value, so value should be a comparable.
     *
     * @param  map   map to sort
     * @return Sorted map
     */
    public static <K, V extends Comparable<? super V>>  Map<K, V> sortMapByValue(Map<K, V> map){
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue())); //Sort on value in descending order
        LinkedHashMap<K, V> linkedHashMap = new LinkedHashMap<>(); //maintains order
        for(Map.Entry<K,V> entry: list){
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }

    //Mapper from WebNote to Note
    public static Note mapWebNoteToNote(WebNote webNote){
        if(webNote.id() == null) {
            return new Note(webNote.title(),
                    webNote.createdDate(),
                    webNote.text(),
                    webNote.tags());
        }
        else {
            return new Note(webNote.id(),
                    webNote.title(),
                    webNote.createdDate(),
                    webNote.text(),
                    webNote.tags());
        }
    }

    //Mapper from Note to WebNote
    public static WebNote mapNoteToWebNote(Note note){
        return new WebNote(note.getId(),
                        note.getTitle(),
                        note.getCreatedDate(),
                        note.getText(),
                        note.getTags());
    }

    //Mapper from Note to SimplifiedWebNote
    public static SimplifiedWebNote mapNoteToSimplifiedWebNote(Note note) {
        return new SimplifiedWebNote(note.getId(),
                note.getTitle(),
                note.getCreatedDate());
    }
}
