package de.muenchen.vaadin.demo.i18nservice;

import com.vaadin.server.FontAwesome;

/**
 * Interface to open up most important functionality of a Controller.
 *
 * @author p.mueller
 */
public interface I18nResolver {
    /**
     * Build the complete Path with the basePath and resolve the String from the properties.
     * @param path
     * @return the resolved String.
     */
    String resolve(String path);

    /**
     * Resolves the Path with a Classname as BasePath and a String of the relative Path
     * @param clazz
     * @param relativePath
     * @return
     */
    String resolveRelative(Class clazz, String relativePath);

    /**
     * resolves the icon due to given Classname and relative path
     * @param clazz
     * @param relativePath
     * @return
     */
    FontAwesome resolveIcon(Class clazz, String relativePath);

    /**
     * returns the basePath resulting from a given class
     * @param clazz
     * @return
     */
    String getBasePath(Class clazz);
}
