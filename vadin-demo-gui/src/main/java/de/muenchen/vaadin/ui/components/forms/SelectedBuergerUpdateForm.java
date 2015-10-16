package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerForm;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;

/**
 * Provides a simple update form for the current selected buerger.
 *
 * @author p.mueller
 * @version 1.0
 */
public class SelectedBuergerUpdateForm extends SelectedBuergerForm {
    /** The String to navigate to on the save button. */
    private final String navigateTo;
    /** The String to navigate to on the back button. */
    private final String navigateBack;

    /**
     * Formular zum Bearbeiten eines {@link Buerger}s. Über diesen Konstruktor kann zusätzlich eine Zielseite für die
     * 'abbrechen' Schaltfläche erstellt werden. Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in
     * eine definierte Abfolge von Formularen eingebettet wird.
     *
     * @param entityController The controller used for everything.
     * @param navigateTo
     * @param navigateBack
     */
    public SelectedBuergerUpdateForm(final EntityController entityController, final String navigateTo, final String navigateBack) {
        super(entityController);

        this.navigateTo = navigateTo;
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

        final ActionButton updateButton = createSaveButton();
        buttons.addComponent(updateButton);

        getFormLayout().addComponent(buttons);
    }

    /**
     * Create the Button for the Save Action.
     *
     * @return The save Button.
     */
    private ActionButton createSaveButton() {
        final ActionButton saveButton = new ActionButton(getI18nResolver(), SimpleAction.save);

        final BuergerSingleActions buergerSingleActions = new BuergerSingleActions(getI18nResolver(), this::getBuerger, getEventBus());
        saveButton.addActionPerformer(buergerSingleActions::update);

        final NavigateActions navigateActions = new NavigateActions(getNavigator(), getEventBus(), getNavigateTo());
        saveButton.addActionPerformer(navigateActions::navigate);

        return saveButton;
    }

    /**
     * Create the Button for the Back Action.
     *
     * @return The back Button.
     */
    private ActionButton createBackButton() {
        final ActionButton backButton = new ActionButton(getI18nResolver(), SimpleAction.back);

        final NavigateActions navigateActions = new NavigateActions(getNavigator(), getEventBus(), getNavigateBack());
        backButton.addActionPerformer(navigateActions::navigate);

        return backButton;
    }

    /**
     * Create the Headline for the form.
     *
     * @return The Headline Label.
     */
    private Label createHeadline() {
        final Label headline = new Label(getI18nResolver().resolveRelative(getFormPath(SimpleAction.update, I18nPaths.Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        return headline;
    }


    /**
     * Get the String representation of the view the back button of the form navigates to.
     *
     * @return The navigate back String.
     */
    public String getNavigateBack() {
        return navigateBack;
    }


    /**
     * Get the String representation of the view the form navigates to.
     *
     * @return The navigate to String.
     */
    public String getNavigateTo() {
        return navigateTo;
    }
}
