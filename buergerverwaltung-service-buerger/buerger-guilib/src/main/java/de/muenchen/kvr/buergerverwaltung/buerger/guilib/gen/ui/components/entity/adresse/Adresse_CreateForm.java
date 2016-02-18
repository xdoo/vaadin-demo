package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.adresse;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import java.util.stream.Collectors;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Adresse_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.adresse.Adresse_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.adresseIntern.AdresseIntern_Form;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.adresseExtern.AdresseExtern_Form;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * Provides a simple Form for creating a new Adresse_.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class Adresse_CreateForm extends Adresse_Form {
	/** Indicates the mode of the form. */
    private static final boolean READ_ONLY = false;

	/** The layout for all Buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    
    /** The navigation for the save aciton. */
    private final NavigateActions saveNavigation;
    
    /** The button for the save action. */
    private final ActionButton saveButton = new ActionButton(Adresse_.class, SimpleAction.save);
    
    /**
     * Form of the interneAdresse of this Adresse.
     */
    private final AdresseIntern_Form interneAdresse_Form = new AdresseIntern_Form();
    
    /**
     * Form of the externeAdresse of this Adresse.
     */
    private final AdresseExtern_Form externeAdresse_Form = new AdresseExtern_Form();
    
	/**
	 * Create a new Adresse_CreateForm that navigates to the navigateTo View on save.
	 *
	 * @param navigateTo The String of the view to navigate to on save.
	 */
    public Adresse_CreateForm(final String navigateTo) {
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

		getFormLayout().addComponent(interneAdresse_Form);
		getFormLayout().addComponent(externeAdresse_Form);
		
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
        Adresse_SingleActions adresseSingleActions = new Adresse_SingleActions(this::getAdresse);
        getSaveButton().addActionPerformer(adresseSingleActions::create);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
        
        getSaveButton().useNotification(true);
    }
    

	// Getters
	@Override
	public Adresse_ getAdresse(){
		Adresse_ adresse=super.getAdresse();
		adresse.setInterneAdresse(interneAdresse_Form.getAdresseIntern());
		adresse.setExterneAdresse(externeAdresse_Form.getAdresseExtern());
		return adresse;
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
