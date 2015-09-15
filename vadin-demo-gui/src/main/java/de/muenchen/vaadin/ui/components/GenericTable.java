package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.demo.api.util.FieldIdentifier;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getEntityFieldPath;

/**
 * Generic table for all Entities.
 * @param <T>  the type of Entity
 * @author rene.zarwel
 */
public class GenericTable<T> extends CustomComponent implements Consumer<Event<ComponentEvent<Resource<T>>>> {

    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GenericTable.class);
    private final BeanContainer<Resource<T>, T> container;
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
    public GenericTable(final BuergerViewController controller, Class<T> entityClass, final TableActionButton.Builder... buttonBuilders) {

        this.buttonBuilders = Arrays.asList(buttonBuilders);

        // Have a container of some type to contain the data
        this.container = new BeanContainer<>(entityClass);

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
     * Adds a resource.
     *
     * @param resource the resource
     */
    public void add(Resource<T> resource) {
        container.addItem(resource, resource.getContent());
        LOG.debug("added entity to table.");
    }

    /**
     * Adds a list of resources.
     *
     * @param resources the resources
     */
    public void addAll(List<Resource<T>> resources) {
        LOG.debug("added search result");
        this.container.removeAllItems();

        resources.stream().forEach(this::add);
    }

    /**
     * Delete an entity.
     *
     * @param id the id
     */
    public void delete(Resource<T> id) {
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
                .map((builder) -> builder.build(container, (Resource<T>) id))
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
    public void accept(reactor.bus.Event<ComponentEvent<Resource<T>>> eventWrapper) {
        ComponentEvent<Resource<T>> event = eventWrapper.getData();

        if(event.getEventType().equals(EventType.SAVE)) {
            event.getEntity().ifPresent(this::add);
        }
        if(event.getEventType().equals(EventType.COPY)) {
            event.getEntity().ifPresent(this::add);
        }

        if(event.getEventType().equals(EventType.DELETE)) {
            event.getEntity().ifPresent(this::delete);
        }

        if(event.getEventType().equals(EventType.UPDATE)) {
            event.getEntity().ifPresent(this::add);
        }

        if(event.getEventType().equals(EventType.QUERY)) {
            this.addAll(event.getEntities());
        }
    }
}
