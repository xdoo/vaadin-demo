package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerPartnerForm;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;

/**
 * Created by p.mueller on 13.10.15.
 */
public class SelectedBuergerPartnerReadForm extends SelectedBuergerPartnerForm {

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
    public SelectedBuergerPartnerReadForm(EntityController controller, final String navigateToUpdate, String navigateBack) {
        super(controller);
        setReadOnly(true);

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

    public String getNavigateToUpdate() {
        return navigateToUpdate;
    }

    private ActionButton createBackButton() {
        final ActionButton backButton = new ActionButton(getI18nResolver(), SimpleAction.back);

        final NavigateActions navigateActions = new NavigateActions(getNavigator(), getEventBus(), getNavigateBack());
        backButton.addActionPerformer(navigateActions::navigate);

        return backButton;
    }


    /**
     * Create the Button for the Save Action.
     *
     * @return The save Button.
     */
    private ActionButton createUpdateButton() {
        final ActionButton updateButton = new ActionButton(getI18nResolver(), SimpleAction.update);

        final BuergerSingleActions buergerSingleActions = new BuergerSingleActions(getI18nResolver(), this::getBuerger, getEventBus());
        updateButton.addActionPerformer(buergerSingleActions::read);

        final NavigateActions navigateActions = new NavigateActions(getNavigator(), getEventBus(), getNavigateToUpdate());
        updateButton.addActionPerformer(navigateActions::navigate);

        return updateButton;
    }


    private Label createHeadline() {
        final Label headline = new Label(getI18nResolver().resolveRelative(getFormPath(SimpleAction.read, I18nPaths.Component.headline, I18nPaths.Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        return headline;
    }

    public String getNavigateBack() {
        return navigateBack;
    }
}
