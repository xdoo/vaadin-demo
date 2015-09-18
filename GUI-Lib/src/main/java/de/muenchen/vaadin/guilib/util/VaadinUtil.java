package de.muenchen.vaadin.guilib.util;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.muenchen.vaadin.demo.api.domain.Augenfarbe;

import java.util.EnumSet;

/**
 *
 * @author claus.straube
 */
@SpringComponent
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

    public ComboBox createFormComboField(BeanFieldGroup binder, String label, String prompt, String property, String basePath) {
        ComboBox cb = (ComboBox) binder.buildAndBind(label, property);
        cb.setTextInputAllowed(false);
        cb.addItems(EnumSet.allOf(Augenfarbe.class));
        return cb;
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
