package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.buerger;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;

/**
 * @author claus
 */
public class Buerger_ReadForm extends BaseComponent {
	/** Indicates the mode of the form. */
	public static final boolean READ_ONLY = true;

    /** The underlying form. */
    private final Buerger_SelectedForm buergerForm;

	/** The layout for all Buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    
    /** The button for the update action. */
    private final ActionButton updateButton = new ActionButton(Buerger_.class, SimpleAction.update);

	/** The navigation for the update aciton. */
    private final NavigateActions updateNavigation;

    public Buerger_ReadForm(final String navigateToUpdate) {
        buergerForm = new Buerger_SelectedForm();
        buergerForm.reLoad();

        updateNavigation = new NavigateActions(navigateToUpdate);
        init();
        setIds();
    }

	/**
	 * Build the basic layout and insert the headline and all Buttons.
	 */
    private void init() {
        getForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getUpdateButton());

        configureUpdateButton();

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
		getUpdateButton().setId(getId() + "#update-button-" + getUpdateNavigation().getNavigateTo());
	}

    private void configureUpdateButton() {
        final Buerger_SingleActions singleActions = new Buerger_SingleActions(getForm()::getBuerger);
        getUpdateButton().addActionPerformer(singleActions::read);
        getUpdateButton().addActionPerformer(getUpdateNavigation()::navigate);
    }
    
    // Getters
    public Buerger_SelectedForm getForm() {
        return buergerForm;
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
    
    public Component addButton(ActionButton button){
    	buttonLayout.addComponent(button);
    	return this;
    }
}
