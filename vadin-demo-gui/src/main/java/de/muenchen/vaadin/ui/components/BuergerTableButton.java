package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 *
 * @author claus.straube
 */
public interface BuergerTableButton {
    
    /**
     * Erzeugt eine Instanz der Schaltfläche.
     * 
     * @return 
     */
    public Button getCustomButton();
    
    /**
     * Gibt die Vaadin {@link CustomComponent} zurück.
     * 
     * @return 
     */
    public Component getComponent();
    
    public void setNavigateFrom(String navigateFrom);
    public void setItemId(Object itemId);
    public void setNavigateTo(String navigateTo);
    public void setController(BuergerViewController controller);
    public void setContainer(BeanItemContainer<Buerger> container);
}
