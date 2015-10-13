package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.node.BuergerForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import java.util.Optional;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;

/**
 * Provides a simple Form for creating a new Buerger or a Buerger as an Association.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class BuergerCreateForm extends BuergerForm {

    /**
     * The String to navigate to on the create button.
     */
    private final String navigateTo;
    /**
     * The String to navigate to on the back button.
     */
    private final String back;
    /** The Controller for Buergers. */
    private final BuergerViewController controller;
    /**
     * The optional relation this CreateForm is for.
     */
    private final Optional<String> relation;

    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'abbrechen' Schaltfläche erstellt werden.
     * Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in eine
     * definierte Abfolge von Formularen eingebettet wird.
     *
     * @param controller der Entity Controller
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     * @param back       Zielseite nach Druck der 'abbrechen' Schaltfläche
     * @param relation   Optionale Angabe einer Assoziation, für die der Buerger ist.
     */
    public BuergerCreateForm(final BuergerViewController controller, final String navigateTo, final String back, final String relation) {
        super(controller, controller.getEventbus());

        this.navigateTo = navigateTo;
        this.back = back;
        this.controller = controller;
        this.relation = Optional.ofNullable(relation);

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

        final ActionButton createButton = createCreateButton();
        buttons.addComponent(createButton);

        getFormLayout().addComponent(buttons);
    }

    /**
     * Create the Button for the Create Action.
     * @return The create Button.
     */
    private ActionButton createCreateButton() {
        final ActionButton createButton = new ActionButton(getI18nResolver(), SimpleAction.create);
        if (getRelation().isPresent()) {
            final BuergerAssociationActions buergerAssociationActions = new BuergerAssociationActions(
                    () -> new Association<>(getBuerger(), getRelation().get()),
                    getEventBus());

            createButton.addClickListener(buergerAssociationActions::addAssociation);
        } else {
            BuergerSingleActions buergerSingleActions = new BuergerSingleActions(this::getBuerger, getEventBus());
            createButton.addClickListener(buergerSingleActions::create);
        }


        final NavigateActions navigateActions = new NavigateActions(controller.getNavigator(), controller.getEventbus(), getNavigateTo());
        createButton.addClickListener(navigateActions::navigate);

        return createButton;
    }

    /**
     * Create the back Button.
     * @return The back button.
     */
    private ActionButton createBackButton() {
        ActionButton backButton = new ActionButton(controller, SimpleAction.back);

        final NavigateActions navigateActions = new NavigateActions(controller.getNavigator(), controller.getEventbus(), getNavigateBack());
        backButton.addClickListener(navigateActions::navigate);

        return backButton;
    }

    /**
     * Create the Headline for the form.
     * @return The Headline Label.
     */
    private Label createHeadline() {
        final Label headline = new Label(getI18nResolver().resolveRelative(getFormPath(SimpleAction.create, I18nPaths.Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        return headline;
    }

    /**
     * Get the String representation of the view the form navigates to.
     * @return The navigate to String.
     */
    public String getNavigateTo() {
        return navigateTo;
    }

    /**
     * Get the Controller of this Component.
     * @return The Controller,
     */
    public BuergerViewController getController() {
        return controller;
    }

    /**
     * Get the String representation of the view the back button of the form navigates to.
     * @return The navigate back String.
     */
    public String getNavigateBack() {
        return back;
    }

    /**
     * Get the Relation this CreateForm is for.
     * @return The optional relation.
     */
    public Optional<String> getRelation() {
        return relation;
    }
}
