/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.util;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.i18n.I18N;

/**
 *
 * @author claus.straube
 */
@Component
public class VaadinUtil {
    
    @Autowired
    I18N i18n;
    
    public Button createNavigationButton(String baseKey, final String path) {
        String text = i18n.get(baseKey + ".navigation.button.label", null);
        Button button = new Button(text);
        button.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = -2896151918118631378L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo(path);
            }
        });
        return button;
    }
    
    public TextField createFormTextField(BeanFieldGroup binder, String baseKey, String property) {
        TextField tf = (TextField) binder.buildAndBind(this.readLabel(baseKey, property), property);
        tf.setNullRepresentation("");
        tf.setInputPrompt(this.readInputPrompt(baseKey, property));
        return tf;
    }
    
    public DateField createFormDateField(BeanFieldGroup binder, String baseKey, String property) {
        DateField df = (DateField) binder.buildAndBind(this.readLabel(baseKey, property), property);
        return df;
    }
 
    public String readLabel(String baseKey, String property) {
        return i18n.get(baseKey + "." + property + ".label", null);
    }
    
    public String readInputPrompt(String baseKey, String property) {
        return i18n.get(baseKey + "." + property + ".input_prompt", null);
    }
    
    public String readColumnHeader(String baseKey, String property) {
        return i18n.get(baseKey + "." + property + ".column-header", null);
    }
    
}
