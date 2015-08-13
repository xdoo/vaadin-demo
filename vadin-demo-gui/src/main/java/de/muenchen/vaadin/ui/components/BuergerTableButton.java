package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 *
 * @author claus.straube
 */
public interface BuergerTableButton {
    
    public Button getCustomButton();
    
    public Component getComponent();
    
    public void setNavigateFrom(String navigateFrom);
    public void setItemId(Object itemId);
    public void setNavigateTo(String navigateTo);
    public void setController(BuergerViewController controller);
    public void setContainer(BeanItemContainer<Buerger> container);
}
