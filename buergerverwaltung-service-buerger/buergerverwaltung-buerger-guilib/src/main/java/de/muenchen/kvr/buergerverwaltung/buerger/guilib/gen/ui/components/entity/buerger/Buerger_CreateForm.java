package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.buerger;

import com.vaadin.data.Validator;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericGrid;
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
	 * Grid to select a required relation.
	 * relation: pass
	 * type:  Pass_
	 */
	 private final GenericGrid<Pass_> pass_Grid = new GenericGrid<>(Pass_.class, Pass_.Field.getProperties());
	 
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

		
		configurePass_Grid();
		getFormLayout().addComponent(pass_Grid);
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
    
    /**
     * Configures the grid used to select the pass of this Buerger
     */
    protected void configurePass_Grid(){
    	pass_Grid.setHeightByRows(5);
    	pass_Grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    	pass_Grid.setCaption(BaseUI.getCurrentI18nResolver().resolve("buerger.pass.label"));
    }

	// Getters
	@Override
	public Buerger_ getBuerger(){
		Buerger_ buerger=super.getBuerger();
		
		try{
			buerger.setPass(pass_Grid.getSelectedEntity().getId().getHref());
			pass_Grid.setComponentError(null);
		} catch(NoSuchElementException e) {
			pass_Grid.setComponentError(new UserError("No Pass selected"));
			throw new Validator.InvalidValueException("No Pass selected");
		}
		
		return buerger;
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
