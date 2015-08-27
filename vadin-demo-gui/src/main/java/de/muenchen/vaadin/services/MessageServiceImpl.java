package de.muenchen.vaadin.services;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.Serializable;
import java.util.Locale;

/**
 *
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class MessageServiceImpl implements MessageService, Serializable {
    
    @Autowired
    private I18nService i18n;

    private Locale locale = Locale.ENGLISH;

    @Autowired
    private Environment env;

    @Override
    public String get(String path) {
        return this.i18n.get(path, locale);
    }

    @Override
    public FontAwesome getFontAwesome(String path) {
        // Icons must not be localized.
        String icon = env.getProperty(path);

        if (icon == null)
            return null;
        if (icon.isEmpty())
            return null;

        return FontAwesome.valueOf(icon);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
