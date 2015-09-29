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
     * //TODO
     * @param relativePath
     * @return
     */
    String resolveRelative(String relativePath);

    FontAwesome resolveIcon(String relativePath);

    String getBasePath();
}
