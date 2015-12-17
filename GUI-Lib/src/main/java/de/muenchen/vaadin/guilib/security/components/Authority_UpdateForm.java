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
 * Provides a simple update form for the current selected Authority_.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Authority_UpdateForm extends BaseComponent {
    private static final boolean READ_ONLY = false;

    private final Authority_SelectedForm authorityForm;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final NavigateActions saveNavigation;
    private final ActionButton saveButton = new ActionButton(Authority_.class, SimpleAction.save);


    public Authority_UpdateForm(final String navigateToSaved) {
        saveNavigation = new NavigateActions(navigateToSaved);
        authorityForm = new Authority_SelectedForm();
        authorityForm.reLoad();

        init();
    }

    private void init() {
        getAuthorityForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getSaveButton());

        configureSaveButton();

        getAuthorityForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getAuthorityForm());

        getAuthorityForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }

    public Authority_SelectedForm getAuthorityForm() {
        return authorityForm;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public ActionButton getSaveButton() {
        return saveButton;
    }

    private void configureSaveButton() {
        final Authority_SingleActions singleActions = new Authority_SingleActions(getAuthorityForm()::getAuthority);
        getSaveButton().addActionPerformer(singleActions::update);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
    }

    public NavigateActions getSaveNavigation() {
        return saveNavigation;
    }
}
