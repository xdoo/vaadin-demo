package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import de.muenchen.vaadin.demo.apilib.local.Authority;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.util.FormUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides a very simple and basic Form for a Authority_.
 * <p/>
 * If no Authority_ is set, a blank user without an ID will be used. It has no buttons or additional components but can be
 * used for any Authority_ you set it to.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Authority_Form extends BaseComponent {

    /** The class of the Entity of this Form. */
    public static final Class<Authority> ENTITY_CLASS = Authority.class;
    
    /** The FormLayout that contains all the form fields. */
    private final FormLayout formLayout;
    
    /** Contains the current Authority_ and handles the data binding. */
    private final BeanFieldGroup<Authority> binder = new BeanFieldGroup<>(ENTITY_CLASS);
    
    /** A list of all the Fields. */
    private final List<Field> fields;

    /**
     * Create a new Authority_Form using the specified i18nResolver and the eventbus.
     * <p/>
     * This Form is only the plain fields for input, and has no additional components or buttons. You can use {@link
     * Authority_Form#setReadOnly(boolean)} for a readonly mode.
     */
    public Authority_Form() {
        binder.setItemDataSource(new Authority());
        fields = buildFields();

        final FormLayout formLayout = new FormLayout();
        fields.stream().forEach(formLayout::addComponent);

        this.formLayout = formLayout;
        setCompositionRoot(formLayout);
    }


    /**
     * Build all the (input) Fields used by this form.
     * <p/>
     * The Fields are data binded to the Authority_.
     *
     * @return A List of all Components.
     */
    private List<Field> buildFields() {
        final FormUtil formUtil = new FormUtil(getBinder());

		final TextField authority = formUtil.createTextField(Authority.Field.authority.name());
		
        return Arrays.asList(authority);
    }

    /**
     * Get the Data-Binder of this Form.
     *
     * @return The binder.
     */
    private BeanFieldGroup<Authority> getBinder() {
        return binder;
    }

    /**
     * Get the Authority_ object of this form.
     *
     * @return The Authority_.
     */
    public Authority getAuthority() {
        try {
            getBinder().commit();
        } catch (FieldGroup.CommitException e) {
            throw new Validator.InvalidValueException("Cannot create Entity.");
        }
        return getBinder().getItemDataSource().getBean();
    }

    /**
     * Set the Authority_ of this Form.
     *
     * @param authority The new Authority_.
     */
    public void setAuthority(Authority authority) {
        getBinder().setItemDataSource(authority == null ? new Authority() : authority);
    }

    /**
     * Get the layout of this form, containing all the Fields.
     *
     * @return The base Layout.
     */
    public FormLayout getFormLayout() {
        return formLayout;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        getBinder().setReadOnly(readOnly);
    }

    /**
     * Get all the (input) Fields of this form as a list.
     *
     * @return The list of components.
     */
    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

}
