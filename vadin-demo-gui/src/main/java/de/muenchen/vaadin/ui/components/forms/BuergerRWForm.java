package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerForm;

/**
 * Created by p.mueller on 22.10.15.
 */
public class BuergerRWForm extends BaseComponent {

    public static final boolean DEFAULT_MODE = false;

    private final SelectedBuergerForm buergerForm;
    private final HorizontalLayout buttons = new HorizontalLayout();
    private final HorizontalLayout editButtons = new HorizontalLayout();
    private boolean edit;

    public BuergerRWForm(EntityController entityController) {
        super(entityController);


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
        saveButton.addClickListener(clickEvent -> setEdit(false));

        return new Component[]{saveButton};
    }

    private Component[] createButtons() {
        final ActionButton editButton = new ActionButton(getI18nResolver(), SimpleAction.update);
        editButton.addClickListener(clickEvent -> setEdit(true));

        return new Component[]{editButton};
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

}
