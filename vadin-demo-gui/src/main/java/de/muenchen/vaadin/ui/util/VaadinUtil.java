package de.muenchen.vaadin.ui.util;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.services.MessageService;
import org.springframework.stereotype.Component;

/**
 *
 * @author claus.straube
 */
@Component
public class VaadinUtil {
    
    public Button createNavigationButton(String label, final String path) {
        Button button = new Button(label);
        button.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = -2896151918118631378L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo(path);
            }
        });
        return button;
    }
    
    public TextField createFormTextField(BeanFieldGroup binder, String basePath, String property, MessageService msg) {
        String label = msg.readLabel(basePath, Buerger.VORNAME);
        String prompt = msg.readInputPrompt(basePath, property);
        TextField tf = (TextField) binder.buildAndBind(label, property);
        tf.setNullRepresentation("");
        tf.setInputPrompt(prompt);
        return tf;
    }
    
    public TextField createReadOnlyFormTextField(BeanFieldGroup binder, String basePath, String property, MessageService msg) {
        TextField tf = this.createFormTextField(binder, basePath, property, msg);
        tf.setReadOnly(Boolean.TRUE);
        return tf;
    }
    
    public DateField createFormDateField(BeanFieldGroup binder, String basePath, String property, MessageService msg) {
        String label = msg.readLabel(basePath, property);
        DateField df = (DateField) binder.buildAndBind(label, property);
        return df;
    }
    
    public DateField createReadOnlyDateField(BeanFieldGroup binder, String basePath, String property, MessageService msg) {
        DateField df = this.createFormDateField(binder, basePath, property, msg);
        df.setReadOnly(Boolean.TRUE);
        return df;
    }
    
}
