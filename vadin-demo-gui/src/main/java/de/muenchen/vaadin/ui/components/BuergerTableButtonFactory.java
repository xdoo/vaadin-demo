package de.muenchen.vaadin.ui.components;

import org.slf4j.LoggerFactory;

/**
 * Die Factory wird an die Tabelle übergeben. Die Tabelle ist dann dafür zuständig
 * über die Factory neue Instanzen der Schaltflächen zu erzeugen. 
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
     
    /**
     * Erzeugt eine Instanz der Factory.
     * 
     * @param navigateTo Die Seite, zu der bei drücken der Schaltfläche navigiert werden soll. Null, wenn keine Navigation erfolgen soll.
     * @param clazz Die eigene Schaltflächen Klasse. Wichtig ist, dass diese von {@link BuergerTableButtonBase} abgeleitet wird. 
     * @return eine Instanz der Factory
     */
    public static BuergerTableButtonFactory getFactory(String navigateTo, Class clazz) {
        return new BuergerTableButtonFactory(navigateTo, clazz);
    }
    
    /**
     * Erzeugt die Schaltfläche. <br/> 
     * <b>wichtig:</b><br/>
     * Diese muss noch mit zusätzlichen Parametern versorgt werden. Die passiert 
     * üblicherweise innerhalb der Tabellen Instanz. D.h. der Entwickler muss sich 
     * nicht darum kümmern.
     * 
     * @return 
     */
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
