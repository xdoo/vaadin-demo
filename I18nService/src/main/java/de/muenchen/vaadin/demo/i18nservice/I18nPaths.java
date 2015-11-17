package de.muenchen.vaadin.demo.i18nservice;

import de.muenchen.vaadin.demo.i18nservice.buttons.Action;

import java.util.stream.Stream;


/**
 * This utilityclass can be used to get correctly formatted paths for retrieving I18n-texts.
 * @author claus.straube
 */
public class I18nPaths {

    /**
     * Interface for an I18n-provider.
     */
    public interface I18nPath {
        String name();
    }

    /**
     * Type of the GUI-Component.
     */
    public enum Component implements I18nPath {
        button, headline
    }


    /**
     * Texttype.
     */
    public enum Type implements I18nPath {
        label,text,title, column_header, validation, validationstring, input_prompt, confirm, cancel, tooltip
    }

    /**
     * Type of a Notification.
     */
    public enum NotificationType implements I18nPath {
        error,success,warning,info,failure
    }

    /**
     * Concatenates the I18nPaths seperated by "."
     * @param paths list of I18nPaths to concatenate
     * @return concatenated String
     */
    private static String get(final I18nPath... paths) {
        return Stream.of(paths)
                .map(I18nPath::name).reduce("", (left, right) -> left + "." + right);
    }

    /**
     * Concatenates the additional attributes seperated by "."
     * @param attribues list of attribues to concatenate
     * @return concatenated String
     */
    private static String addAttribues(final String... attribues) {
        return Stream.of(attribues)
                .reduce("", (left, right) -> left + "." + right);
    }

    /**
     * Returns the formpath-ending for a form with the given arguments.
     * Params may not be null.
     * @param a SimpleAction the GUI is used for.
     * @param c Component this text is for.
     * @param t Type of this text.
     * @return path
     */
    public static String getFormPath(Action a, Component c, Type t, String... additionalAttributes) {
        return "form" + get(a, c, t) + addAttribues(additionalAttributes);
    }

    /**
     * Returns the pagepath-ending for a page with the given arguments.
     * Params may not be null.
     * @param t Type of this text.
     * @return path
     */
    public static String getPagePath(Type t, String... additionalAttributes) {
        return "page" + get(t) + addAttribues(additionalAttributes);
    }

    /**
     * Returns the navigationpath-ending for a navigation-item with the given arguments.
     * Params may not be null.
     * @param c Component this text is for.
     * @param t Type of this text.
     * @return path
     */
    public static String getNavigationPath(Component c, Type t, String... additionalAttributes) {
        return "navigation" + get(c,t) + addAttribues(additionalAttributes);
    }

    /**
     * Returns the path ending for a notification with the given arguments.
     * Params may not be null.
     * @param nt Type of this notification.
     * @param a SimpleAction the GUI is used for.
     * @param t Type of this text.
     * @return path
     */
    public static String getNotificationPath(NotificationType nt, Action a, Type t, String... additionalAttributes) {
        return "notification" + get(nt,a,t) + addAttribues(additionalAttributes);
    }

    /**
     * Returns the path ending for a confirmation with the given arguments.
     * Params may not be null.
     * @param a SimpleAction the GUI is used for.
     * @param t Type of this text.
     * @return path
     */
    public static String getConfirmationPath(Action a, Type t, String... additionalAttributes) {
        return "confirmation" + get(a,t) + addAttribues(additionalAttributes);
    }

    /**
     * Returns the path ending for a entityfield with the given arguments.
     * Params may not be null.
     * @param field Name of the entity-field.
     * @param t Type of this text.
     * @return path
     */
    public static String getEntityFieldPath(String field, Type t, String... additionalAttributes){
        return field + get(t) + addAttribues(additionalAttributes);
    }

}
