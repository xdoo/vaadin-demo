package de.muenchen.vaadin.ui.util;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.services.MessageService;
import org.springframework.stereotype.Component;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;

/**
 *
 * @author claus.straube
 */
@Component
public class VaadinUtil {
    
    public static final String TABLE_COLUMN_ACTIONS = "actions";
    
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
    
    public TextField createFormTextField(BeanFieldGroup binder, String label, String prompt, String property, String basePath) {
        TextField tf = (TextField) binder.buildAndBind(label, property);
        tf.setNullRepresentation("");
        tf.setInputPrompt(prompt);
        tf.setId(String.format("%s_%s_FIELD", basePath, property).toUpperCase());
        return tf;
    }
    
    public TextField createReadOnlyFormTextField(BeanFieldGroup binder, String label, String prompt, String property, String basePath) {
        TextField tf = this.createFormTextField(binder, label, prompt, property, basePath);
        tf.setReadOnly(Boolean.TRUE);
        return tf;
    }
    
    public DateField createFormDateField(BeanFieldGroup binder, String label, String property, String basePath) {
        DateField df = (DateField) binder.buildAndBind(label, property);
        df.setId(String.format("%s_%s_DATEFIELD", basePath, property).toUpperCase());
        return df;
    }
    
    public DateField createReadOnlyDateField(BeanFieldGroup binder, String label, String property, String basePath) {
        DateField df = this.createFormDateField(binder, label, property, basePath);
        df.setReadOnly(Boolean.TRUE);
        return df;
    }
    
}
