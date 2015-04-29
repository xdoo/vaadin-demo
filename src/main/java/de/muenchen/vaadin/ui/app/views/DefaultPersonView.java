package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.app.views.events.CreatePersonEvent;
import de.muenchen.vaadin.ui.app.views.events.PersonEvent;
import de.muenchen.vaadin.ui.app.views.events.SelectPersonEvent;
import de.muenchen.vaadin.ui.app.views.events.UpdatePersonEvent;
import de.muenchen.vaadin.ui.components.CreatePersonForm;
import de.muenchen.vaadin.ui.components.PersonTable;
import de.muenchen.vaadin.ui.components.UpdatePersonForm;
import de.muenchen.vaadin.ui.controller.PersonViewController;
import de.muenchen.vaadin.ui.util.I18nPaths;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

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
public abstract class DefaultPersonView extends VerticalLayout implements View, EventBusListener<PersonEvent>{

    public static final String I18N_BASE_PATH = "m1.person";
    
    @Autowired
    PersonViewController controller;
    
    @Autowired
    VaadinUtil util;
    
    @Autowired
    PersonService service;
    
    @Autowired
    EventBus eventbus;
    
    // components
    CreatePersonForm createPersonForm;
    UpdatePersonForm updatePersonForm;
    PersonTable personTable;
    
    /**
     * 
     */
    @PostConstruct
    private void postConstruct() {
        this.configure();
        this.createPersonForm = new CreatePersonForm(util, service, eventbus);
        this.updatePersonForm = new UpdatePersonForm(util, service, eventbus);
        this.personTable = new PersonTable(util, service, eventbus);
        
        // add some components
        this.addHeadline();
        this.site();
        
        this.eventbus.subscribe(this, true);
    }
    
    /**
     * The 'rigth'side of the site. You can put in here, everthing you need.
     */
    protected abstract void site();
    
    protected void addHeadline() {
        
        // headline
        Label pageTitle = new Label(util.readText(I18N_BASE_PATH, I18nPaths.I18N_PAGE_TITLE));
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

    @Override
    public void onEvent(org.vaadin.spring.events.Event<PersonEvent> e) {
        PersonEvent event = e.getPayload();
        
        // create
        if(event instanceof CreatePersonEvent) {
            this.personTable.add(event.getPerson());
        } 
        
        // update
        if(event instanceof UpdatePersonEvent) {
            this.personTable.update(event.getPerson());
        }
        
        // select
        if(event instanceof SelectPersonEvent) {
            this.updatePersonForm.select(event.getItem());
        }
    }
    
}
