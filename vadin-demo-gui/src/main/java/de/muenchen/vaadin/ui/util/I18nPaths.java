package de.muenchen.vaadin.ui.util;

import de.muenchen.vaadin.ui.controller.BuergerViewController;

import java.util.Arrays;
import java.util.stream.Stream;


/**
 *
 * @author claus.straube
 */
public class I18nPaths {

    /**
     * Ist ein GUI-Komponententyp.
     */
    public enum Component implements I18nPath {
        button, headline
    }


    /**
     * Aktion der GUI Komponente.
     */
    public enum Action implements I18nPath {
        create, read, update, back, save, delete, cancel, copy
    }

    /**
     * Type des Texts
     */
    public enum Type implements I18nPath {
        label,text,title, column_header, input_prompt
    }

    private static String get(final I18nPath... paths) {
        return Stream.of(paths)
                .map(I18nPath::name).reduce("", (left, right) -> left + "." + right);
    }


    public static String getFormPath(Action a, Component c, Type t) {
        return ".form" + get(a, c, t);
    }

    public static String getPagePath(Type t) {
        return ".page" + get(t);
    }

    public static String getNavigationPath(Component c, Type t) {
        return ".navigation" + get(c,t);
    }

    public static String getSuccessNotificationPath(Action a, Type t) {
        return ".notification.success" + get(a,t);
    }

    public static String getEntityFieldPath(String field, Type t){
        return "." + field + get(t);
    }

    public static void main(String[] args) {
        System.out.println(I18nPaths.getFormPath(Action.create,Component.button, Type.label));
    }
    
}
