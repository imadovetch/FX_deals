package org.bloomberg.fx_deals.Annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.bloomberg.fx_deals.Corevalidation.VerifyInstantFormatValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VerifyInstantFormatValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyInstantFormat {
    String message() default "Deal timestamp must be ISO-8601 format like 2025-08-04T02:36:07Z";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
