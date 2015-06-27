package de.muenchen.vaadin.services;

import java.util.Locale;

/**
 *
 * @author claus.straube
 */
public interface MessageService {

    public String get(String path);
    
    public void setLocale(Locale locale);
    
    public Locale getLocale();
}
