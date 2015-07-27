package de.muenchen.vaadin.services;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 *
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class MessageServiceImpl implements MessageService {

    @Autowired
    private I18nService i18n;

    private Locale locale;

    @Override
    public String get(String path) {
        return this.i18n.get(path, locale);
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String readLabel(String baseKey, String property) {
        return this.get(baseKey + "." + property + ".label");
    }

    @Override
    public String readInputPrompt(String baseKey, String property) {
        return this.get(baseKey + "." + property + ".input_prompt");
    }

    @Override
    public String readColumnHeader(String baseKey, String property) {
        return " " + this.get(baseKey + "." + property + ".column_header");
    }

    @Override
    public String readText(String baseKey, String property) {
        return this.get(baseKey + "." + property);
    }

    @Override
    public FontAwesome readColumnHeaderIcon(String baseKey, String property) {
        String icon = this.get(baseKey + "." + property + ".column_header.icon");
        if (!StringUtils.isEmpty(icon)) {
            return FontAwesome.valueOf(icon);
        }
        return null;
    }
}
