package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.EventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;

/**
 * Generisches Bestätigungsfenster mit einer "ok" und 
 * einer "abbrechen" Schaltfläche. 
 * 
 * TODO -> sollte in einen generischen Artefakt übernommen werden.
 * 
 * @author claus.straube
 */
public class GenericConfirmationWindow extends Window {
    
    /**
     * Beschriftung "ok" Schaltfläche
     */
    private String okButton;
    
    /**
     * Beschriftung "abbrechen" Schaltfläche
     */
    private String cancelButton;
    
    /**
     * Fenster Nachricht
     */
    private String message;
    
    /**
     * Konfiguriert ein Bestätigungsfenster mit einer "ok" und 
     * einer "abbrechen" Schaltfläche.
     * 
     * @param event Ereignis für Klick auf "ok" Schaltfläche
     * @param eventbus der aktuelle Event Bus
     */
    public GenericConfirmationWindow(AppEvent event, EventBus eventbus) {
        this.addDefaultLabels();
    
        Panel content = new Panel();
        VerticalLayout l = new VerticalLayout(new Label(this.message));
        l.setMargin(true);
        content.setSizeFull();
        content.addStyleName(ValoTheme.PANEL_BORDERLESS);
        content.setContent(l);
        
        HorizontalLayout footer = new HorizontalLayout(this.addOkButton(event, eventbus), this.addCancelButton());
        footer.setWidth("100%");
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        
        
        VerticalLayout root = new VerticalLayout(content, footer);
        
        this.setWidth("380px");
        String prevHeight = this.getHeight() + this.getHeightUnits().toString();
        this.setHeight(prevHeight);
        this.setClosable(false);
        this.setResizable(false);
        this.setContent(root);
        this.setCloseShortcut(KeyCode.ESCAPE, null);
    }
    
    public void addDefaultLabels() {
        this.message = "Wollen Sie den Datensatz wirklich löschen?";
        this.cancelButton = "abbrechen";
        this.okButton = "löschen";
    }
    
    /**
     * Erstellt eine Schaltfläche, die das übergebene Event an 
     * den Eventbus schickt. Danach wird das Fenster geschlossen.
     * 
     * @param event
     * @param eventbus
     * @return "ok" Schaltfläche
     */
    private Button addOkButton(AppEvent event, EventBus eventbus) {
        Button ok = new Button();
        ok.setIcon(FontAwesome.TRASH_O);
        ok.setCaption(this.okButton);
        ok.addStyleName(ValoTheme.BUTTON_DANGER);
        ok.addClickListener(e -> {
            eventbus.post(event);
            this.close();
        });
        
        return ok;
    }
    
    /**
     * Erstellt eine Schaltfläche, die ohne Aktion das Fenster schließt.
     * 
     * @return "cancel" Schaltfläche
     */
    private Button addCancelButton() {
        Button cancel = new Button();
        cancel.setIcon(FontAwesome.REORDER);
        cancel.setCaption(this.cancelButton);
        cancel.addClickListener(e -> {
            this.close();
        });
        
        return cancel;
    }
    
    // Default Werte überschreiben

    public String getOkButton() {
        return okButton;
    }

    public void setOkButton(String okButton) {
        this.okButton = okButton;
    }

    public String getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(String cancelButton) {
        this.cancelButton = cancelButton;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}