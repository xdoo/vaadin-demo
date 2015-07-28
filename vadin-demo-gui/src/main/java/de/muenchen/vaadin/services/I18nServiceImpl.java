package de.muenchen.vaadin.services;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * TODO -> umstellen!
 * 
 * @author claus.straube
 */
@Service
public class I18nServiceImpl implements I18nService {
    
    // HACK
    Map<String,String> messages = new HashMap<>();
    
    private static final Logger LOG = LoggerFactory.getLogger(I18nService.class);

    @Override
    public String get(String path, Locale locale) {
        if(this.messages.containsKey(path)) {
            return this.messages.get(path);
        } else {
            LOG.warn(String.format("found no message to path %s", path));
            return "";
        }
    }
    
    @PostConstruct
    private void init() {
       this.messages.put("m1.buerger.page.title", "Person Beispiel View");
       this.messages.put("m1.buerger.navigation.button.label", "Person Pflege");
       this.messages.put("m1.buerger.form.update.headline.label", "Person bearbeiten");
       this.messages.put("m1.buerger.form.create.headline.label", "Person erstellen");
       this.messages.put("m1.buerger.form.update.button.label", "aktualisieren");
       this.messages.put("m1.buerger.form.create.button.label", "neu");
       this.messages.put("m1.buerger.form.cancel.button.label", "abbrechen");
       this.messages.put("m1.buerger.form.save.button.label", "speichern");
       this.messages.put("m1.buerger.vorname.label", "Vorname:");
       this.messages.put("m1.buerger.vorname.column_header", "Vorname");
       this.messages.put("m1.buerger.vorname.column_header.icon", "");
       this.messages.put("m1.buerger.vorname.input_prompt", "Vorname");
       this.messages.put("m1.buerger.nachname.label", "Nachname:");
       this.messages.put("m1.buerger.nachname.column_header", "Nachname");
       this.messages.put("m1.buerger.nachname.column_header.icon", "");
       this.messages.put("m1.buerger.nachname.input_prompt", "Nachname");
       this.messages.put("m1.buerger.geburtsdatum.label", "Geburtsdatum:");
       this.messages.put("m1.buerger.geburtsdatum.column_header", "Geburtsdatum");
       this.messages.put("m1.buerger.geburtsdatum.column_header.icon", "");
       
    }
    
}
