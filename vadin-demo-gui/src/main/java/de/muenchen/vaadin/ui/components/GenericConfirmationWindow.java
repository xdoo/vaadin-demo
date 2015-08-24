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
import de.muenchen.vaadin.ui.components.buttons.Action;
import de.muenchen.vaadin.ui.controller.ControllerContext;
import de.muenchen.vaadin.ui.util.I18nPaths;

import static de.muenchen.vaadin.ui.util.I18nPaths.getConfirmationPath;

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
     * @param controller der Controller
     * @param action Art des Fensters
     */
    public GenericConfirmationWindow(AppEvent event, ControllerContext<?> controller, Action action){
        this.okButton = controller.resolve(getConfirmationPath(action, I18nPaths.Type.confirm));
        this.cancelButton = controller.resolve(getConfirmationPath(action, I18nPaths.Type.cancel));
        this.message = controller.resolve(getConfirmationPath(action, I18nPaths.Type.text));

        Panel content = new Panel();
        VerticalLayout l = new VerticalLayout(new Label(this.message));
        l.setMargin(true);
        content.setSizeFull();
        content.addStyleName(ValoTheme.PANEL_BORDERLESS);
        content.setContent(l);

        HorizontalLayout footer = new HorizontalLayout(this.addOkButton(event, controller, getIconFor(action)), this.addCancelButton());
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

    private FontAwesome getIconFor(Action action) {
        return action.getIcon().orElse(FontAwesome.AMBULANCE);
    }


    /**
     * Erstellt eine Schaltfläche, die das übergebene Event an 
     * den Eventbus schickt. Danach wird das Fenster geschlossen.
     * 
     * @param event
     * @param controller
     * @return "ok" Schaltfläche
     */
    private Button addOkButton(AppEvent event, ControllerContext<?> controller, FontAwesome icon) {
        Button ok = new Button();
        ok.setIcon(icon);
        ok.setCaption(this.okButton);
        ok.addStyleName(ValoTheme.BUTTON_DANGER);
        ok.addClickListener(e -> {
            controller.postToEventBus(event);
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