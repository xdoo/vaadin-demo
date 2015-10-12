package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.read.BuergerROForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;

/**
 * @author claus
 */
public class DemoBuergerReadForm extends BuergerROForm {

    private final BuergerViewController controller;
    private final String navigateToUpdate;
    private final String navigateBack;

    /**
     * Formular zum Lesen eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'zurück' und 'bearbeiten' Schaltflächen
     * erstellt werden.
     *
     * @param controller
     * @param navigateToUpdate
     * @param navigateBack
     */
    public DemoBuergerReadForm(BuergerViewController controller, final String navigateToUpdate, String navigateBack) {
        super(controller, controller.getEventbus());
        this.controller = controller;
        this.navigateToUpdate = navigateToUpdate;
        this.navigateBack = navigateBack;

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

    /**
     * Erzeugt das eigentliche Formular.
     */
//    private void createForm() {
//
//        FormLayout layout = new FormLayout();
//        HorizontalLayout buttonLayout = new HorizontalLayout();
//        buttonLayout.setSpacing(true);
//        layout.setMargin(true);
//
//        // headline
//        Label headline = new Label(controller.resolveRelative(getFormPath(SimpleAction.read, I18nPaths.Component.headline, Type.label)));
//        headline.addStyleName(ValoTheme.LABEL_H3);
//        layout.addComponent(headline);
//
//        layout.addComponent(controller.getUtil().createFormTextField(binder,
//                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.label)),
//                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.input_prompt)),
//                Buerger.Field.vorname.name(), BuergerViewController.I18N_BASE_PATH));
//        layout.addComponent(controller.getUtil().createFormTextField(binder,
//                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.label)),
//                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.input_prompt)),
//                Buerger.Field.nachname.name(), BuergerViewController.I18N_BASE_PATH));
//        layout.addComponent(controller.getUtil().createFormComboBox(binder,
//                controller.resolveRelative(getEntityFieldPath(Buerger.Field.augenfarbe.name(), Type.label)),
//                Buerger.Field.augenfarbe.name(),
//                Augenfarbe.class));
//        layout.addComponent(controller.getUtil().createFormDateField(
//                binder, controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.label)),
//                Buerger.Field.geburtsdatum.name(), BuergerViewController.I18N_BASE_PATH));
//
//        // auf 'read only setzen
//        this.binder.setReadOnly(true);
//        layout.addComponent(buttonLayout);
//        // die Schaltfläche zum Aktualisieren
//        ActionButton backButton = new ActionButton(controller, SimpleAction.back);
//        backButton.addClickListener(clickEvent -> controller.getNavigator().navigateTo(getNavigateBack()));
//        buttonLayout.addComponent(backButton);
//
//        // die Schaltfläche zum Bearbeiten
//        ActionButton updateButton = new ActionButton(controller, SimpleAction.update);
//        updateButton.addClickListener(clickEvent -> {
//            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(binder.getItemDataSource().getBean()));
//            controller.getNavigator().navigateTo(navigateToUpdate);
//        });
//        buttonLayout.addComponent(updateButton);
//        setCompositionRoot(layout);
//    }
/*
    @Override
    public void accept(reactor.bus.Event<BuergerDatastore> eventWrapper) {
        BuergerDatastore event = eventWrapper.getData();
        event.getSelectedBuerger().ifPresent(binder::setItemDataSource);
    }
*/
    public String getNavigateBack() {
        return navigateBack;
    }
}
