package de.muenchen.vaadin.services;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import java.io.Serializable;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import static de.muenchen.vaadin.ui.util.I18nPaths.*;

/**
 *
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class MessageServiceImpl implements MessageService, Serializable {
    
    @Autowired
    private I18nService i18n;

    private Locale locale = Locale.getDefault();

    @Override
    public String get(String path) {
        return this.i18n.get(path, locale);
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FontAwesome getFontAwesome(String path) {
        String icon = this.get(path);
        if (!StringUtils.isEmpty(icon)) {
            return FontAwesome.valueOf(icon);
        }
        return null;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /*
    @Override
    public String readEntityField(String base, String property,Type type) {
        return this.get(getEntityFieldPath(base,property,type));
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
    */
}
