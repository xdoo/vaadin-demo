package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.buerger;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * Provides a simple Form for creating a new Buerger_.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class Buerger_CreateForm extends Buerger_Form {
	/** Indicates the mode of the form. */
    private static final boolean READ_ONLY = false;

	/** The layout for all Buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    
    /** The navigation for the save aciton. */
    private final NavigateActions saveNavigation;
    
    /** The button for the save action. */
    private final ActionButton saveButton = new ActionButton(Buerger_.class, SimpleAction.save);
    
	/**
	 * Create a new Buerger_CreateForm that navigates to the navigateTo View on save.
	 *
	 * @param navigateTo The String of the view to navigate to on save.
	 */
    public Buerger_CreateForm(final String navigateTo) {
        this.saveNavigation = new NavigateActions(navigateTo);
        init();
        setIds();
    }

    /**
     * Build the basic layout and insert the headline and all Buttons.
     */
    protected void init() {
		setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getSaveButton());
		
        configureSaveButton();

        getFormLayout().addComponent(getButtonLayout());
		getFields().stream().findFirst().ifPresent(Field::focus);
    }
    
    /**
	 * Set the IDs for important components.
	 */
	protected void setIds() {
		setId(getClass().getSimpleName());
		getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
		setId(getId() + "#form");
		getSaveButton().setId(getId() + "#save-button-" + getSaveNavigation().getNavigateTo());
	}
	
	/**
	 * Configures the action the save button performs after beeing clicked.
	 */
	protected void configureSaveButton() {
        Buerger_SingleActions buergerSingleActions = new Buerger_SingleActions(this::getBuerger);
        getSaveButton().addActionPerformer(buergerSingleActions::create);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
        
        getSaveButton().useNotification(true);
    }
    

	// Getters
	@Override
	public Buerger_ getBuerger(){

		return super.getBuerger();
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
