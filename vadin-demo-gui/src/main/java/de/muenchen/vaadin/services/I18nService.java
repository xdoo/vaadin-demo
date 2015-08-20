package de.muenchen.vaadin.services;



import java.util.Locale;

/**
 *
 * @author claus.straube
 */
public interface I18nService {

    
    public String get(String path, Locale locale);
    
}
