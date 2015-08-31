package de.muenchen.vaadin.services;



import java.util.Locale;
import java.util.Set;

/**
 *
 * @author claus.straube
 */
public interface I18nService {

    public String get(String path, Locale locale);

    /**
     * Get all the supported Locales by this Service.
     *
     * @return a set of supported Locales.
     */
    Set<Locale> getSupportedLocales();

    Locale getFallbackLocale();
}
