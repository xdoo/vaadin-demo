/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.app.views.events.CreatePersonEvent;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author claus.straube
 */
public class CreatePersonForm extends CustomComponent {

    public CreatePersonForm(VaadinUtil util, final PersonService service, final EventBus eventbus) {
        FormLayout layout = new FormLayout();
        layout.setMargin(true);

        // Now use a binder to bind the members
        final BeanFieldGroup<Person> binder = new BeanFieldGroup<Person>(Person.class);
        binder.setItemDataSource(new Person());

        layout.addComponent(util.createFormTextField(binder, PersonConstants.BASE_PATH, "firstname"));
        layout.addComponent(util.createFormTextField(binder, PersonConstants.BASE_PATH, "lastname"));
        layout.addComponent(util.createFormDateField(binder, PersonConstants.BASE_PATH, "birthdate"));

        // A button to commit the buffer
        layout.addComponent(new Button("Erstellen", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    binder.commit();
                    Notification.show("Thanks!");
                    Person person = service.createPerson(binder.getItemDataSource().getBean());
                    eventbus.publish(this, new CreatePersonEvent(person));
                    binder.setItemDataSource(new Person());
                } catch (CommitException e) {
                    Notification.show("You fail!");
                }
            }
        }));

        setCompositionRoot(layout);
    }

}
