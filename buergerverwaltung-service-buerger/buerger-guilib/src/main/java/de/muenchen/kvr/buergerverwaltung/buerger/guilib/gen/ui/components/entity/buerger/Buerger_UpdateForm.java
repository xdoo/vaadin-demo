package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.buerger;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Field;

import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;

/**
 * Provides a simple update form for the current selected Buerger_.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Buerger_UpdateForm extends BaseComponent {
	/** Indicates the mode of the form. */
    private static final boolean READ_ONLY = false;

	/** The underlying form. */
    private final Buerger_SelectedForm buergerForm;

	/** The layout for all Buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    
    /** The button for the update action. */
    private final NavigateActions saveNavigation;
    
    /** The button for the save action. */
    private final ActionButton saveButton = new ActionButton(Buerger_.class, SimpleAction.save);


    public Buerger_UpdateForm(final String navigateToSaved) {
        saveNavigation = new NavigateActions(navigateToSaved);
        buergerForm = new Buerger_SelectedForm();
        buergerForm.reLoad();

        init();
        setIds();
    }

	/**
	 * Initialize the ReadForm.
	 */
    private void init() {
        getForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getSaveButton());

        configureSaveButton();

        getForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getForm());

        getForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }
    
    /**
	 * Set the IDs for important components.
	 */
	private void setIds() {
		setId(getClass().getSimpleName());
		getForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
		getForm().setId(getId() + "#form");
		getSaveButton().setId(getId() + "#save-button-" + getSaveNavigation().getNavigateTo());
	}

    private void configureSaveButton() {
        final Buerger_SingleActions singleActions = new Buerger_SingleActions(getForm()::getBuerger);
        getSaveButton().addActionPerformer(singleActions::update);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
        
        getSaveButton().useNotification(true);
        getSaveButton().setNotifyAction(SimpleAction.update);
    }

	// Getters
    public Buerger_SelectedForm getForm() {
        return buergerForm;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public ActionButton getSaveButton() {
        return saveButton;
    }

    public NavigateActions getSaveNavigation() {
        return saveNavigation;
    }
    
    public Component addButton(ActionButton button){
    	buttonLayout.addComponent(button);
    	return this;
    }
}
