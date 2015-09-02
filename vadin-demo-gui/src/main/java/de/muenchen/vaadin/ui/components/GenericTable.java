package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.demo.api.util.FieldIdentifier;
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
 * Generic table for all Entities.
 * @param <T>  the type of Entity
 * @author rene.zarwel
 */
public class GenericTable<T extends BaseEntity> extends CustomComponent {

    private final BeanItemContainer<T> container;
    private BuergerViewController controller;
    private Table table;

    // Navigation
    private String from;

    /**
     * The Button builders.
     */
    List<TableActionButton.Builder> buttonBuilders;

    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GenericTable.class);

    /**
     * Instantiates a new Generic table.
     *
     * @param controller the controller
     * @param entityClass the entity class
     * @param buttonBuilders the button builders
     */
    public GenericTable(final BuergerViewController controller, Class<T> entityClass, final TableActionButton.Builder... buttonBuilders) {

        this.controller = controller;
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
        if(entity.isPresent()) {
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
                .map((builder) -> builder.build(container,id))
                .forEachOrdered(layout::addComponent);

        layout.setSpacing(true);
        return layout;
    }

    // Members

    /**
     * Gets from.
     *
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets from.
     *
     * @param from the from
     */
    public void setFrom(String from) {
        this.from = from;
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

}
