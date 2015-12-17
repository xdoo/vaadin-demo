package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.guilib.security.components.Authority_Permissions_ReadEdit;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.guilib.security.controller.Permission_ViewController;
import de.muenchen.vaadin.ui.app.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = UserView.NAME)
@UIScope
public class UserView extends VerticalLayout implements View {

    public static final String NAME = "users";

    @Autowired
    Authority_ViewController authorityViewController;

    @Autowired
    Permission_ViewController permissionViewController;

    @PostConstruct
    private void postConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(new MarginInfo(false, true, false, true));

        Label pageTitle = new Label("Main View", ContentMode.HTML);
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);

        addComponent(pageTitle);

        Authority_Permissions_ReadEdit supertoll = new Authority_Permissions_ReadEdit(permissionViewController, authorityViewController, "asdf");
        addComponent(supertoll);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        MainUI ui = (MainUI) getUI();
        ui.setHelpContent("");
    }
}
