package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.security.components.User_CreateForm;
import de.muenchen.vaadin.guilib.security.components.User_Grid;
import de.muenchen.vaadin.guilib.security.controller.User_ViewController;
import de.muenchen.vaadin.ui.app.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = UserCreateView.NAME)
@UIScope
public class UserCreateView extends VerticalLayout implements View {

    public static final String NAME = "userCreate";

    @Autowired
    User_ViewController userViewController;

    @PostConstruct
    private void postConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(new MarginInfo(false, true, false, true));

        Label pageTitle = new Label("Create User", ContentMode.HTML);
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);

        addComponent(pageTitle);

        ActionButton button = new ActionButton(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".back"), SimpleAction.back);
        button.addActionPerformer(new NavigateActions(UserView.NAME)::navigate);
        addComponent(button);

        User_CreateForm createForm = new User_CreateForm(UserView.NAME);
        addComponent(createForm);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        MainUI ui = (MainUI) getUI();
        ui.setHelpContent("");
    }
}
