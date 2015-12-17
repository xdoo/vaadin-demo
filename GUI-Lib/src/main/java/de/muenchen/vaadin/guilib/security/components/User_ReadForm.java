package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.apilib.local.User_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.User_SingleActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * @author claus
 */
public class User_ReadForm extends BaseComponent {
	public static final boolean READ_ONLY = true;

    /** The underlying form. */
    private final User_SelectedForm userForm;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final ActionButton updateButton = new ActionButton(User_.class, SimpleAction.update);

    private final NavigateActions updateNavigation;

    public User_ReadForm(final String navigateToUpdate) {
        userForm = new User_SelectedForm();
        userForm.reLoad();

        updateNavigation = new NavigateActions(navigateToUpdate);
        init();
    }

    private void init() {
        getUserForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getUpdateButton());

        configureUpdateButton();

        getUserForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getUserForm());
    }

    public User_SelectedForm getUserForm() {
        return userForm;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public ActionButton getUpdateButton() {
        return updateButton;
    }


    private void configureUpdateButton() {
        final User_SingleActions singleActions = new User_SingleActions(getUserForm()::getUser);
        getUpdateButton().addActionPerformer(singleActions::read);
        getUpdateButton().addActionPerformer(getUpdateNavigation()::navigate);
    }


    public NavigateActions getUpdateNavigation() {
        return updateNavigation;
    }
}
