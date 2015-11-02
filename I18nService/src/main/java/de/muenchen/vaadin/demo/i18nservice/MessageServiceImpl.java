package de.muenchen.vaadin.demo.i18nservice;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

/**
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class MessageServiceImpl implements MessageService, Serializable {

    private final static Logger LOG = org.slf4j.LoggerFactory.getLogger(MessageService.class);

    private final I18nService i18n;
    private final Environment env;
    private Locale locale;

    @Autowired
    public MessageServiceImpl(I18nService i18n, Environment env) {
        this.env = env;
        this.i18n = i18n;

        this.locale = i18n.getFallbackLocale();
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
        if (i18n.getSupportedLocales().contains(locale)) {
            LOG.debug("Locale set to " + locale.getDisplayLanguage());
            this.locale = locale;
        } else {
            LOG.warn("Browser Locale " + locale.toLanguageTag() + " not supported. Using fallback locale.");
            this.locale = i18n.getFallbackLocale();
        }
    }

    @Override
    public Set<Locale> getSupportedLocales() {
        return i18n.getSupportedLocales();
    }
}
