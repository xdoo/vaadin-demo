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

import javax.annotation.PostConstruct;

@SpringView(name = MainView.NAME)
@UIScope
public class MainView extends VerticalLayout implements View {

    public static final String NAME = "";
    private static final long serialVersionUID = -3780256410686877889L;

    @PostConstruct
    private void postConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(new MarginInfo(false, true, false, true));

        Label pageTitle = new Label("Main View", ContentMode.HTML);
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);

        // Image as a file resource
        ThemeResource resource = new ThemeResource(
                "../../img/gaia.png");


        final Image image1 = new Image("",resource);

        addComponent(pageTitle);
        addComponent(image1);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        //
    }
}
