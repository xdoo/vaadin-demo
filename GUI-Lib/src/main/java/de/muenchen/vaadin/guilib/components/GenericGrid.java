package de.muenchen.vaadin.guilib.components;

import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.components.actions.EntityActions;
import de.muenchen.vaadin.guilib.components.actions.EntityListActions;
import de.muenchen.vaadin.guilib.components.actions.EntitySingleActions;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.components.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.controller.EntityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

/**
 * Created by rene.zarwel on 07.10.15.
 *
 * @param <T> the entity
 */
@SuppressWarnings("unchecked")
public class GenericGrid<T> extends CustomComponent {
    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GenericGrid.class);
    /**
     * Components Layout
     */
    final HorizontalLayout topComponentsLayout = new HorizontalLayout();
    /** Default Components **/
    private Grid grid = new Grid();
    //----------- Search Components
    private Optional<CssLayout> searchLayout = Optional.empty();
    private TextField filter = new TextField();
    private Button search;
    private Button reset;
    //-------------
    private Optional<ActionButton> read = Optional.empty();
    private Optional<ActionButton> edit = Optional.empty();
    private Optional<ActionButton> copy = Optional.empty();
    private Optional<ActionButton> delete = Optional.empty();
    private Optional<ActionButton> create = Optional.empty();
    /** Custom Buttons **/
    private List<Button> customSingleSelectButtons = new ArrayList<>();
    private List<Button> customMultiSelectButtons = new ArrayList<>();
    private List<Button> customButtons = new ArrayList<>();
    /** Controller **/
    private EntityController controller;

    /**
     * Constructor of Grid with default configuration (no Buttons just grid).
     *
     * @param controller Controller of current context
     * @param dataStore  Datastore of this grid
     * @param fields     Fields to show.
     */
    public GenericGrid(EntityController controller, BeanItemContainer<T> dataStore, String[] fields) {
        this.controller = controller;

        grid.setContainerDataSource(dataStore);

        //----------- Grid Configuration
        grid.setColumns(fields);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selectionEvent -> setButtonVisability());
        grid.setVisible(Boolean.TRUE);
        grid.setWidth("100%");
        grid.setHeightByRows(10);
        //Grid Selection
        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                boolean isClicked = grid.isSelected(itemClickEvent.getItemId());
                if (!itemClickEvent.isCtrlKey()) {
                    grid.getSelectedRows().stream().forEach(grid::deselect);
                }
                if (!isClicked)
                    grid.select(itemClickEvent.getItemId());
                else
                    grid.deselect(itemClickEvent.getItemId());
            }
        });

        // HACK:
        // Change Buttonvisibility and RowSelection if
        // datastore is changed elsewhere
        dataStore.addItemSetChangeListener(event -> {
            grid.getSelectedRows().stream()
                    .filter(itemID -> !event.getContainer().containsId(itemID))
                    .forEach(grid::deselect);
            setButtonVisability();
        });

        this.grid.getColumns().stream().forEach(c ->
                        c.setHidable(true)
        );

        Stream.of(fields).forEach(field ->
                        this.grid.getColumn(field).setHeaderCaption(controller.getResolver().resolveRelative(getEntityFieldPath(field, I18nPaths.Type.column_header)))
        );
        //---------------

        //----------- ComponentsLayout Configuration
        topComponentsLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        //topComponentsLayout.setSizeFull();
        topComponentsLayout.setSpacing(true);
        //--------------

        // Assemble GenericGrid
        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(topComponentsLayout);
        layout.addComponent(grid);
        layout.setSpacing(true);
        setCompositionRoot(layout);

        //Request Data for this Grid
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_LIST));

    }

    //-------------------------
    // Methods to create default components
    //-------------------------


    private void createRead(String navigateToRead) {
        ActionButton readButton = new ActionButton(controller.getResolver(), SimpleAction.read);

        readButton.addActionPerformer(getSingleActionOnSelected()::read);
        readButton.addActionPerformer(getNavigateAction(navigateToRead)::navigate);

        readButton.setVisible(false);
        read = Optional.of(readButton);
    }

    private void createCopy() {
        ActionButton copyButton = new ActionButton(controller.getResolver(), SimpleAction.copy);

        copyButton.addActionPerformer(getListActionOnSelected()::create);
        copy = Optional.of(copyButton);
    }

    private void createDelete() {
        ActionButton deleteButton = new ActionButton(controller.getResolver(), SimpleAction.delete);

        deleteButton.addActionPerformer(getListActionOnSelected()::delete);
        delete = Optional.of(deleteButton);
    }

    private void createEdit(String navigateToEdit) {
        ActionButton editButton = new ActionButton(controller.getResolver(), SimpleAction.update);

        editButton.addActionPerformer(getSingleActionOnSelected()::read);
        editButton.addActionPerformer(getNavigateAction(navigateToEdit)::navigate);

        edit = Optional.of(editButton);
    }

    private void createCreate(String navigateToCreate) {
        ActionButton createButton = new ActionButton(controller.getResolver(), SimpleAction.create);

        createButton.addActionPerformer(getNavigateAction(navigateToCreate)::navigate);
        createButton.setVisible(Boolean.TRUE);

        create = Optional.of(createButton);
    }

    private void createSearch() {
        search = new Button(FontAwesome.SEARCH);
        search.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        search.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        search.addClickListener(getEntityAction()::readList);

        search.setId(String.format("%s_SEARCH_BUTTON", controller.getResolver().getBasePath()));
    }

    private void createFilter() {
        filter.setId(String.format("%s_QUERY_FIELD", controller.getResolver().getBasePath()));
        filter.focus();
        filter.setWidth("100%");
    }

    private void createReset() {
        reset = new Button(FontAwesome.TIMES);
        reset.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        reset.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        reset.addClickListener(e -> {
            filter.setValue("");
            getEntityAction().readList(e);
        });
        reset.setId(String.format("%s_RESET_BUTTON", controller.getResolver().getBasePath()));
    }

    private void setButtonVisability() {
        int size = grid.getSelectedRows().size();
        if (read.isPresent()) read.get().setVisible(size == 1);
        if (edit.isPresent()) edit.get().setVisible(size == 1);
        if (copy.isPresent()) copy.get().setVisible(size > 0);
        if (delete.isPresent()) delete.get().setVisible(size > 0);

        customSingleSelectButtons.stream().forEach(button -> button.setVisible(size == 1));
        customMultiSelectButtons.stream().forEach(button -> button.setVisible(size > 0));
    }

    //--------------
    //Configuration-Methods
    //--------------

    /**
     * Activate double click to read entity.
     *
     * @param navigateToRead the navigate to read
     * @return the generic grid
     */
    public GenericGrid<T> activateDoubleClickToRead(String navigateToRead) {
        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    T entity = ((BeanItem<T>) itemClickEvent.getItem()).getBean();
                    controller
                            .getEventbus()
                            .notify(controller
                                    .getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(entity));
                    controller.getNavigator().navigateTo(navigateToRead);
                }
            }
        });
        return this;
    }

    /**
     * Add double click listener. The Consumer gets the entity.
     *
     * @param consumer the consumer
     * @return the generic grid
     */
    public GenericGrid<T> addDoubleClickListener(Consumer consumer) {
        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.isDoubleClick()) {
                consumer.accept(((BeanItem<T>) grid.getContainerDataSource().getItem(itemClickEvent.getItemId())).getBean());
            }
        });
        return this;
    }

    /**
     * Activate search on generic grid.
     *
     * @return the generic grid
     */
    public GenericGrid<T> activateSearch() {

        if (!searchLayout.isPresent()) {
            //Configure layout
            CssLayout cssLayout = new CssLayout();
            cssLayout.addStyleName("v-component-group");
            cssLayout.setSizeFull();
            //Set Components
            createSearch();
            createFilter();
            createReset();
            cssLayout.addComponents(filter, search, reset);
            searchLayout = Optional.of(cssLayout);
        } else {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(searchLayout.get());
        }
        topComponentsLayout.addComponent(searchLayout.get());

        setButtonVisability();
        return this;
    }


    /**
     * Activate create entity on generic grid.
     *
     * @param navigateToCreate the navigate to create
     * @return the generic grid
     */
    public GenericGrid<T> activateCreate(String navigateToCreate) {
        if (create.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(create.get());
        }
        createCreate(navigateToCreate);
        topComponentsLayout.addComponent(create.get());

        setButtonVisability();
        return this;
    }


    /**
     * Activate read button on generic grid.
     *
     * @param navigateToRead the navigate to read
     * @return the generic grid
     */
    public GenericGrid<T> activateRead(String navigateToRead) {
        if (read.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(read.get());
        }

        createRead(navigateToRead);
        topComponentsLayout.addComponent(read.get());

        setButtonVisability();
        return this;
    }


    /**
     * Activate copy of entities on generic grid.
     *
     * @return the generic grid
     */
    public GenericGrid<T> activateCopy() {
        if (copy.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(copy.get());
        } else {
            createCopy();
        }
        topComponentsLayout.addComponent(copy.get());

        setButtonVisability();
        return this;
    }

    /**
     * Activate edit button on generic grid.
     *
     * @param navigateToEdit the navigate to edit
     * @return the generic grid
     */
    public GenericGrid<T> activateEdit(String navigateToEdit) {
        if (edit.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(edit.get());
        }
        createEdit(navigateToEdit);
        topComponentsLayout.addComponent(edit.get());

        setButtonVisability();
        return this;
    }

    /**
     * Activate delete of entities on generic grid.
     *
     * @return the generic grid
     */
    public GenericGrid activateDelete() {
        if (delete.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(delete.get());
        } else {
            createDelete();
        }
        topComponentsLayout.addComponent(delete.get());

        setButtonVisability();
        return this;
    }

    /**
     * Add custom multi select button on generic grid.
     *
     * @param buttonName the button name
     * @param consumer   the consumer
     * @return the generic grid
     */
    public GenericGrid<T> addMultiSelectButton(String buttonName, Consumer consumer) {
        Button button = new Button(buttonName);

        button.addClickListener(event -> {
            if (grid.getSelectedRows() != null) {
                consumer.accept(grid.getSelectedRows().stream()
                        .map(item -> ((BeanItem<T>) grid.getContainerDataSource().getItem(item)).getBean())
                        .collect(Collectors.toList()));
            }
        });

        addMultiSelectButton(button);
        return this;
    }

    /**
     * Add custom multi select button on generic grid.
     *
     * @param button the button to add
     * @return the generic grid
     */
    public GenericGrid<T> addMultiSelectButton(Button button) {

        customMultiSelectButtons.add(button);
        topComponentsLayout.addComponent(button);

        setButtonVisability();
        return this;
    }

    /**
     * Add custom single select button on generic grid.
     *
     * @param buttonName the button name
     * @param consumer   the consumer
     * @return the generic grid
     */
    public GenericGrid<T> addSingleSelectButton(String buttonName, Consumer<T> consumer) {
        Button button = new Button(buttonName);

        button.addClickListener(event -> {
            if (grid.getSelectedRows().size() == 1) {
                consumer.accept(((BeanItem<T>) grid.getContainerDataSource().getItem(grid.getSelectedRows().toArray()[0])).getBean());
            }
        });

        addSingleSelectButton(button);
        return this;
    }

    /**
     * Add custom single select button on generic grid.
     *
     * @param button the button to add
     * @return the generic grid
     */
    public GenericGrid<T> addSingleSelectButton(Button button) {

        customSingleSelectButtons.add(button);
        topComponentsLayout.addComponent(button);

        setButtonVisability();
        return this;
    }

    /**
     * Add custom button on generic grid. The Button has its own business action with no connection to this grid.
     *
     * @param buttonName the button name
     * @param runnable   the runnable
     * @return the generic grid
     */
    public GenericGrid<T> addButton(String buttonName, Runnable runnable) {
        Button button = new Button(buttonName);

        button.addClickListener(event -> runnable.run());
        addButton(button);
        return this;
    }

    /**
     * Add custom button on generic grid. The Button has its own business action with no connection to this grid.
     *
     * @param button the button to add
     * @return the generic grid
     */
    public GenericGrid<T> addButton(Button button) {

        customButtons.add(button);
        topComponentsLayout.addComponent(button);

        button.setVisible(Boolean.TRUE);
        return this;
    }
    //--------------
    // Getter / Setter
    //--------------

    /**
     * Get all selected Entities of this grid.
     * @return selected Entities.
     */
    public List<T> getSelectedEntities(){
        return grid.getSelectedRows().stream()
                .map(item -> (BeanItem<T>) grid.getContainerDataSource().getItem(item))
                .map(BeanItem::getBean)
                .collect(Collectors.toList());
    }

    /**
     * Get a single selected Entitiy.
     * If more than one Entity is selected, this method will return the first one.
     *
     * @return single selected entitiy
     */
    public T getSelectedEntity(){
        return grid.getSelectedRows().stream()
                .map(item -> (BeanItem<T>) grid.getContainerDataSource().getItem(item))
                .map(BeanItem::getBean)
                .findFirst().get();
    }


    //--------------
    //intern Helper-Methods
    //--------------

    private EntitySingleActions getSingleActionOnSelected(){
        return new EntitySingleActions(
                getResolver(),
                this::getSelectedEntity,
                getEventbus(),
                getType()
        );
    }

    private EntityListActions getListActionOnSelected(){
        return new EntityListActions(
                () -> grid.getSelectedRows().stream()
                        .peek(grid::deselect)
                        .map(itemID -> (BeanItem<T>) grid.getContainerDataSource().getItem(itemID))
                        .map(BeanItem::getBean)
                        .collect(Collectors.toList()),
                getType(),
                getEventbus()
        );
    }

    private EntityActions getEntityAction(){
        return new EntityActions(
                filter::getValue,
                getEventbus(),
                getType()
        );
    }

    private NavigateActions getNavigateAction(String navigateTo){
        return new NavigateActions(getNavigator(), getEventbus(), navigateTo);
    }

    private Class<?> getType(){
        return ((AbstractBeanContainer)grid.getContainerDataSource()).getBeanType();
    }

    private I18nResolver getResolver(){
        return controller.getResolver();
    }

    private EventBus getEventbus(){
        return controller.getEventbus();
    }

    private Navigator getNavigator(){
        return controller.getNavigator();
    }

}
