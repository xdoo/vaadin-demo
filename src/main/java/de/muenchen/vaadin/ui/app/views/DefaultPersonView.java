package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.PersonViewController;
import de.muenchen.vaadin.ui.util.I18nPaths;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

/**
 * Für jede Entity existiert eine (voll generierte) Basis Klasse. Aus dieser
 * leiten sich alle anderen Views ab.
 * <br/><br/>
 * Diskussion: 
 * <ul>
 * <li>
 * 1) Aus meiner Sicht wäre es sinnvoller bereits im Modell eine view
 * zu definieren und dieser Entities zu zu ordnen. In der Basis Klasse würden
 * dann Vorbereitungen zur Verarbeitung aller Enties getroffen.
 * </li>
 * <li>
 * 2) Sollen in der Basis View schon alle vorhandenen custom UI Komponenten
 * initialisiert werden? Dafür spricht, dass es dann nicht mehr gemacht werden muss,
 * dagegen spricht, dass damit jeweils eine große Wolke an Objekten initialisiert 
 * wird, die potenziell gar nicht benötigt wird.
 * </li>
 * </ul>
 * 
 * @author claus.straube
 */
public abstract class DefaultPersonView extends VerticalLayout implements View{
    
    
    PersonViewController controller;
    
    public DefaultPersonView(PersonViewController controller, EventBus eventbus, MainUI ui) {
        this.controller = controller;
        this.controller.registerEventBus(eventbus);
        this.controller.registerUI(ui);
    }
    
    
    /**
     * 
     */
    @PostConstruct
    private void postConstruct() {
        this.configure();
        
        // add some components
        this.addHeadline();
        this.site();
    }
    
    /**
     * 
     */
    protected abstract void site();
    
    protected void addHeadline() {
        
        // headline
        Label pageTitle = new Label(this.controller.getUtil().readText(controller.getI18nBasePath(), I18nPaths.I18N_PAGE_TITLE));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        
        
        //
        
        HorizontalLayout head = new HorizontalLayout(pageTitle);
        addComponent(pageTitle);
    }
    
    private void configure() {
        setSizeFull();
        this.setHeightUndefined();
        setMargin(true);
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // not implemented
    }
    
}
