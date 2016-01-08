package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import de.muenchen.vaadin.demo.apilib.local.User;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.util.FormUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides a very simple and basic Form for a User_.
 * <p>
 * If no User_ is set, a blank user without an ID will be used. It has no buttons or additional components but can be
 * used for any User_ you set it to.
 *
 * @author p.mueller
 * @version 1.0
 */
public class User_Form extends BaseComponent {

    /**
     * The class of the Entity of this Form.
     */
    public static final Class<User> ENTITY_CLASS = User.class;

    /**
     * The FormLayout that contains all the form fields.
     */
    private final FormLayout formLayout;

    /**
     * Contains the current User_ and handles the data binding.
     */
    private final BeanFieldGroup<User> binder = new BeanFieldGroup<>(ENTITY_CLASS);

    /**
     * A list of all the Fields.
     */
    private final List<Field> fields;

    /**
     * Create a new User_Form using the specified i18nResolver and the eventbus.
     * <p>
     * This Form is only the plain fields for input, and has no additional components or buttons. You can use {@link
     * User_Form#setReadOnly(boolean)} for a readonly mode.
     */
    public User_Form() {
        binder.setItemDataSource(new User());
        fields = buildFields();

        final FormLayout formLayout = new FormLayout();
        fields.stream().forEach(formLayout::addComponent);

        this.formLayout = formLayout;
        setCompositionRoot(formLayout);
    }


    /**
     * Build all the (input) Fields used by this form.
     * <p>
     * The Fields are data binded to the User_.
     *
     * @return A List of all Components.
     */
    private List<Field> buildFields() {
        final FormUtil formUtil = new FormUtil(getBinder());

		final TextField username = formUtil.createTextField(User.Field.username.name());
		final TextField forname = formUtil.createTextField(User.Field.forname.name());
		final TextField surname = formUtil.createTextField(User.Field.surname.name());
		final DateField birthday = formUtil.createDateField(User.Field.birthdate.name());
		final TextField email = formUtil.createTextField(User.Field.email.name());
		final CheckBox userEnabled = formUtil.createCheckBox(User.Field.userEnabled.name());
		
        return Arrays.asList(username, forname, surname, birthday, email, userEnabled);
    }

    /**
     * Get the Data-Binder of this Form.
     *
     * @return The binder.
     */
    private BeanFieldGroup<User> getBinder() {
        return binder;
    }

    /**
     * Get the User_ object of this form.
     *
     * @return The User_.
     */
    public User getUser() {
        try {
            getBinder().commit();
        } catch (FieldGroup.CommitException e) {
            throw new Validator.InvalidValueException("Cannot create Entity.");
        }
        return getBinder().getItemDataSource().getBean();
    }

    /**
     * Set the User_ of this Form.
     *
     * @param user The new User_.
     */
    public void setUser(User user) {
        getBinder().setItemDataSource(user == null ? new User() : user);
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
