package de.muenchen.auditing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation um Entität für Auditing zu markieren. Optional können die Events die gespeichert werden sollen angegeben werden.
 * Created by fabian.holtkoetter on 08.09.15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MUCAudited {
    /**
     * Alle Zugriffe protokollieren.
     */
    String ALL = "all";
    /**
     * Lesende Zugriffe protokollieren.
     */
    String READ = "read";
    /**
     * Erstellen protokollieren.
     */
    String CREATE = "create";
    /**
     * Aktualisieren protokollieren.
     */
    String UPDATE = "update";
    /**
     * Löschen protokollieren.
     */
    String DELETE = "delete";

    String[] value() default ALL;
}
