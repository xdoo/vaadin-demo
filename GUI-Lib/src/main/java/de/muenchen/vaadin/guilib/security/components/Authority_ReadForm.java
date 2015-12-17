package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.Authority_SingleActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * @author claus
 */
public class Authority_ReadForm extends BaseComponent {
	public static final boolean READ_ONLY = true;

    /** The underlying form. */
    private final Authority_SelectedForm authorityForm;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final ActionButton updateButton = new ActionButton(Authority_.class, SimpleAction.update);

    private final NavigateActions updateNavigation;

    public Authority_ReadForm(final String navigateToUpdate) {
        authorityForm = new Authority_SelectedForm();
        authorityForm.reLoad();

        updateNavigation = new NavigateActions(navigateToUpdate);
        init();
    }

    private void init() {
        getAuthorityForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getUpdateButton());

        configureUpdateButton();

        getAuthorityForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getAuthorityForm());
    }

    public Authority_SelectedForm getAuthorityForm() {
        return authorityForm;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public ActionButton getUpdateButton() {
        return updateButton;
    }


    private void configureUpdateButton() {
        final Authority_SingleActions singleActions = new Authority_SingleActions(getAuthorityForm()::getAuthority);
        getUpdateButton().addActionPerformer(singleActions::read);
        getUpdateButton().addActionPerformer(getUpdateNavigation()::navigate);
    }


    public NavigateActions getUpdateNavigation() {
        return updateNavigation;
    }
}
