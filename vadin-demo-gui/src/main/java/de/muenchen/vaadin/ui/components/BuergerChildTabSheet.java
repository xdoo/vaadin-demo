package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;

/**
 *
 * @author claus
 */
public class BuergerChildTabSheet extends CustomComponent {

    public BuergerChildTabSheet() {
        TabSheet ts = new TabSheet();
        
        
        setCompositionRoot(ts);
    }
    
    
    
}
