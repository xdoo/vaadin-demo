package de.muenchen.vaadin.ui.util;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author claus.straube
 */
@Component
public class VaadinUtil {
    
    @Autowired
    MessageService i18n;
    
    public Button createNavigationButton(String baseKey, final String path) {
        String text = i18n.get(baseKey + ".navigation.button.label");
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
        return i18n.get(baseKey + "." + property + ".label");
    }
    
    public String readInputPrompt(String baseKey, String property) {
        return i18n.get(baseKey + "." + property + ".input_prompt");
    }
    
    public String readColumnHeader(String baseKey, String property) {
        return " " + i18n.get(baseKey + "." + property + ".column_header");
    }
    
    public String readText(String baseKey, String property) {
        return i18n.get(baseKey + "." + property);
    }
    
    public FontAwesome readColumnHeaderIcon(String baseKey, String property) {
        String icon = i18n.get(baseKey + "." + property + ".column_header.icon");
        if(!StringUtils.isEmpty(icon)) {
            return FontAwesome.valueOf(icon);
        }
        return null;
    }
    
}
