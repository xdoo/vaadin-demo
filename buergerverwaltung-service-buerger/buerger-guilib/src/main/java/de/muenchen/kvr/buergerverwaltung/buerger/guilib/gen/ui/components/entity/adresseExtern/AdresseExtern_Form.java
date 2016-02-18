package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.adresseExtern;

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

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.AdresseExtern_;
import de.muenchen.vaadin.guilib.util.FormUtil;
import de.muenchen.vaadin.guilib.components.BaseComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides a very simple and basic Form for a AdresseExtern_.
 * <p/>
 * If no AdresseExtern_ is set, a blank user without an ID will be used. It has no buttons or additional components but can be
 * used for any AdresseExtern_ you set it to.
 *
 * @author p.mueller
 * @version 1.0
 */
public class AdresseExtern_Form extends BaseComponent {

    /** The class of the Entity of this Form. */
    public static final Class<AdresseExtern_> ENTITY_CLASS = AdresseExtern_.class;
    
    /** The FormLayout that contains all the form fields. */
    private final FormLayout formLayout;
    
    /** Contains the current AdresseExtern_ and handles the data binding. */
    private final BeanFieldGroup<AdresseExtern_> binder = new BeanFieldGroup<>(ENTITY_CLASS);
    
    /** A list of all the Fields. */
    private final List<Field> fields;

    /**
     * Create a new AdresseExtern_Form using the specified i18nResolver and the eventbus.
     * <p/>
     * This Form is only the plain fields for input, and has no additional components or buttons. You can use {@link
     * AdresseExtern_Form#setReadOnly(boolean)} for a readonly mode.
     */
    public AdresseExtern_Form() {
        binder.setItemDataSource(new AdresseExtern_());
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
     * The Fields are data binded to the AdresseExtern_.
     *
     * @return A List of all Components.
     */
    private List<Field> buildFields() {
        final FormUtil formUtil = new FormUtil(getBinder());

		final TextField strasse = formUtil.createTextField(AdresseExtern_.Field.strasse.name());
		final TextField hausnummer = formUtil.createTextField(AdresseExtern_.Field.hausnummer.name());
		final TextField plz = formUtil.createTextField(AdresseExtern_.Field.plz.name());
		final TextField ort = formUtil.createTextField(AdresseExtern_.Field.ort.name());
		
        return Arrays.asList(strasse, hausnummer, plz, ort);
    }

    /**
     * Get the Data-Binder of this Form.
     *
     * @return The binder.
     */
    private BeanFieldGroup<AdresseExtern_> getBinder() {
        return binder;
    }

    /**
     * Get the AdresseExtern_ object of this form.
     *
     * @return The AdresseExtern_.
     */
    public AdresseExtern_ getAdresseExtern() {
        try {
            getBinder().commit();
        } catch (FieldGroup.CommitException e) {
            throw new Validator.InvalidValueException("Cannot create AdresseExtern.");
        }
        return getBinder().getItemDataSource().getBean();
    }

    /**
     * Set the AdresseExtern_ of this Form.
     *
     * @param adresseExtern The new AdresseExtern_.
     */
    public void setAdresseExtern(AdresseExtern_ adresseExtern) {
        getBinder().setItemDataSource(adresseExtern == null ? new AdresseExtern_() : adresseExtern);
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
