package de.muenchen.vaadin.ui.app.views;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.services.SecurityService;
import de.muenchen.vaadin.ui.app.views.events.LoginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.bus.EventBus;

import static reactor.bus.Event.wrap;

@SpringView(name = LoginView.NAME)
@UIScope
public class LoginView extends VerticalLayout implements View {
    private static final long serialVersionUID = -4430276235082912377L;
    public static final String NAME = SecurityService.LOGIN_VIEW_NAME;
    // Services
    private SecurityService security;
    @Autowired
    private EventBus eventBus;
    // Vaadin Komponenten
    private TextField username;
    private PasswordField password;
    @Autowired
    public LoginView(SecurityService security) {
        this.security = security;
        setSizeFull();
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }
    @Override
    public void enter(ViewChangeEvent event) {
        username.focus();
    }
    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        loginPanel.setMargin(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");
        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        return loginPanel;
    }
    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");
        username = new TextField("Username");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.setId("LOGIN_USERNAME_TEXTFIELD");
        password = new PasswordField("Password");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        password.setId("LOGIN_PASSWORD_TEXTFIELD");
        final Button signin = new Button("Sign In");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();
        signin.setId("LOGIN_SIGNIN_BUTTON");
        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
        signin.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void buttonClick(final ClickEvent event) {
                if(security.login(username.getValue(), password.getValue())) {
                    LoginEvent loginEvent = new LoginEvent();
                    eventBus.notify(loginEvent,wrap(loginEvent));
                } else {
// Anmeldung fehlgeschlagen
                    Notification notif = new Notification(
                            "Anmeldung fehlgeschlagen",
                            "Bei der Eingabe Ihrer Usernamens/Ihres Kennworts ist ein Fehler aufgetreten. Versuchen Sie es erneut.",
                            Notification.Type.WARNING_MESSAGE);
                    notif.setDelayMsec(5000);
                    notif.setPosition(Position.BOTTOM_RIGHT);
                    notif.show(Page.getCurrent());
                }
// try {
// security.login(username.getValue(), password.getValue());
// } catch (AuthenticationException e) {
// e.printStackTrace();
// } catch (Exception e) {
// e.printStackTrace();
// }
// TODO Register Remember me Token
/*
* Redirect is handled by the VaadinRedirectStrategy
* User is redirected to either always the default
* or the URL the user request before authentication
*
* Strategy is configured within SecurityConfiguration
* Defaults to User request URL.
*/
            }
        });
        return fields;
    }
    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");
        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);
        Label title = new Label("Security-Sample");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }
}
