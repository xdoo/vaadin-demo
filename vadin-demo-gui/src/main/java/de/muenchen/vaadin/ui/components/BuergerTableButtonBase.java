package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 * Basisklasse für alle selbst entwickelten Tabellen Schaltflächen.
 * 
 * @author claus.straube
 */
public abstract class BuergerTableButtonBase extends CustomComponent implements BuergerTableButton {

    protected BeanItemContainer<Buerger> container;
    protected Object itemId;
    protected BuergerViewController controller;
    protected String navigateTo;
    protected String navigateFrom;

    public BeanItemContainer<Buerger> getContainer() {
        return container;
    }

    @Override
    public void setContainer(BeanItemContainer<Buerger> container) {
        this.container = container;
    }

    public BuergerViewController getController() {
        return controller;
    }

    @Override
    public void setController(BuergerViewController controller) {
        this.controller = controller;
    }

    public String getNavigateTo() {
        return navigateTo;
    }

    @Override
    public void setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
    }

    public String getNavigateFrom() {
        return navigateFrom;
    }

    @Override
    public void setNavigateFrom(String navigateFrom) {
        this.navigateFrom = navigateFrom;
    }

    public Object getItemId() {
        return itemId;
    }

    @Override
    public void setItemId(Object itemId) {
        this.itemId = itemId;
    }
    
    @Override
    public Component getComponent() {
        Button button = this.getCustomButton();
        button.setId(String.format("%s_%s_%s_%s_TABLE_BUTTON_%s", BuergerViewController.I18N_BASE_PATH, this.navigateTo, this.navigateFrom, button.getClass().getName(), itemId));
        setCompositionRoot(button);
        return this;
    }

}
