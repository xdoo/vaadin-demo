/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views;

import javax.annotation.PostConstruct;
import org.vaadin.spring.navigator.annotation.VaadinView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import org.vaadin.spring.annotation.VaadinUIScope;

@VaadinView(name = SecuredView.NAME)
@VaadinUIScope
//@Secured("ROLE_USER")
public class SecuredView extends VerticalLayout implements View {

    private static final long serialVersionUID = 6937605817612926676L;
    public static final String NAME = "secured";

    @PostConstruct
    private void postConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        addComponent(new Label("<h3>Secured View</h3>", ContentMode.HTML));
        addComponent(VaadinUtil.createNavigationButton("Go To Main View", MainView.NAME));
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }
}
