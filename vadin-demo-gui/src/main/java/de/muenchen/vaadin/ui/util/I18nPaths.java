package de.muenchen.vaadin.ui.util;

import de.muenchen.vaadin.ui.controller.BuergerViewController;

import java.util.Arrays;
import java.util.stream.Stream;


/**
 *
 * @author claus.straube
 */
public class I18nPaths {

    public enum Group implements I18nPath {
        form,navigation,page,notification
    }
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
        create,read,update,back,save
    }

    /**
     * Type des Texts
     */
    public enum Type implements I18nPath {
        label,text,title
    }

    private static String get(final I18nPath... paths) {
        return Stream.of(paths)
                .map(I18nPath::name).reduce("", (left, right) -> left + "." + right);
    }


    public static String getFormPath(String base, Action a, Component c, Type t) {
        return base + ".form" + get(a, c, t);
    }

    public static String getPagePath(String base, Type t) {
        return base + ".page" + get(t);
    }

    public static String getNavigationPath(String base, Component c, Type t) {
        return base + ".navigation" + get(c,t);
    }

    public static void main(String[] args) {
        System.out.println(I18nPaths.getFormPath("buerger",Action.create,Component.button, Type.label));
    }
    
}
