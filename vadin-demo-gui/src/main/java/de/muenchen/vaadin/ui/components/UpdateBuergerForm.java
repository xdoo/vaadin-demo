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
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus
 */
public class UpdateBuergerForm extends CustomComponent {
    
    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(UpdateBuergerForm.class);
    
    final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
    FormLayout layout = new FormLayout();
    final BuergerViewController controller;
    
    
    private Buerger entity;
    private String navigateTo;

    public UpdateBuergerForm(BuergerViewController controller, String navigateTo) {
        
        this.controller = controller;
        this.navigateTo = navigateTo;
        
        // create form
        this.createForm();

    }
    
    private void createForm() {
        
        // Beim ersten Aufruf der view kann die Komponente nicht
        // aktiv mit einem Objekt versorgt werden. Hier muss sich
        // die Komponente die Daten selbst holen.
        if(this.entity == null) {
           this.binder.setItemDataSource(this.controller.getCurrent());
           this.entity = this.controller.getCurrent().getBean();
        }
        
        layout.setMargin(true);
        
        // headline
        Label headline = new Label(controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_UPDATE_HEADLINE_LABEL));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.VORNAME, controller.getMsg()));
        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.NACHNAME, controller.getMsg()));
        layout.addComponent(controller.getUtil().createFormDateField(binder, controller.getI18nBasePath(), Buerger.GEBURTSDATUM, controller.getMsg()));

        // A button to commit the buffer
        String label = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_UPDATE_BUTTON_LABEL);
        layout.addComponent(new Button(label, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent click) {
                try {
                    binder.commit();
                    Notification.show("Thanks!");
                    Buerger entity = binder.getItemDataSource().getBean();
                    BuergerEvent event = new BuergerEvent(entity, EventType.UPDATE);
                    event.setNavigateTo(navigateTo);
                    controller.getEventbus().post(event);
                } catch (FieldGroup.CommitException e) {
                    Notification.show("You fail!");
                }
            }
        }));

        setCompositionRoot(layout);
    }
    
    public void select(BeanItem<Buerger> item) {
        LOG.debug("seleted person to modify.");
        this.binder.setItemDataSource(item);
        this.entity = item.getBean();
    }

}
