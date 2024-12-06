package org.notes.model.web;

import java.time.LocalDate;

//Record to return on title and date when listing notes
public record SimplifiedWebNote (String id,
                                 String title,
                                 LocalDate createdDate) {
}
