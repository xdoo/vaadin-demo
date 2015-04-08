/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.springframework.context.MessageSource;
import org.vaadin.spring.annotation.VaadinUI;

@VaadinUI
@Title("Vaadin Spring-Security Sample")
@Theme("valo")
//@Widgetset("de.muenchen.vaadin.Widgetset")
public class MainUI extends UI {

    private static final long serialVersionUID = 5310014981075920878L;
    
    @Autowired
    private SpringViewProvider ViewProvider;
    
    @Override
    protected void init(VaadinRequest request) {
        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(ViewProvider);
        setNavigator(navigator);
    }
}
