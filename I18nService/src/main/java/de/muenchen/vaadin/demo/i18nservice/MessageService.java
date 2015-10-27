package de.muenchen.vaadin.demo.i18nservice;

import com.vaadin.server.FontAwesome;

import java.util.Locale;
import java.util.Set;

/**
 *
 * @author claus.straube
 */
public interface MessageService {
    
    public final static String I18N_NAVIGATION_BUTTON_LABEL =  ".navigation.button.label";

    public String get(String path);
    public FontAwesome getFontAwesome(String path);

    public void setLocale(Locale locale);
    
    public Locale getLocale();

    public Set<Locale> getSupportedLocales();

    /*
    public String readLabel(String baseKey, String property);
    
    public String readInputPrompt(String baseKey, String property);
    
    public String readColumnHeader(String baseKey, String property);
    
    public String readText(String baseKey, String property);
    
    public FontAwesome readColumnHeaderIcon(String baseKey, String property);
    */
}
