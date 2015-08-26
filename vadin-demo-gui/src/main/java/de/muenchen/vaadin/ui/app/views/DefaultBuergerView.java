package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import javax.annotation.PostConstruct;

import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getPagePath;


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
    
    /**
     * 
     */
    protected abstract void site();
    
    protected void addHeadline() {
        
        // headline
        Label pageTitle = new Label(controller.resolveRelative(getPagePath(Type.title)));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        
        
        //HorizontalLayout head = new HorizontalLayout(pageTitle);
        addComponent(pageTitle);
    }
    
    private void configureLayout() {
        setSizeFull();
        this.setHeightUndefined();
        setMargin(true);
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.configureLayout();

        // add some components
        this.addHeadline();
        this.site();
    }
    
}
