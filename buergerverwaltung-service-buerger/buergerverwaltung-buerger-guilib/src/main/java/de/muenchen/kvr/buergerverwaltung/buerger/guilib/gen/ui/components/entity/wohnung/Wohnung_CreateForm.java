package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.wohnung;

import com.vaadin.data.Validator;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Adresse_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.wohnung.Wohnung_SingleActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * Provides a simple Form for creating a new Wohnung_.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class Wohnung_CreateForm extends Wohnung_Form {
	/** Indicates the mode of the form. */
    private static final boolean READ_ONLY = false;

	/** The layout for all Buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    
    /** The navigation for the save aciton. */
    private final NavigateActions saveNavigation;
    
    /** The button for the save action. */
    private final ActionButton saveButton = new ActionButton(Wohnung_.class, SimpleAction.save);
    
	/** 
	 * Grid to select a required relation.
	 * relation: adresse
	 * type: multiple Adresse_
	 */
	 private final GenericGrid<Adresse_> adresse_Grid = new GenericGrid<>(Adresse_.class, Adresse_.Field.getProperties());
	 
	/**
	 * Create a new Wohnung_CreateForm that navigates to the navigateTo View on save.
	 *
	 * @param navigateTo The String of the view to navigate to on save.
	 */
    public Wohnung_CreateForm(final String navigateTo) {
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

		
		configureAdresse_Grid();
		getFormLayout().addComponent(adresse_Grid);
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
        Wohnung_SingleActions wohnungSingleActions = new Wohnung_SingleActions(this::getWohnung);
        getSaveButton().addActionPerformer(wohnungSingleActions::create);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
        
        getSaveButton().useNotification(true);
    }
    
    /**
     * Configures the grid used to select the adresse of this Wohnung
     */
    protected void configureAdresse_Grid(){
    	adresse_Grid.setHeightByRows(5);
    	adresse_Grid.setSelectionMode(Grid.SelectionMode.MULTI);
    	adresse_Grid.setCaption(BaseUI.getCurrentI18nResolver().resolve("wohnung.adresse.label"));
    }

	// Getters
	@Override
	public Wohnung_ getWohnung(){
		Wohnung_ wohnung=super.getWohnung();
		
		try{
			wohnung.setAdresse(adresse_Grid.getSelectedEntities().stream().map(o -> o.getId().getHref()).collect(Collectors.toList()));
			adresse_Grid.setComponentError(null);
		} catch(NoSuchElementException e) {
			adresse_Grid.setComponentError(new UserError("No Adresse selected"));
			throw new Validator.InvalidValueException("No Adresse selected");
		}
		
		return wohnung;
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
