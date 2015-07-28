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
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

/**
 *
 * @author claus.straube
 */
public class CreateBuergerForm extends CustomComponent {

    public CreateBuergerForm(final BuergerViewController controller, final String navigateTo) {
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        
        // headline
        Label headline = new Label(controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_HEADLINE_LABEL));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        // Now use a binder to bind the members
        final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
        binder.setItemDataSource(controller.createBuerger());

        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.VORNAME, controller.getMsg()));
        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.NACHNAME, controller.getMsg()));
        layout.addComponent(controller.getUtil().createFormDateField(binder, controller.getI18nBasePath(), Buerger.GEBURTSDATUM, controller.getMsg()));

        // A button to commit the buffer
        String label = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_BUTTON_LABEL);
        layout.addComponent(new Button(label, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent click) {
                try {
                    binder.commit();
                    BuergerEvent event = new BuergerEvent(binder.getItemDataSource().getBean(), EventType.SAVE);
                    event.setNavigateTo(navigateTo);
                    controller.getEventbus().post(event);
                    //reset
                    binder.setItemDataSource(controller.createBuerger());
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
