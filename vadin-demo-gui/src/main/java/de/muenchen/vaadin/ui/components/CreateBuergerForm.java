package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

/**
 * Formular zum Erstellen eines {@link Buerger}s.
 * 
 * @author claus.straube
 */
public class CreateBuergerForm extends CustomComponent {
    
    private final String navigateTo;
    private final String navigateBack;
    private final BuergerViewController controller;

    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen
     * Konstruktor wir die Zielseite für die 'erstellen' Schaltfläche automatisch
     * zur Zielseite für die 'abbrechen' Schaltfläche. Dies ist in den meisten Fällen
     * das gewollte verhalten.
     * 
     * @param controller der Entity Controller
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     */
    public CreateBuergerForm(final BuergerViewController controller, final String navigateTo) {
        this.navigateTo = navigateTo;
        this.navigateBack = navigateTo;
        this.controller = controller;
        setCompositionRoot(this.createComponent());
    }
    
    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'abbrechen' Schaltfläche erstellt werden. 
     * Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in eine
     * definierte Abfolge von Formularen eingebettet wird.
     * 
     * @param controller der Entity Controller
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     * @param navigateBack Zielseite nach Druck der 'abbrechen' Schaltfläche
     */
    public CreateBuergerForm(final BuergerViewController controller, final String navigateTo, final String navigateBack) {
        this.navigateTo = navigateTo;
        this.navigateBack = navigateBack;
        this.controller = controller;
        setCompositionRoot(this.createComponent());
    }
    
    /**
     * Erzeugt das eigentliche Formular.
     * 
     * @return 
     */
    private FormLayout createComponent() {
        FormLayout layout = new FormLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
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

        layout.addComponent(buttonLayout);
        // die 'speichern' Schaltfläche
        String createLabel = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_BUTTON_LABEL);
        Button createButton = new Button(createLabel, (ClickEvent click) -> {
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
        });
        createButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        createButton.setIcon(FontAwesome.MAGIC);
        buttonLayout.addComponent(createButton);
        // die 'abbrechen' Schaltfläche
        String cancelLabel = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CANCEL_BUTTON_LABEL);
        Button cancelButton = new Button(cancelLabel, (ClickEvent cancel) -> {
            BuergerEvent event = new BuergerEvent(binder.getItemDataSource().getBean(), EventType.CANCEL);
            event.setNavigateTo(this.navigateBack);
            this.controller.getEventbus().post(event);
            //reset
            binder.setItemDataSource(controller.createBuerger());
        });
        cancelButton.setIcon(FontAwesome.TIMES);
        buttonLayout.addComponent(cancelButton);
        return layout;
    }

    public String getNavigateTo() {
        return navigateTo;
    }

    public BuergerViewController getController() {
        return controller;
    }

    public String getNavigateBack() {
        return navigateBack;
    }
}
