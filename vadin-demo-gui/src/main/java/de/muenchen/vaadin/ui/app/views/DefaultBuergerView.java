package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getPagePath;


/**
 * Für jede Entity existiert eine (voll generierte) Basis Klasse. Aus dieser
 * leiten sich alle anderen Views ab.
 * 
 * @author claus.straube
 */
public abstract class DefaultBuergerView extends VerticalLayout implements View{

    BuergerViewController controller;
    BuergerI18nResolver resolver;

    public DefaultBuergerView(BuergerViewController controller, BuergerI18nResolver resolver, MainUI ui) {
        this.controller = controller;
        this.resolver = resolver;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.configureLayout();

        // add some components
        this.addHeadline();
        this.site();
    }

    private void configureLayout() {
        setSizeFull();
        this.setHeightUndefined();
        setMargin(new MarginInfo(false, true, false, true));
    }

    protected void addHeadline() {

        // headline
        Label pageTitle = new Label(resolver.resolveRelative(getPagePath(Type.title)));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);

        removeAllComponents();
        //HorizontalLayout head = new HorizontalLayout(pageTitle);
        addComponent(pageTitle);

        final NavigateActions navigateActions = new NavigateActions(BuergerTableView.NAME);
        final ActionButton aButton = new ActionButton(controller.getResolver(), SimpleAction.back);
        final NavigateActions navigateActions2 = new NavigateActions(BuergerDetailView.NAME);
        final ActionButton aButton2 = new ActionButton(controller.getResolver(), SimpleAction.back);
        aButton.addActionPerformer(navigateActions::navigate);
        aButton2.addActionPerformer(navigateActions2::navigate);
        addComponents(new HorizontalLayout(aButton, aButton2));
    }

    /**
     *
     */
    protected abstract void site();
    
}
