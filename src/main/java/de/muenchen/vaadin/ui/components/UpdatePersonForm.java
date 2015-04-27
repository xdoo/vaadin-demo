/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.app.views.PersonView;
import de.muenchen.vaadin.ui.app.views.events.UpdatePersonEvent;
import de.muenchen.vaadin.ui.util.I18nPaths;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author claus
 */
public class UpdatePersonForm extends CustomComponent {
    
    PersonService service;
    final EventBus eventBus;
    final BeanFieldGroup<Person> binder = new BeanFieldGroup<Person>(Person.class);
    VaadinUtil util;
    FormLayout layout = new FormLayout();
    
    
    private Person person;

    public UpdatePersonForm(VaadinUtil util, final PersonService service, final EventBus eventbus) {
        
        this.service = service;
        this.eventBus = eventbus;
        this.util = util;

        // create form
        this.createForm();

    }
    
    private void createForm() {
        // Now use a binder to bind the members
        this.person = null;
        
        binder.setItemDataSource(person);
        
        layout.setMargin(true);
        
        // headline
        Label headline = new Label(util.readText(PersonView.I18N_BASE_PATH, I18nPaths.I18N_FORM_UPDATE_HEADLINE_LABEL));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        layout.addComponent(util.createFormTextField(binder, PersonView.I18N_BASE_PATH, "firstname"));
        layout.addComponent(util.createFormTextField(binder, PersonView.I18N_BASE_PATH, "lastname"));
        layout.addComponent(util.createFormDateField(binder, PersonView.I18N_BASE_PATH, "birthdate"));

        // A button to commit the buffer
        String label = util.readText(PersonView.I18N_BASE_PATH, I18nPaths.I18N_FORM_UPDATE_BUTTON_LABEL);
        layout.addComponent(new Button(label, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    binder.commit();
                    Notification.show("Thanks!");
                    Person person = binder.getItemDataSource().getBean();
                    eventBus.publish(this, new UpdatePersonEvent(person));
                    service.updatePerson(person);
                } catch (FieldGroup.CommitException e) {
                    Notification.show("You fail!");
                }
            }
        }));

        setCompositionRoot(layout);
    }
    
    public void select(BeanItem<Person> person) {
        this.binder.setItemDataSource(person);
    }

}
