package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.adresse;

import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Adresse_;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BetterAccordion;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rene.zarwel on 02.03.16.
 *
 * Custom Field for a Value Object List of Adresse_
 */
public class Adresse_Accordion extends CustomField<List<Adresse_>> {

    /** Accordion is by default expanded on every tab **/
    private static final boolean DEFAULT_EXPAND = true;

    /** Accordion of Adresses **/
    private final BetterAccordion root = new BetterAccordion(DEFAULT_EXPAND);

    /** Container to save the Adresses linked by the Binder**/
    private List<Adresse_> adresseContainer;

    /**Components **/
    private List<Adresse_Form> adresseForms = new ArrayList<>();
    private VerticalLayout create = new VerticalLayout();

    public Adresse_Accordion() {
        super();
        initCreate();
    }

    /**
     * Initializes the Create form of a new Adresse_
     */
    private void initCreate(){
        Adresse_Form form = new Adresse_Form();
        form.setReadOnly(false);

        //Configure Button to add an Adresse_
        ActionButton add = new ActionButton(Adresse_.class, SimpleAction.add);
        add.addActionPerformer(clickEvent -> {
            try {
                Adresse_ adresse = form.getAdresse();
                //Add Adress Form
                addAdresseForm(adresse);
                //Add Adress to Container
                adresseContainer.add(adresse);
                //Reset CreateForm
                form.setAdresse(new Adresse_());
                form.getFields().stream()
                        .map(field -> (AbstractField)field)
                        .forEach(field -> field.setValidationVisible(false));
                return true;
            } catch (Exception e){
                return false;
            }
        });

        //Add Components
        form.getFormLayout().addComponent(add);
        create.addComponents(form);
        root.addStaticContent(create);
    }

    /**
     * Clears the whole Field and initializes the new Container
     * @param adresseList
     */
    public void setAdresseContainer(List<Adresse_> adresseList) {
        //Drop everything
        root.removeAllTabs();
        adresseForms.clear();

        //Build new on new container
        adresseContainer = adresseList;
        adresseList.forEach(this::addAdresseForm);
    }

    /**
     * Adds a single AdressForm.
     * @param adresse Adresse_ of new Form
     */
    private void addAdresseForm(Adresse_ adresse){

        //Build AddressForm-Tab
        Adresse_Form form = new Adresse_Form();
        form.setAdresse(adresse);
        adresseForms.add(form);

        //Build Delete Button
        ActionButton delete = new ActionButton(Adresse_.class, SimpleAction.delete);

        //Build ReadOnly Listener to change Visibility
        ReadOnlyStatusChangeListener deleteListener = (ReadOnlyStatusChangeListener) event -> delete.setVisible(!isReadOnly());
        addReadOnlyStatusChangeListener(deleteListener);

        //On click remove everything of this adresse and its components
        delete.addActionPerformer(clickEvent -> {
            adresseContainer.remove(form.getAdresse());
            setAdresseContainer(adresseContainer);
            removeReadOnlyStatusChangeListener(deleteListener);
            return true;
        });


        form.getFormLayout().addComponent(delete);

        root.addTab(form, String.valueOf(adresseForms.size()));
    }

    @Override
    public void setReadOnly(boolean readOnly){
        super.setReadOnly(readOnly);
        adresseForms.forEach(form -> form.setReadOnly(readOnly));
        create.setVisible(!readOnly);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends List<Adresse_>> getType() {
        return (Class<List<Adresse_>>)(Object)List.class;
    }


    @Override
    protected Component initContent() {
        return root;
    }

    @Override
    protected void setInternalValue(List<Adresse_> address) {
        super.setInternalValue(address);
        setAdresseContainer(address);
    }


    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        //Commit changes in the underlying forms too
        adresseForms.forEach(Adresse_Form::getAdresse);
        super.commit();
    }
}
