package de.muenchen.vaadin.ui.components.forms.node;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import de.muenchen.vaadin.demo.api.domain.Augenfarbe;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.ui.components.BaseComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides a very simple and basic Form for a Buerger.
 * <p/>
 * If no Buerger is set, a blank user without an ID will be used. It has no buttons or additional components but can be
 * used for any Buerger you set it to.
 *
 * @author p.mueller
 * @version 1.0
 */
public class BuergerForm extends BaseComponent {

    /**
     * The class of the Entity of this Form.
     */
    public static final Class<Buerger> ENTITY_CLASS = Buerger.class;
    /** The FormLayout that contains all the form fields. */
    private final FormLayout formLayout;
    /** Contains the current Buerger and handles the data binding. */
    private final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<>(ENTITY_CLASS);
    /** A list of all the Fields. */
    private final List<Component> fields;

    /**
     * Create a new BuergerForm using the specified i18nResolver and the eventbus.
     * <p/>
     * This Form is only the plain fields for input, and has no additional components or buttons. You can use {@link
     * BuergerForm#setReadOnly(boolean)} for a readonly mode.
     *
     * @param entityController The controller used for everything.
     */
    public BuergerForm(EntityController entityController) {
        super(entityController);
        binder.setItemDataSource(new Buerger());
        fields = buildFields();

        final FormLayout formLayout = new FormLayout();
        fields.stream().forEach(formLayout::addComponent);

        this.formLayout = formLayout;
        setCompositionRoot(formLayout);
    }


    /**
     * Build all the (input) Fields used by this form.
     * <p/>
     * The Fields are data binded to the Buerger.
     *
     * @return A List of all Components.
     */
    private List<Component> buildFields() {
        final FormUtil formUtil = new FormUtil(getBinder(), getI18nResolver());

        final TextField vorname = formUtil.createTextField(Buerger.Field.vorname.name());
        final TextField nachname = formUtil.createTextField(Buerger.Field.nachname.name());
        final ComboBox augenfarbe = formUtil.createComboBox(Buerger.Field.augenfarbe.name(), Augenfarbe.class);
        final DateField geburtsdatum = formUtil.createDateField(Buerger.Field.geburtsdatum.name());

        return Arrays.asList(vorname, nachname, augenfarbe, geburtsdatum);
    }

    /**
     * Get the Data-Binder of this Form.
     *
     * @return The binder.
     */
    private BeanFieldGroup<Buerger> getBinder() {
        return binder;
    }

    /**
     * Get the Buerger object of this form.
     *
     * @return The Buerger.
     */
    public Buerger getBuerger() {
        try {
            getBinder().commit();
        } catch (FieldGroup.CommitException e) {
            throw new AssertionError("ItemDataSource must be set?! See constructor. OR!! now because of validation...");
        }
        return getBinder().getItemDataSource().getBean();
    }

    /**
     * Set the Buerger of this Form.
     *
     * @param buerger The new Buerger.
     */
    public void setBuerger(Buerger buerger) {
        if (buerger == null)
            throw new NullPointerException();
        getBinder().setItemDataSource(buerger);
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
        getBinder().setReadOnly(true);
    }

    /**
     * Get all the (input) Fields of this form as a list.
     *
     * @return The list of components.
     */
    public List<Component> getFields() {
        return Collections.unmodifiableList(fields);
    }

}
