package de.muenchen.vaadin.demo.i18nservice;



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

    /**
     * Get the fallback Locale.
     * <p>
     * This can be used as the default Locale if e.g. the desired Locale is not contained in the supported ones.
     *
     * @return the fallback locale.
     */
    Locale getFallbackLocale();
}
