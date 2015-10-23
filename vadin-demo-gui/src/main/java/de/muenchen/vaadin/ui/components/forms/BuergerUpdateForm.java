package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.Field;
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
public class BuergerUpdateForm extends BaseComponent {

    private static final boolean READ_ONLY = false;

    private final SelectedBuergerForm buergerForm;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final NavigateActions saveNavigation;
    private final ActionButton saveButton = new ActionButton(getI18nResolver(), SimpleAction.save);


    public BuergerUpdateForm(EntityController entityController, final String navigateToSaved) {
        super(entityController);
        saveNavigation = new NavigateActions(navigateToSaved);
        buergerForm = new SelectedBuergerForm(entityController);

        init();
    }

    private void init() {
        getBuergerForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getSaveButton());

        configureSaveButton();

        getBuergerForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getBuergerForm());

        getBuergerForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }

    public SelectedBuergerForm getBuergerForm() {
        return buergerForm;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public ActionButton getSaveButton() {
        return saveButton;
    }

    private void configureSaveButton() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getI18nResolver(), getBuergerForm()::getBuerger);
        getSaveButton().addActionPerformer(singleActions::update);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
    }

    public NavigateActions getSaveNavigation() {
        return saveNavigation;
    }
}
