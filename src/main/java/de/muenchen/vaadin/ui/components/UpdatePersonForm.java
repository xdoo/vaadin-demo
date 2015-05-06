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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus
 */
public class UpdatePersonForm extends CustomComponent {
    
    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(UpdatePersonForm.class);
    
    final BeanFieldGroup<Person> binder = new BeanFieldGroup<Person>(Person.class);
    FormLayout layout = new FormLayout();
    final PersonViewController controller;
    
    
    private Person person;
    private String navigateTo;

    public UpdatePersonForm(PersonViewController controller, String navigateTo) {
        
        this.controller = controller;
        this.navigateTo = navigateTo;
        
        // create form
        this.createForm();

    }
    
    private void createForm() {
        
        // Beim ersten Aufruf der view kann die Komponente nicht
        // aktiv mit einem Objekt versorgt werden. Hier muss sich
        // die Komponente die Daten selbst holen.
        if(this.person == null) {
           this.binder.setItemDataSource(this.controller.getCurrent());
           this.person = this.controller.getCurrent().getBean();
        }
        
        layout.setMargin(true);
        
        // headline
        Label headline = new Label(controller.getUtil().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_UPDATE_HEADLINE_LABEL));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), "firstname"));
        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), "lastname"));
        layout.addComponent(controller.getUtil().createFormDateField(binder, controller.getI18nBasePath(), "birthdate"));

        // A button to commit the buffer
        String label = controller.getUtil().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_UPDATE_BUTTON_LABEL);
        layout.addComponent(new Button(label, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent click) {
                try {
                    binder.commit();
                    Notification.show("Thanks!");
                    Person person = binder.getItemDataSource().getBean();
                    PersonEvent event = new PersonEvent(person, EventType.UPDATE);
                    event.setNavigateTo(navigateTo);
                    controller.getEventbus().publish(this, event);
                } catch (FieldGroup.CommitException e) {
                    Notification.show("You fail!");
                }
            }
        }));

        setCompositionRoot(layout);
    }
    
    public void select(BeanItem<Person> person) {
        LOG.debug("seleted person to modify.");
        this.binder.setItemDataSource(person);
        this.person = person.getBean();
    }

}
