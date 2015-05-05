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
import de.muenchen.vaadin.ui.app.views.DefaultPersonView;
import de.muenchen.vaadin.ui.app.views.events.PersonEvent;
import de.muenchen.vaadin.ui.controller.PersonViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

/**
 *
 * @author claus
 */
public class UpdatePersonForm extends CustomComponent {
    
    final BeanFieldGroup<Person> binder = new BeanFieldGroup<Person>(Person.class);
    FormLayout layout = new FormLayout();
    final PersonViewController controller;
    
    
    private Person person;

    public UpdatePersonForm(PersonViewController controller) {
        
        this.controller = controller;
        
        // create form
        this.createForm();

    }
    
    private void createForm() {
        // Now use a binder to bind the members
        this.person = null;
        
        binder.setItemDataSource(person);
        
        layout.setMargin(true);
        
        // headline
        Label headline = new Label(controller.getUtil().readText(DefaultPersonView.I18N_BASE_PATH, I18nPaths.I18N_FORM_UPDATE_HEADLINE_LABEL));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        layout.addComponent(controller.getUtil().createFormTextField(binder, DefaultPersonView.I18N_BASE_PATH, "firstname"));
        layout.addComponent(controller.getUtil().createFormTextField(binder, DefaultPersonView.I18N_BASE_PATH, "lastname"));
        layout.addComponent(controller.getUtil().createFormDateField(binder, DefaultPersonView.I18N_BASE_PATH, "birthdate"));

        // A button to commit the buffer
        String label = controller.getUtil().readText(DefaultPersonView.I18N_BASE_PATH, I18nPaths.I18N_FORM_UPDATE_BUTTON_LABEL);
        layout.addComponent(new Button(label, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    binder.commit();
                    Notification.show("Thanks!");
                    Person person = binder.getItemDataSource().getBean();
                    controller.getEventbus().publish(this, new PersonEvent(person, EventType.UPDATE));
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
