package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.apilib.local.User_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.User_SingleActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * Provides a simple Form for creating a new User_.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class User_CreateForm extends BaseComponent {
    private static final boolean READ_ONLY = false;

    private final User_Form userForm;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final NavigateActions saveNavigation;
    private final ActionButton saveButton = new ActionButton(User_.class, SimpleAction.save);

	/**
	 * Create a new User_CreateForm that navigates to the navigateTo View on save.
	 *
	 * @param navigateTo The String of the view to navigate to on save.
	 */
    public User_CreateForm(final String navigateTo) {
        this.saveNavigation = new NavigateActions(navigateTo);
        userForm = new User_Form();
        init();
    }

    /**
     * Build the basic layout and insert the headline and all Buttons.
     */
    private void init() {
        getUserForm().setReadOnly(READ_ONLY);


        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getSaveButton());

        configureSaveButton();

        getUserForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getUserForm());
        getUserForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }
    
    /**
	 * Set the IDs for important components.
	 */
	private void setIds() {
		setId(getClass().getSimpleName());
		getUserForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
		getUserForm().setId(getId() + "#form");
		getSaveButton().setId(getId() + "#save-button-" + getSaveNavigation().getNavigateTo());
	}
	
	/**
	 * Configures the action the save button performs after beeing clicked.
	 */
	private void configureSaveButton() {
        User_SingleActions userSingleActions = new User_SingleActions(getUserForm()::getUser);
        getSaveButton().addActionPerformer(userSingleActions::create);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
    }

    public User_Form getUserForm() {
        return userForm;
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
