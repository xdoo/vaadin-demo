package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.pass;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.pass.Pass_SingleActions;

/**
 * @author claus
 */
public class Pass_ReadForm extends BaseComponent {
	/** Indicates the mode of the form. */
	public static final boolean READ_ONLY = true;

    /** The underlying form. */
    private final Pass_SelectedForm passForm;

	/** The layout for all Buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    
    /** The button for the update action. */
    private final ActionButton updateButton = new ActionButton(Pass_.class, SimpleAction.update);

	/** The navigation for the update aciton. */
    private final NavigateActions updateNavigation;

    public Pass_ReadForm(final String navigateToUpdate) {
        passForm = new Pass_SelectedForm();
        passForm.reLoad();
		
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
        final Pass_SingleActions singleActions = new Pass_SingleActions(getForm()::getPass);
        getUpdateButton().addActionPerformer(singleActions::read);
        getUpdateButton().addActionPerformer(getUpdateNavigation()::navigate);
    }

    public Component addButton(ActionButton button){
    	buttonLayout.addComponent(button);
    	return this;
    }

    // Getters
    public Pass_SelectedForm getForm() {
        return passForm;
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
