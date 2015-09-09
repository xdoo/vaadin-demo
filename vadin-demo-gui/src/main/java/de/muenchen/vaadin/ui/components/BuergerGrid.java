package de.muenchen.vaadin.ui.components;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.guilib.util.VaadinUtil;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

/**
 *
 * @author claus.straube
 */
public class BuergerGrid extends CustomComponent {

    private final BeanItemContainer<Buerger> container;
    private BuergerViewController controller;
    private Grid grid;

    public BuergerGrid(final BuergerViewController controller) {

        this.controller = controller;
        
        // Have a container of some type to contain the data
        this.container = new BeanItemContainer<>(Buerger.class);

        this.grid = new Grid();
        this.grid.setContainerDataSource(this.container);

                // Generate button caption column
        GeneratedPropertyContainer gpc
                = new GeneratedPropertyContainer(this.container);
        gpc.addGeneratedProperty(VaadinUtil.TABLE_COLUMN_ACTIONS,
                new PropertyValueGenerator<String>() {

                    @Override
                    public String getValue(Item item, Object itemId,
                            Object propertyId) {
                        return ""; // The caption
                    }

                    @Override
                    public Class<String> getType() {
                        return String.class;
                    }
                });
        
        // configure
        this.grid.setWidth("100%");
        this.grid.setHeightByRows(10);
        this.grid.getColumns().stream().forEach(c -> {c.setHidable(true);});
        this.grid.setColumnOrder(Buerger.VORNAME, Buerger.NACHNAME, Buerger.GEBURTSDATUM, VaadinUtil.TABLE_COLUMN_ACTIONS);
        
        // set headers
        this.grid.getColumn(Buerger.VORNAME).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.VORNAME, Type.column_header)));
        this.grid.getColumn(Buerger.GEBURTSDATUM).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.column_header)));
        this.grid.getColumn(Buerger.NACHNAME).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.column_header)));
        this.grid.getColumn(VaadinUtil.TABLE_COLUMN_ACTIONS).setHeaderCaption("");
        
        setCompositionRoot(this.grid);
    }

}
