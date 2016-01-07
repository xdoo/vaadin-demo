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
import de.muenchen.vaadin.guilib.security.components.User_Details_AllInOne;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.guilib.security.controller.User_ViewController;
import de.muenchen.vaadin.ui.app.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = UserDetailView.NAME)
@UIScope
public class UserDetailView extends VerticalLayout implements View {

    public static final String NAME = "userDetails";

    @Autowired
    User_ViewController userViewController;

    @Autowired
    Authority_ViewController authorityViewController;

    @PostConstruct
    private void postConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(new MarginInfo(false, true, false, true));

        Label pageTitle = new Label("User Details", ContentMode.HTML);
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);

        addComponent(pageTitle);

        ActionButton button = new ActionButton(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".back"), SimpleAction.back);
        button.addActionPerformer(new NavigateActions(UserView.NAME)::navigate);
        addComponent(button);

        User_Details_AllInOne userDetails = new User_Details_AllInOne(userViewController, authorityViewController);
        addComponent(userDetails);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        MainUI ui = (MainUI) getUI();
        ui.setHelpContent("");
    }
}
