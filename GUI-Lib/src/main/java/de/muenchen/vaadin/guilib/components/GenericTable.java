package de.muenchen.vaadin.guilib.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import de.muenchen.eventbus.events.ComponentEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.apilib.util.FieldIdentifier;
import de.muenchen.vaadin.demo.i18nservice.ControllerContext;
import de.muenchen.vaadin.demo.i18nservice.buttons.TableActionButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

/**
 * Generic table for all Entities.
 * @param <T>  the type of Entity
 * @author rene.zarwel
 */
public class GenericTable<T> extends CustomComponent implements Consumer<Event<ComponentEvent<T>>> {

    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GenericTable.class);
    private final BeanItemContainer<T> container;
    /**
     * The Button builders.
     */
    List<TableActionButton.Builder> buttonBuilders;
    private Table table;

    /**
     * Instantiates a new Generic table.
     *
     * @param controller the controller
     * @param entityClass the entity class
     * @param buttonBuilders the button builders
     */
    public GenericTable(final ControllerContext controller, Class<T> entityClass, final TableActionButton.Builder... buttonBuilders) {

        this.buttonBuilders = Arrays.asList(buttonBuilders);

        // Have a container of some type to contain the data
        this.container = new BeanItemContainer<>(entityClass);

        // create table
        this.table = new Table();
        table.setContainerDataSource(this.container);

        //set action column
        table.addGeneratedColumn("table_buttons",
                (Table source, Object itemId, Object columnId) -> addButtons(itemId)
        );

        //configure
        table.setWidth("100%");
        table.setPageLength(10);
        table.setColumnCollapsingAllowed(true);


        //Read all FieldIdentifier of Entity and generate Column for each one
        List<String> columnList = Arrays.asList(entityClass.getDeclaredFields()).stream()
                .filter(field -> field.isAnnotationPresent(FieldIdentifier.class))
                .map(Field::getName)
                .collect(Collectors.toList());

        columnList.add("table_buttons");

        table.setVisibleColumns(columnList.toArray());

        columnList.stream()
                .forEach(fieldName -> {

                    table.setColumnHeader(fieldName, controller.resolveRelative(getEntityFieldPath(fieldName, Type.column_header)));

                    table.setColumnIcon(fieldName, controller.resolveIcon(getEntityFieldPath(fieldName, Type.column_header)));
                });


        setCompositionRoot(table);
    }

    /**
     * Adds a entity.
     *
     * @param entity the entity
     */
    public void add(Optional<T> entity) {
        if (entity.isPresent()) {
            LOG.debug("added entity to table.");
            this.container.addBean(entity.get());
        }
    }

    /**
     * Adds a list of entities.
     *
     * @param entity the entity
     */
    public void addAll(List<T> entity) {
        LOG.debug("added search result");
        this.container.removeAllItems();
        this.container.addAll(entity);
    }

    /**
     * Delete an entity.
     *
     * @param id the id
     */
    public void delete(Object id) {
        LOG.debug("deleted buerger from table.");
        this.container.removeItem(id);
    }

    /**
     * "SimpleAction" Buttons für jede Tabellenzeile. In jeder Tabellenzeile
     * gibt "SimpleAction" Buttons.
     *
     * @param id ID
     * @return HL horizontal layout
     */
    public HorizontalLayout addButtons(final Object id) {
        HorizontalLayout layout = new HorizontalLayout();

        this.buttonBuilders.stream()
                .map((builder) -> builder.build(container, id))
                .forEachOrdered(layout::addComponent);

        layout.setSpacing(true);
        return layout;
    }


    /**
     * Über diese Liste können der Tabelle weitere Schaltflächen hinzu gefügt,
     * bzw. die vorhandenen Schaltflächen in eine neue reihenfolge gebracht
     * werden.
     *
     * @return Liste der
     */
    public List<TableActionButton.Builder> getButtonBuilders() {
        return buttonBuilders;
    }

    @Override
    public void accept(reactor.bus.Event<ComponentEvent<T>> eventWrapper) {
        ComponentEvent event = eventWrapper.getData();

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
}
