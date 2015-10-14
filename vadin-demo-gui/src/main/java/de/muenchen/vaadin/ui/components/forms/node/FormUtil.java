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
 * Provides a simple Util for creating various binded Fields on properties.
 * <p/>
 * This is kind of something like a {@link DefaultFieldGroupFieldFactory}.
 *
 * @author p.mueller
 * @version 1.0
 */
public class FormUtil {
    /**
     * The null representation on one input.
     */
    public static final String NULL_REPRESENTATION = "";
    /**
     * The binder that is used for the DataBinding.
     */
    private final BeanFieldGroup<?> binder;
    /**
     * The i18n Resolver.
     */
    private final I18nResolver i18nResolver;

    /**
     * Create a new FormUtil for the binder resolving string via i18n.
     *
     * @param binder       The binder containing the Data the Fields are bound to.
     * @param i18nResolver The i18nResolver used to resolve the Strings.
     */
    public FormUtil(BeanFieldGroup<?> binder, I18nResolver i18nResolver) {
        this.binder = binder;
        this.i18nResolver = i18nResolver;
    }

    /**
     * Create a new TextField for the given property.
     * <p/>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property The property to bind to of the entity.
     * @return A TextField bound to the property of the binder.
     */
    public TextField createTextField(String property) {
        final String caption = getCaption(property);
        final String prompt = getPrompt(property);

        TextField tf = (TextField) getBinder().buildAndBind(caption, property);
        tf.setNullRepresentation(NULL_REPRESENTATION);
        tf.setInputPrompt(prompt);
        //tf.setId(String.format("%s_%s_FIELD", getI18nResolver().getBasePath(), property).toUpperCase());
        return tf;
    }

    /**
     * Resolve the Caption of a Field for the specified property.
     *
     * @param property The property of the entity.
     * @return The caption of the property.
     */
    private String getCaption(String property) {
        return getI18nResolver().resolveRelative(I18nPaths.getEntityFieldPath(property, I18nPaths.Type.label));
    }

    /**
     * Resolve the Prompt of a Field for the specified property.
     *
     * @param property The property of the entity.
     * @return The prompt of the property.
     */
    private String getPrompt(String property) {
        return getI18nResolver().resolveRelative(I18nPaths.getEntityFieldPath(property, I18nPaths.Type.input_prompt));
    }

    /**
     * Get the binder of this Util.
     *
     * @return The binder.
     */
    public BeanFieldGroup<?> getBinder() {
        return binder;
    }

    /**
     * Get the i18n Resolver of this Util.
     *
     * @return
     */
    public I18nResolver getI18nResolver() {
        return i18nResolver;
    }

    /**
     * Create a ComboBox for the specified property. The enum is used to determine the possible values.
     * <p/>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property    The property of the entity.
     * @param enumeration The enum containing all possible values of the property.
     * @return The Combobox for the given property.
     */
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

    /**
     * Create a Date Field for the given property.
     * <p/>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property The property of the entity to bind tlo.
     * @return The DateField for the property.
     */
    public DateField createDateField(String property) {
        final String caption = getCaption(property);

        DateField df = (DateField) binder.buildAndBind(caption, property);
        //df.setId(String.format("%s_%s_DATEFIELD", getI18nResolver().getBasePath(), property).toUpperCase());

        return df;
    }
}
