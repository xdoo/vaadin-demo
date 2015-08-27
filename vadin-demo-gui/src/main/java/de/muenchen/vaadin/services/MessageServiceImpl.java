package de.muenchen.vaadin.services;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Locale;

/**
 *
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class MessageServiceImpl implements MessageService, Serializable {

    /** Default Local of App if requested local is not supported**/
    private final static Locale DEFAULT_LOCAL = Locale.GERMANY;
    
    @Autowired
    private I18nService i18n;

    private Locale locale = Locale.getDefault();

    @Autowired
    private Environment env;

    @PostConstruct
    private void init() {
        //TODO Better check if locale is supported
        if (!i18n.get("supported" , locale).equals("TRUE")) {
            locale = DEFAULT_LOCAL;
        }

    }

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
