package org.notes.exception.handlers;

import java.util.List;

public class ErrorMessage {
    private String description;
    private List<String> details;

    public ErrorMessage(String description, List<String> details){
        this.description = description;
        this.details = details;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
