package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.sachbearbeiter;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.tokenfield.TokenField;
import com.vaadin.data.Validator;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;
import de.muenchen.vaadin.guilib.util.FormUtil;
import de.muenchen.vaadin.guilib.components.BaseComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides a very simple and basic Form for a Sachbearbeiter_.
 * <p/>
 * If no Sachbearbeiter_ is set, a blank user without an ID will be used. It has no buttons or additional components but can be
 * used for any Sachbearbeiter_ you set it to.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Sachbearbeiter_Form extends BaseComponent {

    /** The class of the Entity of this Form. */
    public static final Class<Sachbearbeiter_> ENTITY_CLASS = Sachbearbeiter_.class;
    
    /** The FormLayout that contains all the form fields. */
    private final FormLayout formLayout;
    
    /** Contains the current Sachbearbeiter_ and handles the data binding. */
    private final BeanFieldGroup<Sachbearbeiter_> binder = new BeanFieldGroup<>(ENTITY_CLASS);
    
    /** A list of all the Fields. */
    private final List<Field> fields;

    /**
     * Create a new Sachbearbeiter_Form using the specified i18nResolver and the eventbus.
     * <p/>
     * This Form is only the plain fields for input, and has no additional components or buttons. You can use {@link
     * Sachbearbeiter_Form#setReadOnly(boolean)} for a readonly mode.
     */
    public Sachbearbeiter_Form() {
        binder.setItemDataSource(new Sachbearbeiter_());
        fields = buildFields();

        final FormLayout formLayout = new FormLayout();
        formLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        fields.stream().forEach(formLayout::addComponent);

        this.formLayout = formLayout;
        setCompositionRoot(formLayout);
    }


    /**
     * Build all the (input) Fields used by this form.
     * <p/>
     * The Fields are data binded to the Sachbearbeiter_.
     *
     * @return A List of all Components.
     */
    private List<Field> buildFields() {
        final FormUtil formUtil = new FormUtil(getBinder());

		final TextField telefon = formUtil.createTextField(Sachbearbeiter_.Field.telefon.name());
		final TextField fax = formUtil.createTextField(Sachbearbeiter_.Field.fax.name());
		final TextField funktion = formUtil.createTextField(Sachbearbeiter_.Field.funktion.name());
		final TextField organisationseinheit = formUtil.createTextField(Sachbearbeiter_.Field.organisationseinheit.name());
		
        return Arrays.asList(telefon, fax, funktion, organisationseinheit);
    }

    /**
     * Get the Data-Binder of this Form.
     *
     * @return The binder.
     */
    private BeanFieldGroup<Sachbearbeiter_> getBinder() {
        return binder;
    }

    /**
     * Get the Sachbearbeiter_ object of this form.
     *
     * @return The Sachbearbeiter_.
     */
    public Sachbearbeiter_ getSachbearbeiter() {
        try {
            getBinder().commit();
        } catch (FieldGroup.CommitException e) {
            throw new Validator.InvalidValueException("Cannot create Sachbearbeiter.");
        }
        return getBinder().getItemDataSource().getBean();
    }

    /**
     * Set the Sachbearbeiter_ of this Form.
     *
     * @param sachbearbeiter The new Sachbearbeiter_.
     */
    public void setSachbearbeiter(Sachbearbeiter_ sachbearbeiter) {
        getBinder().setItemDataSource(sachbearbeiter == null ? new Sachbearbeiter_() : sachbearbeiter);
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
