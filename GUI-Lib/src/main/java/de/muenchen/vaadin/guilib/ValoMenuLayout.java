/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.guilib;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 *
 * @since @author Vaadin Ltd
 */
public class ValoMenuLayout extends HorizontalLayout {

    CssLayout contentArea = new CssLayout();
    CssLayout menuArea = new CssLayout();

    public ValoMenuLayout() {
        setSizeFull();
        menuArea.setPrimaryStyleName("valo-menu");
        contentArea.setPrimaryStyleName("valo-content");
        contentArea.addStyleName("v-scrollable");
        contentArea.setSizeFull();
        addComponents(menuArea, contentArea);
        setExpandRatio(contentArea, 1);
    }

    public ComponentContainer getContentContainer() {
        return contentArea;
    }

    public void addMenu(Component menu) {
        menu.addStyleName("valo-menu-part");
        menuArea.addComponent(menu);
    }
    
    public void switchOffMenu() {
        this.menuArea.setVisible(Boolean.FALSE);
    }
    
    public void switchOnMenu() {
        this.menuArea.setVisible(Boolean.TRUE);
    }
}
