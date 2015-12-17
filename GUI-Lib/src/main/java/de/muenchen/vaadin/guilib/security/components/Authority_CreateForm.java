package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.Authority_SingleActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * Provides a simple Form for creating a new Authority_.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class Authority_CreateForm extends BaseComponent {
    private static final boolean READ_ONLY = false;

    private final Authority_Form authorityForm;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final NavigateActions saveNavigation;
    private final ActionButton saveButton = new ActionButton(Authority_.class, SimpleAction.save);

	/**
	 * Create a new Authority_CreateForm that navigates to the navigateTo View on save.
	 *
	 * @param navigateTo The String of the view to navigate to on save.
	 */
    public Authority_CreateForm(final String navigateTo) {
        this.saveNavigation = new NavigateActions(navigateTo);
        authorityForm = new Authority_Form();
        init();
    }

    /**
     * Build the basic layout and insert the headline and all Buttons.
     */
    private void init() {
        getAuthorityForm().setReadOnly(READ_ONLY);


        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getSaveButton());

        configureSaveButton();

        getAuthorityForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getAuthorityForm());
        getAuthorityForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }
    
    /**
	 * Set the IDs for important components.
	 */
	private void setIds() {
		setId(getClass().getSimpleName());
		getAuthorityForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
		getAuthorityForm().setId(getId() + "#form");
		getSaveButton().setId(getId() + "#save-button-" + getSaveNavigation().getNavigateTo());
	}
	
	/**
	 * Configures the action the save button performs after beeing clicked.
	 */
	private void configureSaveButton() {
        Authority_SingleActions authoritySingleActions = new Authority_SingleActions(getAuthorityForm()::getAuthority);
        getSaveButton().addActionPerformer(authoritySingleActions::create);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
    }

    public Authority_Form getAuthorityForm() {
        return authorityForm;
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
}
