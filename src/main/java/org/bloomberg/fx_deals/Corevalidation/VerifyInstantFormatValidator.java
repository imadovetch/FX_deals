package org.bloomberg.fx_deals.Corevalidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.bloomberg.fx_deals.Annotation.VerifyInstantFormat;

public class VerifyInstantFormatValidator implements ConstraintValidator<VerifyInstantFormat, String> {

    private static final String ISO_8601_REGEX = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull to enforce null check separately
        }
        return value.matches(ISO_8601_REGEX);
    }
}
