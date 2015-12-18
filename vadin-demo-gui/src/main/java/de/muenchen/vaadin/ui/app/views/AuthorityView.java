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
import de.muenchen.vaadin.guilib.security.components.Authority_AllInOne;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.guilib.security.controller.Permission_ViewController;
import de.muenchen.vaadin.ui.app.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = AuthorityView.NAME)
@UIScope
public class AuthorityView extends VerticalLayout implements View {

    public static final String NAME = "authorities";

    @Autowired
    Authority_ViewController authorityViewController;

    @Autowired
    Permission_ViewController permissionViewController;

    @PostConstruct
    private void postConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(new MarginInfo(false, true, false, true));

        Label pageTitle = new Label("Authorities", ContentMode.HTML);
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);

        addComponent(pageTitle);

        Authority_AllInOne authorities = new Authority_AllInOne(permissionViewController, authorityViewController);
        addComponent(authorities);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        MainUI ui = (MainUI) getUI();
        ui.setHelpContent("");
    }
}
