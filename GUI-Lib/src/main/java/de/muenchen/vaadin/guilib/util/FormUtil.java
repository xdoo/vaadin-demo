package de.muenchen.vaadin.guilib.util;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.BaseUI;
import org.vaadin.tokenfield.TokenField;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides a simple Util for creating various binded Fields on properties.
 * <p>
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
     * The append for the id of an input.
     */
    public static final String INPUT = "-input";

    /**
     * The class for the I18nResolver
     */
    private Class entityClass;
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
     * @param binder The binder containing the Data the Fields are bound to.
     */
    public FormUtil(BeanFieldGroup<?> binder) {
        this.binder = binder;
        this.entityClass = binder.getItemDataSource().getBean().getClass();
        this.i18nResolver = BaseUI.getCurrentI18nResolver();
    }

    /**
     * Create a new TextField for the given property.
     * <p>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property The property to bind to of the entity.
     * @return A TextField bound to the property of the binder.
     */
    public TextField createTextField(String property) {
        final String caption = getCaption(property);
        final String prompt = getPrompt(property);
        final String tooltip = getTooltip(property);
        TextField tf = getBinder().buildAndBind(caption, property, TextField.class);
        tf.setNullRepresentation(NULL_REPRESENTATION);
        tf.setInputPrompt(prompt);
        tf.setId(property + INPUT);

        if (!tooltip.equals(""))
            tf.setDescription(tooltip);
        deactivateValidation(tf);
        //tf.setId(String.format("%s_%s_FIELD", getI18nResolver().getBasePath(), property).toUpperCase());
        return tf;
    }

    /**
     * Create a new PasswordField for the given property.
     * <p>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property The property to bind to of the entity.
     * @return A PasswordField bound to the property of the binder.
     */
    public PasswordField createPasswordField(String property) {
        final String caption = getCaption(property);
        final String prompt = getPrompt(property);
        final String tooltip = getTooltip(property);
        PasswordField tf = getBinder().buildAndBind(caption, property, PasswordField.class);
        tf.setNullRepresentation(NULL_REPRESENTATION);
        tf.setInputPrompt(prompt);
        tf.setId(property + INPUT);

        if (!tooltip.equals(""))
            tf.setDescription(tooltip);
        deactivateValidation(tf);
        return tf;
    }

    /**
     * Create a new TextArea for the given property.
     * <p>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property The property to bind to of the entity.
     * @return A TextArea bound to the property of the binder.
     */
    public TextArea createTextArea(String property) {
        final String caption = getCaption(property);
        final String prompt = getPrompt(property);
        final String tooltip = getTooltip(property);
        TextArea tf = getBinder().buildAndBind(caption, property, TextArea.class);
        tf.setNullRepresentation(NULL_REPRESENTATION);
        tf.setInputPrompt(prompt);
        tf.setId(property + INPUT);

        if (!tooltip.equals(""))
            tf.setDescription(tooltip);
        deactivateValidation(tf);
        return tf;
    }


    /**
     * Resolve the Caption of a Field for the specified property.
     *
     * @param property The property of the entity.
     * @return The caption of the property.
     */
    private String getCaption(String property) {
        return getI18nResolver().resolveRelative(entityClass, I18nPaths.getEntityFieldPath(property, I18nPaths.Type.label));
    }

    /**
     * Resolve the Prompt of a Field for the specified property.
     *
     * @param property The property of the entity.
     * @return The prompt of the property.
     */
    private String getPrompt(String property) {
        return getI18nResolver().resolveRelative(entityClass, I18nPaths.getEntityFieldPath(property, I18nPaths.Type.input_prompt));
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
     * Deactivates the visibility of the validation.
     * On value change or commit the visibility will be active again.
     *
     * @param field field configure
     */
    private void deactivateValidation(AbstractField field) {
        field.setValidationVisible(false);
        field.addValueChangeListener(event -> field.setValidationVisible(true));
        //Hack
        getBinder().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                field.setValidationVisible(true);
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
            }
        });
    }

    /**
     * Get the i18n Resolver of this Util.
     *
     * @return
     */
    public I18nResolver getI18nResolver() {
        return BaseUI.getCurrentI18nResolver();
    }

    /**
     * Create a ComboBox for the specified property. The enum is used to determine the possible values.
     * <p>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property The property of the entity.
     * @return The Combobox for the given property.
     */
    public ComboBox createComboBox(String property) {
        final String caption = getCaption(property);
        final String prompt = getPrompt(property);
        final String tooltip = getTooltip(property);
        ComboBox cb = getBinder().buildAndBind(caption, property, ComboBox.class);
        cb.setInputPrompt(prompt);
        cb.setTextInputAllowed(true);
        cb.setNullSelectionAllowed(false);
        cb.setId(property + INPUT);

        if (!tooltip.equals(""))
            cb.setDescription(tooltip);

        deactivateValidation(cb);
        return cb;
    }

    /**
     * Create a Date Field for the given property.
     * <p>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property The property of the entity to bind to.
     * @return The DateField for the property.
     */
    public DateField createDateField(String property) {
        final String caption = getCaption(property);
        final String tooltip = getTooltip(property);
        DateField df = getBinder().buildAndBind(caption, property, DateField.class);

        deactivateValidation(df);

        df.setId(property + INPUT);
        if (!tooltip.equals(""))
            df.setDescription(tooltip);

        return df;
    }


    /**
     * Create a CheckBox Field for the given property.
     * <p>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property The property of the entity to bind tlo.
     * @return The CheckBox for the property.
     */
    public CheckBox createCheckBox(String property) {
        final String caption = getCaption(property);
        final String tooltip = getTooltip(property);
        CheckBox df = getBinder().buildAndBind(caption, property, CheckBox.class);
        df.setId(property + INPUT);

        if (!tooltip.equals(""))
            df.setDescription(tooltip);

        deactivateValidation(df);
        return df;
    }

    /**
     * Create a TokenField for the given property.
     * <p>
     * <p>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property
     * @return The TokenField of the property
     */
    public TokenField createTokenField(String property, Class<?> clazz) {
        final String caption = getCaption(property);
        final String tooltip = getTooltip(property);
        //Group Elements of TokenField in CSS Layout
        CssLayout lo = new CssLayout();
        lo.addStyleName("v-component-group");

        TokenField tf = new TokenField(caption, lo) {
            public static final String SEPERATOR = ",";

            @Override
            protected void onTokenInput(Object tokenId) {
                //Multiple Tokens separated by ',' possible
                Stream.of(((String) tokenId).split(SEPERATOR))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .forEach(t -> super.onTokenInput(t));
            }

            //HACK TO PREVENT READONLYEXCEPTION
            //AND SHOW CORRECT FIELD IN READONLY MODE
            @Override
            protected void configureTokenButton(Object tokenId, Button button) {
                super.configureTokenButton(tokenId, button);
                button.removeStyleName(ValoTheme.BUTTON_LINK);
            }

            @Override
            public void setReadOnly(boolean readOnly) {
                super.setReadOnly(readOnly);
                buttons.values()
                        .forEach(button -> button.setEnabled(!readOnly));

                if (readOnly)
                    getLayout().removeComponent(cb);
            }
        };

        tf.setConverter(getListToSetConverter(clazz));
        tf.setConversionError(getConversionError(clazz));

        getBinder().bind(tf, property);

        tf.setRememberNewTokens(false);
        tf.setId(property + INPUT);

        if (!tooltip.equals(""))
            tf.setDescription(tooltip);

        deactivateValidation(tf);
        return tf;
    }

    private String getConversionError(Class<?> clazz) {
        if(clazz.equals(Long.class)){
            return BaseUI.getCurrentI18nResolver().resolve("tokenfield.conversion.error.long");
        } else if(clazz.equals(Date.class)){
            return BaseUI.getCurrentI18nResolver().resolve("tokenfield.conversion.error.date");
        } else if(clazz.equals(Boolean.class)){
            return BaseUI.getCurrentI18nResolver().resolve("tokenfield.conversion.error.boolean");
        } else if(clazz.equals(String.class))
            return BaseUI.getCurrentI18nResolver().resolve("tokenfield.conversion.error.string");
        else
            return BaseUI.getCurrentI18nResolver().resolve("tokenfield.conversion.error.other");
    }

    @SuppressWarnings("all")
    private <T> Converter getListToSetConverter(Class<? extends T> clazz) {
        if(clazz==null)
            throw new NullPointerException();
        return new Converter<Set<String>, List<T>>() {

            private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

            @Override
            public List<T> convertToModel(Set<String> value, Class<? extends List<T>> targetType,
                                               Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
                if(value.size()<=0)
                    return new ArrayList<T>();

                if(clazz.equals(Long.class)){
                    return value.stream().map(next -> {
                        try{
                            return (T)(new Long(next));
                        } catch (NumberFormatException e){
                            throw new ConversionException(e);
                        }
                        }).collect(Collectors.toList());
                } else if(clazz.equals(Date.class)){
                    return value.stream().map(next -> {
                        try {
                            return (T) (format.parse(next));
                        } catch (ParseException e) {
                            throw new ConversionException(e);
                        }
                    }).collect(Collectors.toList());
                } else if(clazz.equals(Boolean.class)){
                    return value.stream().map(next -> {
                        return (T)(new Boolean(next));
                    }).collect(Collectors.toList());
                } else if(clazz.equals(String.class))
                    return value.stream().map(o -> (T)o).collect(Collectors.toList());
                else
                    throw new ConversionException("Cannot convert to "+clazz);
            }

            @Override
            public Set<String> convertToPresentation(List<T> value, Class<? extends Set<String>> targetType,
                                                     Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
                if(clazz.equals(Date.class))
                    return value.stream().map(o -> format.format(o)).collect(Collectors.toSet());
                return value.stream().map(o -> o.toString()).collect(Collectors.toSet());
            }

            @Override
            public Class<List<T>> getModelType() {
                return (Class<List<T>>) (Class<?>) ArrayList.class;
            }

            @Override
            public Class<Set<String>> getPresentationType() {
                return (Class<Set<String>>) (Class<?>) HashSet.class;
            }


        };
    }

    private String getTooltip(String property) {

        return getI18nResolver().resolveRelative(entityClass, I18nPaths.getEntityFieldPath(property, I18nPaths.Type.tooltip));
    }


}
