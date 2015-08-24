package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.demo.api.util.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getEntityFieldPath;

/**
 *
 * @author claus.straube
 */
public class BuergerTable extends CustomComponent {

    private final BeanItemContainer<Buerger> container;
    private BuergerViewController controller;
    private Table table;
    
    // Navigation
    private String from;
    
    // Buttons
    List<TableActionButton.Builder> buttonBuilders;
    
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerTable.class);
    
    public BuergerTable(final BuergerViewController controller, final TableActionButton.Builder... buttonBuilders) {
        
        this.controller = controller;
        this.buttonBuilders = Arrays.asList(buttonBuilders);
        
        // Have a container of some type to contain the data
        this.container = new BeanItemContainer<>(Buerger.class);
        
        // create table
        this.table = new Table();
        table.setContainerDataSource(this.container);

        //set action column
        table.addGeneratedColumn("button", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId,
                    Object columnId) {
                
                return addButtons(itemId);
            }
        });
       
        //configure
        table.setWidth("100%");
        table.setPageLength(10);
        table.setColumnCollapsingAllowed(true);
        table.setVisibleColumns(Buerger.VORNAME, Buerger.NACHNAME, Buerger.GEBURTSDATUM, "button");
        
        // set headers
        table.setColumnHeader(Buerger.VORNAME, controller.resolveRelative(getEntityFieldPath(Buerger.VORNAME, Type.column_header)));

        table.setColumnIcon(Buerger.VORNAME, controller.resolveIcon(getEntityFieldPath(Buerger.VORNAME, Type.column_header)));


        table.setColumnHeader(Buerger.GEBURTSDATUM, controller.resolveRelative(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.column_header)));
        table.setColumnIcon(Buerger.GEBURTSDATUM, controller.resolveIcon(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.column_header)));
        table.setColumnHeader(Buerger.NACHNAME, controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.column_header)));
        table.setColumnIcon(Buerger.NACHNAME, controller.resolveIcon(getEntityFieldPath(Buerger.NACHNAME, Type.column_header)));
        table.setColumnHeader("button", "");
        
        setCompositionRoot(table);
    }
    
    @Subscribe
    public void update(BuergerComponentEvent event) {
        if(event.getEventType().equals(EventType.SAVE)) {
            this.add(event.getEntity());
        }
        
        if(event.getEventType().equals(EventType.COPY)) {
            this.add(event.getEntity());
        }

        if(event.getEventType().equals(EventType.DELETE)) {
            this.delete(event.getItemID());
        }
        
        if(event.getEventType().equals(EventType.UPDATE)) {
            this.add(event.getEntity());
        }
        
        if(event.getEventType().equals(EventType.QUERY)) {
            this.addAll(event.getEntities());
        }
    }
    
    public void add(Optional<Buerger> optional) {
        if(optional.isPresent()) {
            LOG.debug("added buerger to table.");
            this.container.addBean(optional.get());
        }
    }
    
    public void addAll(List<Buerger> buerger) {
        LOG.debug("added search result");
        this.container.removeAllItems();
        this.container.addAll(buerger);
    }
    
    public void delete(Object id) {
        LOG.debug("deleted buerger from table.");
        this.container.removeItem(id);
    }
    
    /**
     * "SimpleAction" Buttons für jede Tabellenzeile. In jeder Tabellenzeile
     * gibt "SimpleAction" Buttons.
     * 
     * @param id
     * @return 
     */
    public HorizontalLayout addButtons(final Object id) {
        HorizontalLayout layout = new HorizontalLayout();
        
        this.buttonBuilders.stream()
                .map((builder) -> builder.build(container,id))
                .forEachOrdered(layout::addComponent);

        layout.setSpacing(true);
        return layout;
    }
    
    // Members

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Über diese Liste können der Tabelle weitere Schaltflächen hinzu gefügt,
     * bzw. die vorhandenen Schaltflächen in eine neue reihenfolge gebracht
     * werden.
     * 
     * @return Liste der {@link de.muenchen.vaadin.ui.components.buttons.TableActionButton.Builder}
     */
    public List<TableActionButton.Builder> getButtonBuilders() {
        return buttonBuilders;
    }
    
}
