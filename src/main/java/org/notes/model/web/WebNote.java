package org.notes.model.web;

import jakarta.validation.constraints.NotBlank;
import org.notes.model.Tags;

import java.time.LocalDate;
import java.util.List;

public record WebNote (String id,
                       @NotBlank(message = "Title must not be blank.") String title,
                       LocalDate createdDate,
                       @NotBlank(message = "Text must not be blank.")  String text,
                       List<Tags> tags) {
}
