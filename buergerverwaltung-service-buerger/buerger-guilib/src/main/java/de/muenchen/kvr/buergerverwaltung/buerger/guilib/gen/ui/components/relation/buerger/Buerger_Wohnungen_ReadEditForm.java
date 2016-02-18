package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.buerger;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;

import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.components.ConfirmationWindow;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_AssociationActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.wohnung.Wohnung_SingleActions;

public class Buerger_Wohnungen_ReadEditForm extends BaseComponent {

    /** The root layout for the content of this component. */
	private final VerticalLayout layout = new VerticalLayout();
	
	/** The layout for the buttons (that are always visible). */
	private final HorizontalLayout buttonsAlwaysVisible = new HorizontalLayout();
	
	/** The layout for the buttons that are added to the form. */
	private final HorizontalLayout buttonsInForm = new HorizontalLayout();
	
	/** The create Button. */
	private final ActionButton createButton = new ActionButton(Buerger_.class, SimpleAction.create);
	
	/** The button for the add action. */
	private final ActionButton addButton = new ActionButton(Buerger_.class, SimpleAction.add);
	
	/** The button for the read action. */
	private final ActionButton readButton = new ActionButton(Buerger_.class, SimpleAction.read);
	
	/** The button for the delete action. */
	private final ActionButton deleteButton = new ActionButton(Buerger_.class, SimpleAction.delete);
	
	/** The navigation for the create action. */
	private final NavigateActions createNavigation;
	
	/** The navigation for the read action. */
	private final NavigateActions readNavigation;
	
	/** The navigation for the add action. */
	private final NavigateActions addNavigation;
	
	/** The underlying form. */
    private final Buerger_Wohnungen_Form wohnungenForm;

    /**
     * Formular zum Lesen eines {@link Wohnung_}s. Über diesen Konstruktor kann zusätzlich eine Zielseite für die
     * 'zurück' und 'bearbeiten' Schaltflächen erstellt werden.
     *
     * @param navigateToRead
     * @param navigateToCreate
     * @param navigateToAdd
     */
    public Buerger_Wohnungen_ReadEditForm(final String navigateToRead, final String navigateToCreate, final String navigateToAdd) {
        wohnungenForm = new Buerger_Wohnungen_Form() {
            @Override
            public void setWohnung(Wohnung_ wohnung) {
                setFormVisible(wohnung != null);
                super.setWohnung(wohnung);
            }
        };
        wohnungenForm.reLoad();
        wohnungenForm.setReadOnly(true);

        this.createNavigation = new NavigateActions((navigateToCreate == null)? "" : navigateToCreate);
		this.readNavigation = new NavigateActions((navigateToRead == null)? "" : navigateToRead);
		this.addNavigation = new NavigateActions((navigateToAdd == null)? "" : navigateToAdd);

        init();
        setIds();
    }

    /**
     * Build the basic layout and insert the headline and all Buttons.
     */
    private void init() {
    	if(getNavigateToCreate() != null){
    		configureCreateButton();
    		buttonsAlwaysVisible.addComponent(createButton);
    	}
    	if(getNavigateToAdd() != null){
    		configureAddButton();
    		buttonsAlwaysVisible.addComponent(addButton);
    	}
    	if(getNavigateToRead() != null){
    		configureReadButton();
    		buttonsAlwaysVisible.addComponent(readButton);
    	}
    	configureDeleteButton();
    	
        buttonsAlwaysVisible.setSpacing(true);
        
        buttonsInForm.setSpacing(true);
        buttonsInForm.addComponent(deleteButton);
        getForm().getFormLayout().addComponent(buttonsInForm);

        layout.addComponents(buttonsAlwaysVisible, getForm());
        setCompositionRoot(layout);
    }
    
    /**
	 * Set the IDs for important components.
	 */
	private void setIds() {
		setId(getClass().getSimpleName());
		getForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
		getForm().setId(getId() + "#form");
		if(getNavigateToCreate() != null)
			getCreateButton().setId(getId() + "#create-button-" + getNavigateToCreate());
		if(getNavigateToAdd() != null)
			getAddButton().setId(getId() + "#add-button-" + getNavigateToAdd());
		if(getNavigateToRead() != null)
			getReadButton().setId(getId() + "#read-button-" + getNavigateToRead());
		getDeleteButton().setId(getId() + "#delete-button");
	}

	/**
	 * Sets the visibility of the form.
	 */
    public void setFormVisible(boolean formVisible) {
        getForm().setVisible(formVisible);
    }

	/**
	 * Configures the create button.
	 */
    private void configureCreateButton() {
        createButton.addClickListener(clickEvent -> {
        	performClick(getCreateNavigation());
        });
    }

	/**
	 * Configures the add button.
	 */
    private void configureAddButton() {
        addButton.addClickListener(clickEvent -> {
        	performClick(getAddNavigation());
        });
    }
    
    private void performClick(NavigateActions navigateAction){
		if (wohnungenForm.isVisible()) {
			ConfirmationWindow window = new ConfirmationWindow(SimpleAction.override);
			window.addActionPerformer(navigateAction::navigate);
			getUI().addWindow(window);
			window.center();
			window.focus();
		} else {
			navigateAction.navigate();
		}
	}
	
	/**
	 * Configures the read button.
	 */
    private void configureReadButton() {
        final Wohnung_SingleActions singleActions = new Wohnung_SingleActions(getForm()::getWohnung);
        readButton.addActionPerformer(singleActions::read);
        readButton.addActionPerformer(getReadNavigation()::navigate);
    }

    /**
	 * Configures the delete button.
	 */
    private void configureDeleteButton() {
        final Buerger_AssociationActions associationActions = new Buerger_AssociationActions(
                () -> new Association<>(getForm().getWohnung(), Buerger_.Rel.wohnungen.name()));
        deleteButton.addActionPerformer(associationActions::removeAssociation);
        
        deleteButton.useNotification(true);
    }

    public Component addComponent(Component component){
    	getButtonsInForm().addComponent(component);
    	return this;
    }

	// Getters
    public Buerger_Wohnungen_Form getForm() {
        return wohnungenForm;
    }
    
    public HorizontalLayout getButtons(){
    	return buttonsAlwaysVisible;
    }
    
    public HorizontalLayout getButtonsInForm(){
    	return buttonsInForm;
    }
    
	public ActionButton getCreateButton(){
		return createButton;
	}
	
	public ActionButton getAddButton(){
		return addButton;
	}
	
	public ActionButton getReadButton(){
		return readButton;
	}
	
	public ActionButton getDeleteButton(){
		return deleteButton;
	}
	
	public NavigateActions getCreateNavigation(){
		return createNavigation;
	}
	
	public NavigateActions getReadNavigation(){
		return readNavigation;
	}
	
	public NavigateActions getAddNavigation(){
		return addNavigation;
	}
	
	public String getNavigateToCreate() {
		return createNavigation.getNavigateTo();
	}
	
	public String getNavigateToRead() {
		return readNavigation.getNavigateTo();
	}
	
	public String getNavigateToAdd() {
		return addNavigation.getNavigateTo();
	}
	
	public Component addButton(ActionButton button){
    	buttonsAlwaysVisible.addComponent(button);
    	return this;
    }
}

