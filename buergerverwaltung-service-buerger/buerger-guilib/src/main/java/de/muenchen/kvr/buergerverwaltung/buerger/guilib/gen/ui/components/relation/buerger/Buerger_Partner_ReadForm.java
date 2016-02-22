package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.buerger;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;

/**
 * Provides a simple ReadForm for the Partner of a Buerger_.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Buerger_Partner_ReadForm extends BaseComponent {
	
    /** Indicates the mode of the form. */
    public static final boolean READ_ONLY = true;
    /** The underlying form. */
    private final Buerger_Partner_Form form = new Buerger_Partner_Form(){
			@Override
			public void setBuerger(Buerger_ entity) {
				getDetailButton().setVisible(entity != null);
				super.setBuerger(entity);
			}
		};
	
    /** The layout for all Buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    
    /** The button for the update action. */
    private final ActionButton detailButton = new ActionButton(Buerger_.class, SimpleAction.read);
    
    /** The navigation for the detail aciton. */
    private final NavigateActions readNavigation;

    /**
     * Create a new ReadForm for the Partner of a Buerger_. It will be empty when no Partner is present.
     *
     * @param navigateToRead The View to navigate to on detail button click.
     */
    public Buerger_Partner_ReadForm(String navigateToRead) {
        form.reLoad();
        readNavigation = new NavigateActions(navigateToRead);

        init();
        setIds();
    }
    
    /**
	 * Set the IDs for important components.
	 */
	private void setIds() {
		setId(getClass().getSimpleName());
		getForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
		getForm().setId(getId() + "#form");
		getDetailButton().setId(getId() + "#detail-button-" + getReadNavigation().getNavigateTo());
	}

    /**
     * Get the underlying Form.
     *
     * @return The Form used by this component.
     */
    public Buerger_Partner_Form getForm() {
        return form;
    }

    /**
     * Initialize the ReadForm.
     */
    private void init() {
        getForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getDetailButton());

        configureDetailButton();

        getForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getForm());
    }

    /**
     * Configure the Detail Button.
     */
    private void configureDetailButton() {
    	final Buerger_SingleActions singleActions = new Buerger_SingleActions(getForm()::getBuerger);
		getDetailButton().addActionPerformer(singleActions::read);
        getDetailButton().addActionPerformer(getReadNavigation()::navigate);
    }

    public Component addComponent(Component component){
    	getButtonLayout().addComponent(component);
    	return this;
    }

	// Getters
    /**
     * Get the layout for the Buttons.
     *
     * @return The horizontal Layout.
     */
    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    /**
     * Get the Detail Button.
     *
     * @return The button for the detail action.
     */
    public ActionButton getDetailButton() {
        return detailButton;
    }

    /**
     * Get the Navigation for the read action.
     *
     * @return The NavigateActions for read.
     */
    public NavigateActions getReadNavigation() {
        return readNavigation;
    }
    
    public Component addButton(ActionButton button){
    	buttonLayout.addComponent(button);
    	return this;
    }
}

