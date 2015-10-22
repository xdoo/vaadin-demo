package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerForm;

/**
 * Created by p.mueller on 22.10.15.
 */
public class BuergerRWForm extends BaseComponent {

    public static final boolean DEFAULT_MODE = false;

    private final SelectedBuergerForm buergerForm;
    private final HorizontalLayout buttons = new HorizontalLayout();
    private final HorizontalLayout editButtons = new HorizontalLayout();
    private final String navigateBack;
    private boolean edit;

    public BuergerRWForm(EntityController entityController, final String navigateBack) {
        super(entityController);
        this.navigateBack = navigateBack;


        buergerForm = new SelectedBuergerForm(entityController);
        init();
    }

    private void init() {
        getButtons().setSpacing(true);
        getButtons().addComponents(createButtons());
        getEditButtons().setSpacing(true);
        getEditButtons().addComponents(createEditButtons());

        getBuergerForm().getFormLayout().addComponents(getButtons(), getEditButtons());

        setEdit(DEFAULT_MODE);
        setCompositionRoot(getBuergerForm());
    }

    private Component[] createEditButtons() {
        final ActionButton saveButton = new ActionButton(getI18nResolver(), SimpleAction.save);

        final BuergerSingleActions singleActions = new BuergerSingleActions(getI18nResolver(), getBuergerForm()::getBuerger);
        saveButton.addActionPerformer(singleActions::update);
        saveButton.addActionPerformer(clickEvent -> {
            setEdit(false);
            return true;
        });

        final ActionButton cancelButton = new ActionButton(getI18nResolver(), SimpleAction.cancel);

        cancelButton.addActionPerformer(singleActions::reRead);
        cancelButton.addActionPerformer(clickEvent -> {
            setEdit(false);
            return true;
        });

        return new Component[]{cancelButton, saveButton};
    }

    private Component[] createButtons() {
        final ActionButton editButton = new ActionButton(getI18nResolver(), SimpleAction.update);
        editButton.addClickListener(clickEvent -> setEdit(true));


        final ActionButton backButton = new ActionButton(getI18nResolver(), SimpleAction.back);
        final NavigateActions navigateActions = new NavigateActions(getNavigateBack());
        backButton.addActionPerformer(navigateActions::navigate);

        return new Component[]{backButton, editButton};
    }

    public HorizontalLayout getButtons() {
        return buttons;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;

        getButtons().setVisible(!isEdit());
        getEditButtons().setVisible(isEdit());
        getBuergerForm().setReadOnly(!isEdit());
    }

    public HorizontalLayout getEditButtons() {
        return editButtons;
    }

    public SelectedBuergerForm getBuergerForm() {
        return buergerForm;
    }

    public String getNavigateBack() {
        return navigateBack;
    }
}
