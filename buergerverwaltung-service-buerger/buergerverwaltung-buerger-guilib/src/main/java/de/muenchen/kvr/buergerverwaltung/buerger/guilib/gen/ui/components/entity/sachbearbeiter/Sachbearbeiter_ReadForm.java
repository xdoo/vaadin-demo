package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.sachbearbeiter;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.sachbearbeiter.Sachbearbeiter_SingleActions;

/**
 * @author claus
 */
public class Sachbearbeiter_ReadForm extends BaseComponent {
	/** Indicates the mode of the form. */
	public static final boolean READ_ONLY = true;

    /** The underlying form. */
    private final Sachbearbeiter_SelectedForm sachbearbeiterForm;

	/** The layout for all Buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    
    /** The button for the update action. */
    private final ActionButton updateButton = new ActionButton(Sachbearbeiter_.class, SimpleAction.update);

	/** The navigation for the update aciton. */
    private final NavigateActions updateNavigation;

    public Sachbearbeiter_ReadForm(final String navigateToUpdate) {
        sachbearbeiterForm = new Sachbearbeiter_SelectedForm();
        sachbearbeiterForm.reLoad();
		
	   	updateNavigation = (navigateToUpdate==null ? null : new NavigateActions(navigateToUpdate));
        
        init();
        setIds();
    }

	/**
	 * Build the basic layout and insert the headline and all Buttons.
	 */
    private void init() {
        getForm().setReadOnly(READ_ONLY);
		
        getButtonLayout().setSpacing(true);
		
		if(getUpdateNavigation()!=null){
        	configureUpdateButton();
			getButtonLayout().addComponents(getUpdateButton());
		}
		
        getForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getForm());
    }

    /**
	 * Set the IDs for important components.
	 */
	private void setIds() {
		setId(getClass().getSimpleName());
		getForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
		getForm().setId(getId() + "#form");
		if(getUpdateNavigation()!=null)
			getUpdateButton().setId(getId() + "#update-button-" + getNavigateToUpdate());
	}

    private void configureUpdateButton() {
        final Sachbearbeiter_SingleActions singleActions = new Sachbearbeiter_SingleActions(getForm()::getSachbearbeiter);
        getUpdateButton().addActionPerformer(singleActions::read);
        getUpdateButton().addActionPerformer(getUpdateNavigation()::navigate);
    }

    public Component addButton(ActionButton button){
    	buttonLayout.addComponent(button);
    	return this;
    }

    // Getters
    public Sachbearbeiter_SelectedForm getForm() {
        return sachbearbeiterForm;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public ActionButton getUpdateButton() {
        return updateButton;
    }

    public NavigateActions getUpdateNavigation() {
        return updateNavigation;
    }
    
    public String getNavigateToUpdate() {
        return (updateNavigation==null ? null : updateNavigation.getNavigateTo());
    }
}
