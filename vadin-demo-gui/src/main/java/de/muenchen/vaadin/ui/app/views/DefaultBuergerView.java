package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.EntityActions;
import de.muenchen.vaadin.guilib.components.actions.EntitySingleActions;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.buttons.node.BuergerCreateButton;
import de.muenchen.vaadin.ui.components.buttons.node.BuergerSaveButton;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.read.SelectedBuergerForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import javax.annotation.PostConstruct;
import java.util.List;

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

//        SelectedBuergerForm readForm = new SelectedBuergerForm(controller,controller.getEventbus());
        SelectedBuergerForm form = new SelectedBuergerForm(controller, controller.getEventbus());
        addComponent(form);
        final BuergerCreateButton c = new BuergerCreateButton(controller, controller.getEventbus());
        c.setBuergerSupplier(form::getBuerger);
        addComponent(c);
        final BuergerSaveButton update = new BuergerSaveButton(controller, controller.getEventbus());
        update.setBuergerSupplier(form::getBuerger);
        addComponent(update);



        final BuergerSingleActions buergerAction = new BuergerSingleActions(form::getBuerger, controller.getEventbus());
        final EntitySingleActions buergerActiun = new EntitySingleActions<>(form::getBuerger, controller.getEventbus(), Buerger.class);

        final NavigateActions navigateActions = new NavigateActions(controller.getNavigator(), controller.getEventbus(), BuergerDetailView.NAME);
        final EntityActions entityActions = new EntityActions(controller.getEventbus(), Buerger.class);

        final ActionButton asdf = new ActionButton(controller, SimpleAction.back);

        asdf.addClickListener(buergerAction::create);
        asdf.addClickListener(entityActions::readList);
        asdf.addClickListener(navigateActions::navigate);

        addComponent(asdf);
    }

    private List<Buerger> getBuergers() {
        return null;
    }

    /**
     *
     */
    protected abstract void site();
    
}
