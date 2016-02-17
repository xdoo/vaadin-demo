package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.pass;

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

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;
import de.muenchen.vaadin.guilib.util.FormUtil;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.Passtyp_;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides a very simple and basic Form for a Pass_.
 * <p/>
 * If no Pass_ is set, a blank user without an ID will be used. It has no buttons or additional components but can be
 * used for any Pass_ you set it to.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Pass_Form extends BaseComponent {

    /** The class of the Entity of this Form. */
    public static final Class<Pass_> ENTITY_CLASS = Pass_.class;
    
    /** The FormLayout that contains all the form fields. */
    private final FormLayout formLayout;
    
    /** Contains the current Pass_ and handles the data binding. */
    private final BeanFieldGroup<Pass_> binder = new BeanFieldGroup<>(ENTITY_CLASS);
    
    /** A list of all the Fields. */
    private final List<Field> fields;

    /**
     * Create a new Pass_Form using the specified i18nResolver and the eventbus.
     * <p/>
     * This Form is only the plain fields for input, and has no additional components or buttons. You can use {@link
     * Pass_Form#setReadOnly(boolean)} for a readonly mode.
     */
    public Pass_Form() {
        binder.setItemDataSource(new Pass_());
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
     * The Fields are data binded to the Pass_.
     *
     * @return A List of all Components.
     */
    private List<Field> buildFields() {
        final FormUtil formUtil = new FormUtil(getBinder());

		final TextField passNummer = formUtil.createTextField(Pass_.Field.passNummer.name());
		final ComboBox typ = formUtil.createComboBox(Pass_.Field.typ.name());
		final TextField kode = formUtil.createTextField(Pass_.Field.kode.name());
		final TextField groesse = formUtil.createTextField(Pass_.Field.groesse.name());
		final TextField behoerde = formUtil.createTextField(Pass_.Field.behoerde.name());
		
        return Arrays.asList(passNummer, typ, kode, groesse, behoerde);
    }

    /**
     * Get the Data-Binder of this Form.
     *
     * @return The binder.
     */
    private BeanFieldGroup<Pass_> getBinder() {
        return binder;
    }

    /**
     * Get the Pass_ object of this form.
     *
     * @return The Pass_.
     */
    public Pass_ getPass() {
        try {
            getBinder().commit();
        } catch (FieldGroup.CommitException e) {
            throw new Validator.InvalidValueException("Cannot create Pass.");
        }
        return getBinder().getItemDataSource().getBean();
    }

    /**
     * Set the Pass_ of this Form.
     *
     * @param pass The new Pass_.
     */
    public void setPass(Pass_ pass) {
        getBinder().setItemDataSource(pass == null ? new Pass_() : pass);
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
