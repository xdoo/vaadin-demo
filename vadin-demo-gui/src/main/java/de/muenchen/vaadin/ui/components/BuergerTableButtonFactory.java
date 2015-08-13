package de.muenchen.vaadin.ui.components;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus.straube
 */
public class BuergerTableButtonFactory {
    
    protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(BuergerTableButtonFactory.class);
    
    private final String navigateTo;
    private final Class<BuergerTableButton> clazz;

    public BuergerTableButtonFactory(String navigateTo, Class<BuergerTableButton> clazz) {
        this.navigateTo = navigateTo;
        this.clazz = clazz;
    }
      
    public static BuergerTableButtonFactory getFactory(String navigateTo, Class clazz) {
        return new BuergerTableButtonFactory(navigateTo, clazz);
    }
    
    public BuergerTableButton createButton() {
        BuergerTableButton button = null;
        try {
            button = clazz.newInstance();
            button.setNavigateTo(navigateTo);
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.error(String.format("Problem creating new instance of a BuergerTableButton.\n %s", ex.getLocalizedMessage()));
        }
        return button;
    }
    
}
