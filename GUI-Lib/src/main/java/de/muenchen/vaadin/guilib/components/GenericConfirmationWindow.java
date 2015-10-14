package de.muenchen.vaadin.guilib.components;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.Action;

/**
 * Generisches Bestätigungsfenster mit einer "ok" und
 * einer "abbrechen" Schaltfläche.
 * <p/>
 *
 * @author claus.straube
 */
public class GenericConfirmationWindow extends Window {

    /**
     * Beschriftung "ok" Schaltfläche
     */
    private String okButtonText;

    /**
     * Beschriftung "abbrechen" Schaltfläche
     */
    private String cancelButtonText;

    /**
     * Fenster Nachricht
     */
    private String messageText;


    /**
     * Konfiguriert ein Bestätigungsfenster mit einer "ok" und
     * einer "abbrechen" Schaltfläche.
     *
     * @param controller          der Controller
     * @param action              Art des Fensters
     * @param okButtonClickAction Aktion der bei klick auf den OK-Button ausgeführt werden soll
     */
    public GenericConfirmationWindow(I18nResolver controller, Action action, Button.ClickListener okButtonClickAction) {
        this.okButtonText = controller.resolve(I18nPaths.getConfirmationPath(action, I18nPaths.Type.confirm));
        this.cancelButtonText = controller.resolve(I18nPaths.getConfirmationPath(action, I18nPaths.Type.cancel));
        this.messageText = controller.resolve(I18nPaths.getConfirmationPath(action, I18nPaths.Type.text));

        Panel content = new Panel();
        VerticalLayout l = new VerticalLayout(new Label(this.messageText));
        l.setMargin(true);
        content.setSizeFull();
        content.addStyleName(ValoTheme.PANEL_BORDERLESS);
        content.setContent(l);

        HorizontalLayout footer = new HorizontalLayout(this.addOkButton(okButtonClickAction, getIconFor(action)), this.addCancelButton());
        footer.setWidth("100%");
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);


        VerticalLayout root = new VerticalLayout(content, footer);

        this.setWidth("380px");
        String prevHeight = this.getHeight() + this.getHeightUnits().toString();
        this.setHeight(prevHeight);
        this.setClosable(false);
        this.setResizable(false);
        this.setModal(true);
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
     * @param clickListener
     * @return "ok" Schaltfläche
     */
    private Button addOkButton(Button.ClickListener clickListener, FontAwesome icon) {
        Button ok = new Button() {
            @Override
            public void addClickListener(Button.ClickListener listener) {
                Button.ClickListener complete = event -> {
                    listener.buttonClick(event);
                    GenericConfirmationWindow.this.close();
                };
                super.addClickListener(complete);
            }
        };
        ok.setIcon(icon);
        ok.setCaption(this.okButtonText);
        ok.addStyleName(ValoTheme.BUTTON_DANGER);
        ok.addClickListener(clickListener);
        ok.setSizeFull();
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
        cancel.setCaption(this.cancelButtonText);
        cancel.addClickListener(e -> {
            this.close();
        });
        cancel.setSizeFull();
        return cancel;
    }
}