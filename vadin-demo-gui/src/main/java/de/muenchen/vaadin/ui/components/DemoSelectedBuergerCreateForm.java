package de.muenchen.vaadin.ui.components;

import com.vaadin.navigator.Navigator;
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
 * Formular zum Erstellen eines {@link Buerger}s.
 *
 * @author claus.straube
 */
public class DemoSelectedBuergerCreateForm extends BuergerForm {

    private final String navigateTo;
    private final String back;
    private final BuergerViewController controller;
    private Optional<String> relation;

    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'abbrechen' Schaltfläche erstellt werden.
     * Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in eine
     * definierte Abfolge von Formularen eingebettet wird.
     *
     * @param controller der Entity Controller
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     * @param back       Zielseite nach Druck der 'abbrechen' Schaltfläche
     */
    public DemoSelectedBuergerCreateForm(final BuergerViewController controller, final String navigateTo, String relation, String back) {
        super(controller, controller.getEventbus());


        this.navigateTo = navigateTo;
        this.back = back;
        this.controller = controller;
        this.relation = Optional.ofNullable(relation);

        init();

    }

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

    private ActionButton createBackButton() {
        ActionButton backButton = new ActionButton(controller, SimpleAction.back);

        final NavigateActions navigateActions = new NavigateActions(controller.getNavigator(), controller.getEventbus(), getNavigateBack());
        backButton.addClickListener(navigateActions::navigate);

        return backButton;
    }

    private Label createHeadline() {
        final Label headline = new Label(getI18nResolver().resolveRelative(getFormPath(SimpleAction.create, I18nPaths.Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        return headline;
    }

    public String getNavigateTo() {
        return navigateTo;
    }

    public BuergerViewController getController() {
        return controller;
    }

    public String getNavigateBack() {
        return back;
    }

    public Optional<String> getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = Optional.ofNullable(relation);
    }

    private Navigator getNavigator() {
        return getController().getNavigator();
    }
}
