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
 * Provides a simple Window that allows the execution of multiple {@link de.muenchen.vaadin.guilib.components.buttons.ActionButton.CrashableActionPerformer}
 * on confirmation.
 */
public class ConfirmationWindow extends Window {
    /** The Content of the Window. */
    private final VerticalLayout layout = new VerticalLayout();
    /** The Layout for the Buttons. */
    private final HorizontalLayout buttons = new HorizontalLayout();
    /** The Button for the confirmation. */
    private final ActionButton ok;
    /** The action that shall be confirmed. */
    private final Action action;
    /** List of Actions to perform on click. */
    private List<ActionButton.CrashableActionPerformer> actions = new ArrayList<>();

    /**
     * Create a new ConfirmationWindow for the specified Action.
     *
     * @param action The action which requires a confirmation.
     */
    public ConfirmationWindow(Action action) {
        if (action == null) {
            throw new NullPointerException();
        }
        this.action = action;

        final String okButtonText = BaseUI.getCurrentI18nResolver().resolve(I18nPaths.getConfirmationPath(getAction(), I18nPaths.Type.confirm));
        ok = new ActionButton(okButtonText, SimpleAction.none);

        init();
        setContent(layout);
    }

    /**
     * Get the Action of this ConfirmationWindow.
     *
     * @return The Action.
     */
    public Action getAction() {
        return action;
    }

    /**
     * Get the Button that is used for the confirmation.
     *
     * @return The 'oK' ActionButton.
     */
    public ActionButton getOk() {
        return ok;
    }

    /**
     * Initialize the content of the Window.
     */
    private void init() {
        final String cancelButtonText = BaseUI.getCurrentI18nResolver().resolve(I18nPaths.getConfirmationPath(action, I18nPaths.Type.cancel));
        final Button cancel = new Button(cancelButtonText);
        cancel.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        cancel.addClickListener(c -> close());
        getButtons().addComponents(cancel, getOk());

        final String messageText = BaseUI.getCurrentI18nResolver().resolve(I18nPaths.getConfirmationPath(action, I18nPaths.Type.text));
        final Label label = new Label(messageText);
        label.setHeightUndefined();

        getLayout().addComponents(label, getButtons());
        configureOkButton();
        configureButtons();
        configureContent();
        configureWindow();
    }


    /**
     * Configure the OK Button.
     */
    private void configureOkButton() {
        getOk().setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        getOk().addClickListener(c -> {
            perform(c);
            close();
        });
    }

    /**
     * Add an crashable action performer as click listener to the OK Button..
     *
     * @param performer performer which can crash.
     */
    public void addActionPerformer(ActionButton.CrashableActionPerformer performer) {
        this.actions.add(performer);
    }


    /**
     * Perform all actions until all actions are performed or one crashes.
     *
     * @param event The ClickEvent.
     */
    private void perform(Button.ClickEvent event) {
        //Perform all actions until all actions are performed or one crashes.
        actions.stream().sequential().anyMatch(actionPerformer -> !actionPerformer.perform(event));
    }

    /**
     * Configure the Buttons Layout.
     */
    private void configureButtons() {
        getButtons().setSpacing(true);
    }

    /**
     * Configure the Content Layout.
     */
    private void configureContent() {
        getLayout().setHeightUndefined();
        getLayout().setSpacing(true);
        getLayout().setMargin(true);
        getLayout().setComponentAlignment(getButtons(), Alignment.BOTTOM_RIGHT);
    }

    /**
     * Configure this Window.
     */
    private void configureWindow() {
        setCaption("Achtung!");
        setClosable(false);
        setResizable(false);
        setModal(true);
        setSizeUndefined();
    }

    /**
     * Get the Layout for the Buttons.
     *
     * @return The layout with the Buttons.
     */
    public HorizontalLayout getButtons() {
        return buttons;
    }

    /**
     * Get the layout used as Content in the Window.
     *
     * @return The vertical layout.
     */
    public VerticalLayout getLayout() {
        return layout;
    }
}

