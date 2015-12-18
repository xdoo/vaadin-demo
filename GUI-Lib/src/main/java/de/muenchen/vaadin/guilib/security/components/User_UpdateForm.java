package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.apilib.local.User;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.User_SingleActions;

/**
 * Provides a simple update form for the current selected User_.
 *
 * @author p.mueller
 * @version 1.0
 */
public class User_UpdateForm extends BaseComponent {
    private static final boolean READ_ONLY = false;

    private final User_SelectedForm userForm;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final NavigateActions saveNavigation;
    private final ActionButton saveButton = new ActionButton(User.class, SimpleAction.save);


    public User_UpdateForm(final String navigateToSaved) {
        saveNavigation = new NavigateActions(navigateToSaved);
        userForm = new User_SelectedForm();
        userForm.reLoad();

        init();
    }

    private void init() {
        getUserForm().setReadOnly(READ_ONLY);
        getUserForm().getFields().get(0).setReadOnly(true);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getSaveButton());

        configureSaveButton();

        getUserForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getUserForm());

        getUserForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }

    public User_SelectedForm getUserForm() {
        return userForm;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public ActionButton getSaveButton() {
        return saveButton;
    }

    private void configureSaveButton() {
        final User_SingleActions singleActions = new User_SingleActions(getUserForm()::getUser);
        getSaveButton().addActionPerformer(singleActions::update);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
    }

    public NavigateActions getSaveNavigation() {
        return saveNavigation;
    }
}
