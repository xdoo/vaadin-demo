package de.muenchen.vaadin.guilib.components.buttons;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.Action;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p.mueller on 28.10.15.
 */
public class ConfirmationWindow extends Window {

    private final String okButtonText;
    private final String cancelButtonText;
    private final String messageText;


    /**
     * List of Actions to perform on click.
     **/
    private List<ActionButton.CrashableActionPerformer> actions = new ArrayList<>();

    private final HorizontalLayout buttons = new HorizontalLayout();
    private final VerticalLayout layout = new VerticalLayout();

    public ActionButton getOk() {
        return ok;
    }

    private final ActionButton ok;

    public ConfirmationWindow(Action action) {
        okButtonText = BaseUI.getCurrentI18nResolver().resolve(I18nPaths.getConfirmationPath(action, I18nPaths.Type.confirm));
        cancelButtonText = BaseUI.getCurrentI18nResolver().resolve(I18nPaths.getConfirmationPath(action, I18nPaths.Type.cancel));
        messageText = BaseUI.getCurrentI18nResolver().resolve(I18nPaths.getConfirmationPath(action, I18nPaths.Type.text));

        ok = new ActionButton(getOkButtonText(), SimpleAction.none);

        init();
        setContent(layout);
    }

    private void init() {
        Button cancel = new Button(getCancelButtonText());
        cancel.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        cancel.addClickListener(c -> close());

        getButtons().addComponents(cancel, getOk());

        final Label label = new Label(getMessageText());
        label.setHeightUndefined();

        getLayout().addComponents(label, getButtons());

        configureOkButton();
        configureButtons();
        configureContent();
        configureWindow();
    }

    private void configureOkButton() {
        getOk().setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        getOk().addClickListener(c -> {
            perform(c);
            close();
        });
    }


    /**
     * Add an crashable action performer as click listener.
     *
     * @param performer performer which can crash.
     */
    public void addActionPerformer(ActionButton.CrashableActionPerformer performer) {
        this.actions.add(performer);
    }


    private void perform(Button.ClickEvent event) {
        //Perform all actions until all actions are performed or one crashes.
        actions.stream().sequential().anyMatch(actionPerformer -> !actionPerformer.perform(event));
    }

    private void configureButtons() {
        getButtons().setSpacing(true);
    }

    private void configureContent() {
        getLayout().setHeightUndefined();
        getLayout().setSpacing(true);
        getLayout().setMargin(true);
        getLayout().setComponentAlignment(getButtons(), Alignment.BOTTOM_RIGHT);
    }

    private void configureWindow() {
        setCaption("Achtung!");
        setClosable(false);
        setResizable(false);
        setModal(true);
        setSizeUndefined();
    }


    public HorizontalLayout getButtons() {
        return buttons;
    }

    public VerticalLayout getLayout() {
        return layout;
    }

    public String getOkButtonText() {
        return okButtonText;
    }

    public String getCancelButtonText() {
        return cancelButtonText;
    }

    public String getMessageText() {
        return messageText;
    }
}

