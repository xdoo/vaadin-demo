package de.muenchen.vaadin.ui.app.views;

import javax.annotation.PostConstruct;
import org.vaadin.spring.navigator.annotation.VaadinView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;

@VaadinView(name = MainView.NAME)
@VaadinUIScope
public class MainView extends VerticalLayout implements View {

    private static final long serialVersionUID = -3780256410686877889L;
    public static final String NAME = "";
    
    @Autowired
    VaadinUtil util;

    @PostConstruct
    private void postConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        addComponent(new Label("<h2>Main View</h2>", ContentMode.HTML));
        addComponent(util.createNavigationButton("m2.secured", SecuredView.NAME));
        addComponent(util.createNavigationButton("m1.person", BuergerTableView.NAME));
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }
}
