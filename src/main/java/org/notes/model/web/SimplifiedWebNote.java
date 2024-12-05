package org.notes.model.web;

import java.time.LocalDate;

public record SimplifiedWebNote (String id,
                                 String title,
                                 LocalDate createdDate) {
}
