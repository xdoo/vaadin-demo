package de.muenchen.vaadin.demo.apilib.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Created by arne.schoentag on 18.12.15.
 */
@Constraint(validatedBy = CheckKeyWordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NoKeyWord {

    String message() default "Schlüsselwörter dürfen nicht verwendet werden.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** Languages which Keywords can be checked. */
    public enum Keywords{
        JAVA, BARRAKUDA
    }

    /**
     * Array of the Languages which shall be checked.
     * @return
     */
    Keywords[] of() default {Keywords.JAVA, Keywords.BARRAKUDA};
}