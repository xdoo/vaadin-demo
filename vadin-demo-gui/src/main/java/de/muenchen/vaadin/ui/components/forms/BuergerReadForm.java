package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerForm;

/**
 * Created by p.mueller on 23.10.15.
 */
public class BuergerReadForm extends BaseComponent {
    public static final boolean READ_ONLY = true;

    /** The underlying form. */
    private final SelectedBuergerForm buergerForm;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final ActionButton updateButton = new ActionButton(getI18nResolver(), SimpleAction.update);

    private final NavigateActions updateNavigation;

    public BuergerReadForm(EntityController entityController, final String navigateToUpdate) {
        super(entityController);
        buergerForm = new SelectedBuergerForm(entityController);
        updateNavigation = new NavigateActions(navigateToUpdate);
        init();
    }

    private void init() {
        getBuergerForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getUpdateButton());

        configureUpdateButton();

        getBuergerForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getBuergerForm());
    }

    public SelectedBuergerForm getBuergerForm() {
        return buergerForm;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public ActionButton getUpdateButton() {
        return updateButton;
    }


    private void configureUpdateButton() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getI18nResolver(), getBuergerForm()::getBuerger);
        getUpdateButton().addActionPerformer(singleActions::read);
        getUpdateButton().addActionPerformer(getUpdateNavigation()::navigate);
    }


    public NavigateActions getUpdateNavigation() {
        return updateNavigation;
    }
}
