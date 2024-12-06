package org.notes.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.bson.types.ObjectId;

//Custom validator to ensure String is a valid ObjectId
public class ObjectIdValidator implements ConstraintValidator<ValidObjectId, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false; // Let @NotNull handle null checks if required
        }
        // Validate if the string is a valid MongoDB ObjectId
        return ObjectId.isValid(value);
    }
}