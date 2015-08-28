package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.demo.api.util.FieldIdentifier;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getEntityFieldPath;

/**
 *
 * @author rene.zarwel
 */
public class GenericTable<T extends BaseEntity> extends CustomComponent {

    private final Class<T> entityClass;

    private final BeanItemContainer<T> container;
    private BuergerViewController controller;
    private Table table;

    // Navigation
    private String from;

    // Buttons
    List<TableActionButton.Builder> buttonBuilders;

    protected static final Logger LOG = LoggerFactory.getLogger(GenericTable.class);

    public GenericTable(final BuergerViewController controller, Class<T> entityClass, final TableActionButton.Builder... buttonBuilders) {

        this.entityClass = entityClass;

        this.controller = controller;
        this.buttonBuilders = Arrays.asList(buttonBuilders);

        // Have a container of some type to contain the data
        this.container = new BeanItemContainer<>(entityClass);

        // create table
        this.table = new Table();
        table.setContainerDataSource(this.container);

        //set action column
        table.addGeneratedColumn("button",
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

        table.setVisibleColumns(columnList.toArray());

        columnList.stream()
                .forEach(fieldName -> {

                    table.setColumnHeader(fieldName, controller.resolveRelative(getEntityFieldPath(fieldName, Type.column_header)));

                    table.setColumnIcon(fieldName, controller.resolveIcon(getEntityFieldPath(fieldName, Type.column_header)));
                });


        table.setColumnHeader("button", "");


        setCompositionRoot(table);
    }

    @Subscribe
    public void update(ComponentEvent<T> event) {

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

    public void add(Optional<T> entity) {
        if(entity.isPresent()) {
            LOG.debug("added entity to table.");
            this.container.addBean(entity.get());
        }
    }

    public void addAll(List<T> entity) {
        LOG.debug("added search result");
        this.container.removeAllItems();
        this.container.addAll(entity);
    }

    public void delete(Object id) {
        LOG.debug("deleted buerger from table.");
        this.container.removeItem(id);
    }

    /**
     * "SimpleAction" Buttons für jede Tabellenzeile. In jeder Tabellenzeile
     * gibt "SimpleAction" Buttons.
     *
     * @param id ID
     * @return HL
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
     * @return Liste der {@link TableActionButton.Builder}
     */
    public List<TableActionButton.Builder> getButtonBuilders() {
        return buttonBuilders;
    }

}
