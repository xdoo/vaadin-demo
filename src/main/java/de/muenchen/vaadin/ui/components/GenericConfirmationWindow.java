/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

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
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author claus.straube
 */
public class GenericConfirmationWindow extends Window {
    
    
    private String okButton;
    private String cancelButton;
    private String message;
    
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
    
    private Button addOkButton(AppEvent event, EventBus eventbus) {
        Button ok = new Button();
        ok.setIcon(FontAwesome.TRASH_O);
        ok.setCaption(this.okButton);
        ok.addStyleName(ValoTheme.BUTTON_DANGER);
        ok.addClickListener(e -> {
            eventbus.publish(this, event);
            this.close();
        });
        
        return ok;
    }
    
    private Button addCancelButton() {
        Button cancel = new Button();
        cancel.setIcon(FontAwesome.REORDER);
        cancel.setCaption(this.cancelButton);
        cancel.addClickListener(e -> {
            this.close();
        });
        
        return cancel;
    }
    
}