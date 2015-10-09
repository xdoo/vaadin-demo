package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.buttons.node.BuergerCreateButton;
import de.muenchen.vaadin.ui.components.buttons.node.BuergerSaveButton;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.NavigateActions;
import de.muenchen.vaadin.ui.components.forms.read.BuergerReadForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import javax.annotation.PostConstruct;

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
    
    public DefaultBuergerView(BuergerViewController controller, MainUI ui) {
        this.controller = controller;
        this.controller.registerUI(ui);
    }
    
    
    /**
     * 
     */
    @PostConstruct
    private void postConstruct() {
        //TODO Wirklich notwendig? Lieber enter() verwenden?
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
        Label pageTitle = new Label(controller.resolveRelative(getPagePath(Type.title)));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);

        removeAllComponents();
        //HorizontalLayout head = new HorizontalLayout(pageTitle);
        addComponent(pageTitle);

//        BuergerReadForm readForm = new BuergerReadForm(controller,controller.getEventbus());
        BuergerReadForm form = new BuergerReadForm(controller, controller.getEventbus());
        addComponent(form);
        final BuergerCreateButton c = new BuergerCreateButton(controller, controller.getEventbus());
        c.setBuergerSupplier(form::getBuerger);
        addComponent(c);
        final BuergerSaveButton update = new BuergerSaveButton(controller, controller.getEventbus());
        update.setBuergerSupplier(form::getBuerger);
        addComponent(update);


        final ActionButton asdf = new ActionButton(controller, SimpleAction.back);

        final BuergerActions buergerAction = new BuergerActions(form::getBuerger, controller.getEventbus());
        final NavigateActions navigateActions = new NavigateActions(controller.getNavigator(), BuergerDetailView.NAME);

        asdf.addClickListener(buergerAction::create);
        asdf.addClickListener(buergerAction::read);
        asdf.addClickListener(navigateActions::navigate);

        addComponent(asdf);
    }
    
    /**
     *
     */
    protected abstract void site();
    
}
