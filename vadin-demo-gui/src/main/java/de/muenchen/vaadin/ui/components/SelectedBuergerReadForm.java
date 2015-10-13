package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;

/**
 * @author claus
 */
public class SelectedBuergerReadForm extends SelectedBuergerForm {

    private final BuergerViewController controller;
    private final String navigateToUpdate;
    private final String navigateBack;

    /**
     * Formular zum Lesen eines {@link Buerger}s. Über diesen Konstruktor kann zusätzlich eine Zielseite für die
     * 'zurück' und 'bearbeiten' Schaltflächen erstellt werden.
     *
     * @param controller
     * @param navigateToUpdate
     * @param navigateBack
     */
    public SelectedBuergerReadForm(BuergerViewController controller, final String navigateToUpdate, String navigateBack) {
        super(controller, controller.getEventbus());
        setReadOnly(true);

        this.controller = controller;
        this.navigateToUpdate = navigateToUpdate;
        this.navigateBack = navigateBack;

        init();
    }


    /**
     * Build the basic layout and insert the headline and all Buttons.
     */
    private void init() {
        final Label headline = createHeadline();
        getFormLayout().addComponent(headline, 0);

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        final ActionButton backButton = createBackButton();
        buttons.addComponent(backButton);

        final ActionButton updateButton = createUpdateButton();
        buttons.addComponent(updateButton);

        getFormLayout().addComponent(buttons);
    }

    public BuergerViewController getController() {
        return controller;
    }

    public String getNavigateToUpdate() {
        return navigateToUpdate;
    }

    private ActionButton createBackButton() {
        final ActionButton backButton = new ActionButton(getI18nResolver(), SimpleAction.back);

        final NavigateActions navigateActions = new NavigateActions(getController().getNavigator(), getController().getEventbus(), getNavigateBack());
        backButton.addClickListener(navigateActions::navigate);

        return backButton;
    }


    /**
     * Create the Button for the Save Action.
     *
     * @return The save Button.
     */
    private ActionButton createUpdateButton() {
        final ActionButton updateButton = new ActionButton(getI18nResolver(), SimpleAction.update);

        final BuergerSingleActions buergerSingleActions = new BuergerSingleActions(this::getBuerger, getEventBus());
        updateButton.addClickListener(buergerSingleActions::read);

        final NavigateActions navigateActions = new NavigateActions(getController().getNavigator(), getController().getEventbus(), getNavigateToUpdate());
        updateButton.addClickListener(navigateActions::navigate);

        return updateButton;
    }


    private Label createHeadline() {
        final Label headline = new Label(getI18nResolver().resolveRelative(getFormPath(SimpleAction.read, Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        return headline;
    }

    public String getNavigateBack() {
        return navigateBack;
    }
}
