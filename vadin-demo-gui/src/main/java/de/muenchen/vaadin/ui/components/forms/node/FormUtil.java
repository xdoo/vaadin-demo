package de.muenchen.vaadin.ui.components.forms.node;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;

import java.util.EnumSet;

/**
 * Created by p.mueller on 07.10.15.
 */
public class FormUtil extends DefaultFieldGroupFieldFactory {

    public static final String NULL_REPRESENTATION = "";
    private final BeanFieldGroup<?> binder;
    private final I18nResolver i18nResolver;

    public FormUtil(BeanFieldGroup<?> binder, I18nResolver i18nResolver) {
        this.binder = binder;
        this.i18nResolver = i18nResolver;
    }


    public TextField createTextField(String property) {
        final String caption = getCaption(property);
        final String prompt = getPrompt(property);

        TextField tf = (TextField) getBinder().buildAndBind(caption, property);
        tf.setNullRepresentation(NULL_REPRESENTATION);
        tf.setInputPrompt(prompt);
        //tf.setId(String.format("%s_%s_FIELD", getI18nResolver().getBasePath(), property).toUpperCase());
        return tf;
    }

    private String getCaption(String property) {
        return getI18nResolver().resolveRelative(I18nPaths.getEntityFieldPath(property, I18nPaths.Type.label));
    }

    private String getPrompt(String property) {
        return getI18nResolver().resolveRelative(I18nPaths.getEntityFieldPath(property, I18nPaths.Type.input_prompt));
    }

    public BeanFieldGroup<?> getBinder() {
        return binder;
    }

    public I18nResolver getI18nResolver() {
        return i18nResolver;
    }

    public ComboBox createComboBox(String property, Class enumeration) {
        final String caption = getCaption(property);
        final String prompt = getPrompt(property);

        ComboBox cb = new ComboBox(caption);
        cb.setInputPrompt(prompt);
        cb.setTextInputAllowed(true);
        cb.setNullSelectionAllowed(false);
        if (enumeration.isEnum()) {
            cb.addItems(EnumSet.allOf(enumeration));
        }
        getBinder().bind(cb, property);
        return cb;
    }

    public DateField createDateField(String property) {
        final String caption = getCaption(property);

        DateField df = (DateField) binder.buildAndBind(caption, property);
        //df.setId(String.format("%s_%s_DATEFIELD", getI18nResolver().getBasePath(), property).toUpperCase());

        return df;
    }
}
