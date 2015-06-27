package de.muenchen.vaadin.services;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus.straube
 */
@SpringComponent @UIScope
public class MessageServiceImpl implements MessageService {

    @Autowired private I18nService i18n;
    
    private Locale locale;
    
    @Override
    public String get(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
