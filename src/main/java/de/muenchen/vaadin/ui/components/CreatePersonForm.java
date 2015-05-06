package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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
 * @author claus.straube
 */
public class CreatePersonForm extends CustomComponent {

    public CreatePersonForm(final PersonViewController controller, final String navigateTo) {
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        
        // headline
        Label headline = new Label(controller.getUtil().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_HEADLINE_LABEL));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        // Now use a binder to bind the members
        final BeanFieldGroup<Person> binder = new BeanFieldGroup<Person>(Person.class);
        binder.setItemDataSource(new Person());

        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), "firstname"));
        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), "lastname"));
        layout.addComponent(controller.getUtil().createFormDateField(binder, controller.getI18nBasePath(), "birthdate"));

        // A button to commit the buffer
        String label = controller.getUtil().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_BUTTON_LABEL);
        layout.addComponent(new Button(label, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent click) {
                try {
                    binder.commit();
                    // TODO --> i18n
                    Success succes = new Success("Person erstellt", "Die Person wurde erfolgreich erstellt und gespeichert.");
                    succes.show(Page.getCurrent());
                    PersonEvent event = new PersonEvent(binder.getItemDataSource().getBean(), EventType.UPDATE);
                    event.setNavigateTo(navigateTo);
                    controller.getEventbus().publish(this, event);
                    //reset
                    binder.setItemDataSource(new Person());
                } catch (CommitException e) {
                    // TODO --> i18n
                    Error error = new Error("Fehler", "Beim erstellen der Person ist ein Fehler aufgetreten. Der Service Desk wurde per E-Mail informiert");
                    error.show(Page.getCurrent());
                }
            }
        }));

        setCompositionRoot(layout);
    }

}
