/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;

/**
 *
 * @author claus.straube
 */
@SpringView(name = View2.VIEW_NAME)
public class View2 extends VerticalLayout implements View {

    public static final String VIEW_NAME = "view2";
    
    @PostConstruct
    void init() {
        setMargin(true);
        setSpacing(true);
        addComponent(new Label("This is a view scoped view - 2"));
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // the view is constructed in the init() method()
    }
    
}
