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
import de.muenchen.vaadin.guilib.security.components.User_Grid;
import de.muenchen.vaadin.guilib.security.controller.User_ViewController;
import de.muenchen.vaadin.ui.app.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = UserView.NAME)
@UIScope
public class UserView extends VerticalLayout implements View {

    public static final String NAME = "users";

    @Autowired
    User_ViewController userViewController;

    @PostConstruct
    private void postConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(new MarginInfo(false, true, false, true));

        Label pageTitle = new Label("Users", ContentMode.HTML);
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);

        addComponent(pageTitle);

        User_Grid userGrid = new User_Grid(userViewController);
        userGrid.activateSearch();
        userGrid.activateCreate(UserCreateView.NAME);
        userGrid.activateRead(UserDetailView.NAME);
        userGrid.activateDoubleClickToRead(NAME);
        userGrid.activateDelete();
        addComponent(userGrid);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        MainUI ui = (MainUI) getUI();
        ui.setHelpContent("");
    }
}
