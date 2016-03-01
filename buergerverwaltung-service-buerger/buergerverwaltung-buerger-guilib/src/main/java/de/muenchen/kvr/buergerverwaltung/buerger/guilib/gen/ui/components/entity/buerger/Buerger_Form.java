package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.buerger;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
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

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.vaadin.guilib.util.FormUtil;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.Augenfarben_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.MoeglicheStaatsangehoerigkeiten_;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides a very simple and basic Form for a Buerger_.
 * <p/>
 * If no Buerger_ is set, a blank user without an ID will be used. It has no buttons or additional components but can be
 * used for any Buerger_ you set it to.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Buerger_Form extends BaseComponent {

    /** The class of the Entity of this Form. */
    public static final Class<Buerger_> ENTITY_CLASS = Buerger_.class;
    
    /** The FormLayout that contains all the form fields. */
    private final FormLayout formLayout;
    
    /** Contains the current Buerger_ and handles the data binding. */
    private final BeanFieldGroup<Buerger_> binder = new BeanFieldGroup<>(ENTITY_CLASS);
    
    /** A list of all the Fields. */
    private final List<Field> fields;

    /**
     * Create a new Buerger_Form using the specified i18nResolver and the eventbus.
     * <p/>
     * This Form is only the plain fields for input, and has no additional components or buttons. You can use {@link
     * Buerger_Form#setReadOnly(boolean)} for a readonly mode.
     */
    public Buerger_Form() {
        binder.setItemDataSource(new Buerger_());
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
     * The Fields are data binded to the Buerger_.
     *
     * @return A List of all Components.
     */
    private List<Field> buildFields() {
        final FormUtil formUtil = new FormUtil(getBinder());

		final TextField vorname = formUtil.createTextField(Buerger_.Field.vorname.name());
		final TextField nachname = formUtil.createTextField(Buerger_.Field.nachname.name());
		final DateField geburtstag = formUtil.createDateField(Buerger_.Field.geburtstag.name());
		final ComboBox augenfarbe = formUtil.createComboBox(Buerger_.Field.augenfarbe.name());
		final TextField telefonnummer = formUtil.createTextField(Buerger_.Field.telefonnummer.name());
		final TextField email = formUtil.createTextField(Buerger_.Field.email.name());
		final CheckBox lebendig = formUtil.createCheckBox(Buerger_.Field.lebendig.name());
		final TokenField staatsangehoerigkeiten = formUtil.createEnumTokenField(
					Buerger_.Field.staatsangehoerigkeiten.name(),
					MoeglicheStaatsangehoerigkeiten_.class,
					new BeanItemContainer<>(MoeglicheStaatsangehoerigkeiten_.class, Arrays.asList(MoeglicheStaatsangehoerigkeiten_.values())));
		final TokenField eigenschaften = formUtil.createTokenField(Buerger_.Field.eigenschaften.name(), String.class);
		
        return Arrays.asList(vorname, nachname, geburtstag, augenfarbe, telefonnummer, email, lebendig, staatsangehoerigkeiten, eigenschaften);
    }

    /**
     * Get the Data-Binder of this Form.
     *
     * @return The binder.
     */
    private BeanFieldGroup<Buerger_> getBinder() {
        return binder;
    }

    /**
     * Get the Buerger_ object of this form.
     *
     * @return The Buerger_.
     */
    public Buerger_ getBuerger() {
        try {
            getBinder().commit();
        } catch (FieldGroup.CommitException e) {
            throw new Validator.InvalidValueException(FormUtil.getValidationErrorMessage(e));
        }
        return getBinder().getItemDataSource().getBean();
    }

    /**
     * Set the Buerger_ of this Form.
     *
     * @param buerger The new Buerger_.
     */
    public void setBuerger(Buerger_ buerger) {
        getBinder().setItemDataSource(buerger == null ? new Buerger_() : buerger);
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
