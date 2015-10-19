package de.muenchen.vaadin.guilib.util;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import org.vaadin.tokenfield.TokenField;

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

        TextField tf = getBinder().buildAndBind(caption, property, TextField.class);
        tf.setNullRepresentation(NULL_REPRESENTATION);
        tf.setInputPrompt(prompt);

        deactivateValidation(tf);
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
     * @return The Combobox for the given property.
     */
    public ComboBox createComboBox(String property) {
        final String caption = getCaption(property);
        final String prompt = getPrompt(property);

        ComboBox cb = getBinder().buildAndBind(caption, property, ComboBox.class);
        cb.setInputPrompt(prompt);
        cb.setTextInputAllowed(true);
        cb.setNullSelectionAllowed(false);

        deactivateValidation(cb);
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

        DateField df = getBinder().buildAndBind(caption, property, DateField.class);

        deactivateValidation(df);
        //df.setId(String.format("%s_%s_DATEFIELD", getI18nResolver().getBasePath(), property).toUpperCase());

        return df;
    }


    /**
     * Create a CheckBox Field for the given property.
     * <p/>
     * It has no ID set, the individual component must take care of that.
     *
     * @param property The property of the entity to bind tlo.
     * @return The CheckBox for the property.
     */
    public CheckBox createCheckBox(String property) {
        final String caption = getCaption(property);

        CheckBox df = getBinder().buildAndBind(caption, property, CheckBox.class);

        deactivateValidation(df);
        return df;
    }

    public TokenField createTokenField(String property){
        final String caption = getCaption(property);

        //Group Elements of TokenField in CSS Layout
        CssLayout lo = new CssLayout();
        lo.addStyleName("v-component-group");

        TokenField tf = new TokenField(caption,lo) {

            @Override
            protected void onTokenInput(Object tokenId) {
                String[] tokens = ((String) tokenId).split(",");
                for (int i = 0; i < tokens.length; i++) {
                    String token = tokens[i].trim();
                    if (token.length() > 0) {
                        super.onTokenInput(token);
                    }
                }
            }

            //HACK TO PREVENT READONLYEXCEPTION AND SHOW CORRECT FIELD IN READONLY MODE
            @Override
            protected void configureTokenButton(Object tokenId, Button button) {
                //TODO get a good Style for the tokens
                button.addStyleName(ValoTheme.BUTTON_PRIMARY);

                if(!isReadOnly()) {
                    button.setCaption(this.getTokenCaption(tokenId) + " ×");
                }
                else {
                    button.setCaption(this.getTokenCaption(tokenId));
                    button.setEnabled(false);

                    //This is necessary because it will call a rebuild of this TokenField
                    //Rebuild will set the InputField to invisible
                    setTokenInsertPosition(InsertPosition.AFTER);
                }

            }
        };

        getBinder().bind(tf, property);

        tf.setPrimaryStyleName(TokenField.STYLE_TOKENFIELD);
        tf.setStyleName(TokenField.STYLE_TOKENFIELD);
        tf.setRememberNewTokens(false);

        deactivateValidation(tf);

        return tf;
    }

    /**
     * Deactivates the visibility of the validation.
     * On value change or commit the visibility will be active again.
     *
     * @param field field configure
     */
    private void deactivateValidation(AbstractField field){
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
}
