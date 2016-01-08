package de.muenchen.vaadin.guilib.components;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.actions.EntityActions;
import de.muenchen.vaadin.guilib.components.actions.EntityListActions;
import de.muenchen.vaadin.guilib.components.actions.EntitySingleActions;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.util.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

/**
 * Created by rene.zarwel on 07.10.15.
 *
 * @param <T> the entity
 */
@SuppressWarnings("unchecked")
public class GenericGrid<T> extends BaseComponent {
    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GenericGrid.class);
    /**
     * Components Layout
     */
    final HorizontalLayout topComponentsLayout = new HorizontalLayout(){
        @Override
        public void addComponent(Component c) {
            super.addComponent(c);
            this.setVisible(true);
        }
    };
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
    private final List<Component> customSingleSelectComponents = new ArrayList<>();
    private final List<Component> customMultiSelectComponents = new ArrayList<>();
    private final List<Component> customComponents = new ArrayList<>();

    private Class<T> entityClass;

    private List<String> fields;
    private Map<String,PropertyValueGenerator> propertyValueGenerators = new HashMap<>();

    /**
     * Constructor of Grid with default configuration (no Buttons just grid).
     *
     * @param entityClass Class of entity
     * @param fields    Fields to show.
     */
    public GenericGrid(Class<T> entityClass, String[] fields){
        this.entityClass = entityClass;

        grid.setContainerDataSource(new GeneratedPropertyContainer(grid.getContainerDataSource()));

        getEventBus()
                .on(new ResponseEntityKey(entityClass).toSelector(), this::datastoreEventhandler)
                .cancelAfterUse();



        init(fields);
    }

    /**
     * Constructor of Grid with default configuration (no Buttons just grid).
     *
     * @param dataStore Datastore of this grid
     * @param fields    Fields to show.
     */
    public GenericGrid(BeanItemContainer<T> dataStore, String[] fields) {

        this.entityClass = ((AbstractBeanContainer) dataStore).getBeanType();

        init(fields);

        setDatastore(dataStore);

    }

    private void init(String[] fields){
        //----------- Grid Configuration
        this.fields = Arrays.asList(fields);

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selectionEvent -> setButtonVisability());
        grid.setVisible(Boolean.TRUE);
        grid.setWidth("100%");
        grid.setHeightByRows(10);
        grid.setHeightMode(HeightMode.ROW);

        configureGridSelection();

        //----------- ComponentsLayout Configuration
        topComponentsLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        topComponentsLayout.setSpacing(true);
        topComponentsLayout.setVisible(false);
        //--------------

        // Assemble GenericGrid
        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(topComponentsLayout);
        layout.addComponent(grid);
        layout.setSpacing(true);
        setCompositionRoot(layout);

        //Request Data for this Grid
        getEventBus().notify(new RequestEntityKey(RequestEvent.READ_LIST, getType()));
    }

    /**
     *
     */
    private void configureGridSelection() {
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
    }

    //-------------------------
    // Methods to create default components
    //-------------------------


    private void createRead(String navigateToRead) {
        ActionButton readButton = new ActionButton(getType(), SimpleAction.read);

        readButton.addActionPerformer(getSingleActionOnSelected()::read);
        readButton.addActionPerformer(getNavigateAction(navigateToRead)::navigate);

        readButton.setEnabled(false);
        read = Optional.of(readButton);
    }

    private void createCopy() {
        ActionButton copyButton = new ActionButton(getType(), SimpleAction.copy);
        copyButton.useNotification(true);

        copyButton.addActionPerformer(getListActionOnSelected()::create);
        copy = Optional.of(copyButton);
    }

    private void createDelete() {
        ActionButton deleteButton = new ActionButton(getType(), SimpleAction.delete);
        deleteButton.useNotification(true);

        deleteButton.addActionPerformer(getListActionOnSelected()::delete);
        delete = Optional.of(deleteButton);
    }

    private void createEdit(String navigateToEdit) {
        ActionButton editButton = new ActionButton(getType(), SimpleAction.update);

        editButton.addActionPerformer(getSingleActionOnSelected()::read);
        editButton.addActionPerformer(getNavigateAction(navigateToEdit)::navigate);

        edit = Optional.of(editButton);
    }

    private void createCreate(String navigateToCreate) {
        ActionButton createButton = new ActionButton(getType(), SimpleAction.create);

        createButton.addActionPerformer(getNavigateAction(navigateToCreate)::navigate);
        createButton.setEnabled(Boolean.TRUE);

        create = Optional.of(createButton);
    }


    private void createSearch(boolean nonRelation) {
        search = new Button(FontAwesome.SEARCH);
        search.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        search.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        if(nonRelation)
        	search.addClickListener(getEntityAction()::readList);
        else{
        	search.addClickListener(clickEvent -> relationFilter(filter.getValue()));
        	filter.addTextChangeListener(changeEvent -> {
        		final String query = changeEvent.getText();
        		relationFilter(query);
        	});

        }
        search.setId(String.format("%s_SEARCH_BUTTON", BaseUI.getCurrentI18nResolver().getBasePath(getType())));
    }

    private void createFilter() {
        filter.setId(String.format("%s_QUERY_FIELD", BaseUI.getCurrentI18nResolver().getBasePath(getType())));
        filter.setWidth("100%");
    }

    private void relationFilter(String query){
    	((GeneratedPropertyContainer)grid.getContainerDataSource()).removeAllContainerFilters();
		((GeneratedPropertyContainer)grid.getContainerDataSource()).addContainerFilter(new Filter(){
			@Override
			public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
				boolean result = true;
				if(query==null||query.equals(" "))
					return true;
				List<String> queryStrings = Arrays.asList(query.split(" "));
					for(String next : queryStrings){
						result &= item.toString().contains(next);
					}
				return result;
			}
			@Override
			public boolean appliesToProperty(Object propertyId) {
				return true;
			}
    	});
    }

    private void createReset() {
        reset = new Button(FontAwesome.TIMES);
        reset.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        reset.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        reset.addClickListener(e -> {
            filter.setValue("");
    		((GeneratedPropertyContainer)grid.getContainerDataSource()).removeAllContainerFilters();
            getEntityAction().readList(e);
        });
        reset.setId(String.format("%s_RESET_BUTTON", BaseUI.getCurrentI18nResolver().getBasePath(getType())));
    }

    private void setButtonVisability() {
        int size = grid.getSelectedRows().size();
        if (read.isPresent()) read.get().setEnabled(size == 1);
        if (edit.isPresent()) edit.get().setEnabled(size == 1);
        if (copy.isPresent()) copy.get().setEnabled(size > 0);
        if (delete.isPresent()) delete.get().setEnabled(size > 0);

        customSingleSelectComponents.stream().forEach(component -> component.setEnabled(size == 1));
        customMultiSelectComponents.stream().forEach(component -> component.setEnabled(size > 0));
    }

    //--------------
    //Configuration-Methods
    //--------------

    public GenericGrid<T> setHeightByRows(int rows){
        grid.setHeightByRows(rows);
        return this;
    }

    public GenericGrid<T> setSelectionMode(Grid.SelectionMode mode){
        grid.setSelectionMode(mode);
        return this;
    }

    public GenericGrid<T> setColumnsHidable(boolean hidable){
        this.grid.getColumns().stream().forEach(c ->
                c.setHidable(hidable)
        );
        return this;
    }

    /**
     * Activate double click to read entity.
     *
     * @param navigateToRead the navigate to read
     * @return the generic grid
     */
    public GenericGrid<T> activateDoubleClickToRead(String navigateToRead) {
        NavigateActions navigateAction = new NavigateActions(navigateToRead);
        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    T entity = ((BeanItem<T>) ( (GeneratedPropertyContainer) grid.getContainerDataSource())
                            .getWrappedContainer()
                            .getItem(itemClickEvent.getItemId()))
                            .getBean();

                    getEventBus()
                            .notify(new RequestEntityKey(RequestEvent.READ_SELECTED, getType()), reactor.bus.Event.wrap(entity));
                    navigateAction.navigate();
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
                consumer.accept(
                        ((BeanItem<T>) ( (GeneratedPropertyContainer) grid.getContainerDataSource())
                                .getWrappedContainer()
                                .getItem(itemClickEvent.getItemId()))
                                .getBean());
            }
        });
        return this;
    }

    /**
     * Add a selection Listener.
     * @param listener the listener
     * @return the generic grid
     */
    public GenericGrid<T> addSelectionListener(SelectionEvent.SelectionListener listener) {
        grid.addSelectionListener(listener);
        return this;
    }

    public GenericGrid<T> activateSearch(){
    	return activateSearch(true);
    }

    /**
     * Activate search on generic grid.
     *
     * @return the generic grid
     */
    public GenericGrid<T> activateSearch(boolean nonRelation) {

        if (!searchLayout.isPresent()) {
            //Configure layout
            CssLayout cssLayout = new CssLayout();
            cssLayout.addStyleName("v-component-group");
            cssLayout.setSizeFull();
            //Set Components
            createSearch(nonRelation);
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
                        .map(item -> (
                                ((GeneratedPropertyContainer) grid.getContainerDataSource())
                                        .getWrappedContainer()
                                        .getItem(item)))
                        .map(item -> ((BeanItem<T>) item).getBean())
                        .collect(Collectors.toList()));
            }
        });

        addMultiSelectComponent(button);
        return this;
    }

    /**
     * Add custom multi select component on generic grid.
     *
     * @param component the component to add
     * @return the generic grid
     */
    public GenericGrid<T> addMultiSelectComponent(Component component) {

        customMultiSelectComponents.add(component);
        topComponentsLayout.addComponent(component);

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
                consumer.accept(
                        ((BeanItem<T>) ( (GeneratedPropertyContainer) grid.getContainerDataSource())
                                .getWrappedContainer()
                                .getItem(grid.getSelectedRows().toArray()[0]))
                                .getBean());
            }
        });

        addSingleSelectComponent(button);
        return this;
    }

    /**
     * Add custom single select component on generic grid.
     *
     * @param component the component to add
     * @return the generic grid
     */
    public GenericGrid<T> addSingleSelectComponent(Component component) {

        customSingleSelectComponents.add(component);
        topComponentsLayout.addComponent(component);

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
        addComponent(button);
        return this;
    }

    /**
     * Add custom component on generic grid. The Button has its own business action with no connection to this grid.
     *
     * @param component the component to add
     * @return the generic grid
     */
    public GenericGrid<T> addComponent(Component component) {

        customComponents.add(component);
        topComponentsLayout.addComponent(component);

        component.setEnabled(Boolean.TRUE);
        return this;
    }

    /**
     * Create a new Column with a generated Property.
     * The generator gets the Item of this Row and should
     * generate a cool new Value.
     *
     * @param propertyId Name of the column
     * @param propertyClass Class of the property Item
     * @param generator Generator for Property Item
     * @param <E> Type of property Item
     * @return This Grid for further config
     */
    public <E> GenericGrid<T> addGeneratedColumn(String propertyId, Class<E> propertyClass, Function<T, E> generator){

        GeneratedPropertyContainer container = (GeneratedPropertyContainer) grid.getContainerDataSource();

        if(!propertyValueGenerators.containsKey(propertyId)) {

            propertyValueGenerators.put(propertyId, new PropertyValueGenerator<E>() {
                @Override
                public E getValue(Item item, Object itemId, Object propertyId) {
                    return generator.apply((T) itemId);
                }

                @Override
                public Class<E> getType() {
                    return propertyClass;
                }
            });

            container.addGeneratedProperty(propertyId, propertyValueGenerators.get(propertyId));

            this.grid.getColumn(propertyId)
                    .setHeaderCaption(BaseUI.getCurrentI18nResolver().resolveRelative(getType(), getEntityFieldPath(propertyId, I18nPaths.Type.column_header)));

        }

        return this;
    }

    /**
     * Removes a generated property.
     *
     * @param propertyId Name of property
     * @return This Grid for further config
     */
    public GenericGrid<T> removeGeneratedColumn(String propertyId){
        ((GeneratedPropertyContainer) grid.getContainerDataSource())
            .removeGeneratedProperty(propertyId);

        propertyValueGenerators.remove(propertyId);

        return this;
    }

    /**
     * Removes a non generated property.
     *
     * @param propertyId Name of property
     * @return This Grid for further config
     */
    public GenericGrid<T> removeColumn(String propertyId){
        grid.getContainerDataSource().removeContainerProperty(propertyId);

        return this;
    }

    //--------------
    // Getter / Setter
    //--------------

    /**
     * Get all selected Entities of this grid.
     * @return selected Entities.
     */
    public List<T> getSelectedEntities() {
        return grid.getSelectedRows().stream()
                .map(item ->
                        (BeanItem<T>) ( (GeneratedPropertyContainer) grid.getContainerDataSource())
                                .getWrappedContainer()
                                .getItem(item))
                .map(BeanItem::getBean)
                .collect(Collectors.toList());
    }

    /**
     * Get a single selected Entitiy.
     * If more than one Entity is selected, this method will return the first one.
     *
     * @return single selected entitiy
     */
    public T getSelectedEntity() {
        return grid.getSelectedRows().stream()
                .map(item ->
                        (BeanItem<T>) ( (GeneratedPropertyContainer) grid.getContainerDataSource())
                                .getWrappedContainer()
                                .getItem(item))
                .map(BeanItem::getBean)
                .findFirst().get();
    }

    public Optional<ActionButton> getCreate() {
        return create;
    }

    public Optional<ActionButton> getCopy() {
        return copy;
    }

    public Optional<ActionButton> getRead() {
        return read;
    }

    public Optional<ActionButton> getDelete() {
        return delete;
    }

    public Optional<ActionButton> getEdit() {
        return edit;
    }

    //--------------
    //intern Helper-Methods
    //--------------

    private EntitySingleActions getSingleActionOnSelected() {
        return new EntitySingleActions(
                this::getSelectedEntity,
                getType()
        );
    }

    private EntityListActions getListActionOnSelected() {
        return new EntityListActions(
                () -> grid.getSelectedRows().stream()
                        .peek(grid::deselect)
                        .map(item ->
                                (BeanItem<T>) ( (GeneratedPropertyContainer) grid.getContainerDataSource())
                                        .getWrappedContainer()
                                        .getItem(item))
                        .map(BeanItem::getBean)
                        .collect(Collectors.toList()),
                getType()
        );
    }

    private EntityActions getEntityAction() {
        return new EntityActions(
                filter::getValue,
                getType()
        );
    }

    private NavigateActions getNavigateAction(String navigateTo) {
        return new NavigateActions(navigateTo);
    }

    private Class<?> getType() {
        return entityClass;
    }

    private void setDatastore(BeanItemContainer<T> dataStore) {

        GeneratedPropertyContainer container = new GeneratedPropertyContainer(dataStore);


        //Removes Entity specific content if present
        if (dataStore.getContainerPropertyIds().contains("id")){
            container.removeContainerProperty("id");
        }
        if (dataStore.getContainerPropertyIds().contains("links")){
            container.removeContainerProperty("links");
        }

        grid.setContainerDataSource(container);

        //Remove Columns from Conatiner which are not present in Field enum
        container.getContainerPropertyIds().stream()
                .filter(property -> !fields.contains(property))
                .forEach(container::removeContainerProperty);


        grid.setColumnOrder(fields.toArray());

        fields.forEach(field ->
                this.grid.getColumn(field).setHeaderCaption(BaseUI.getCurrentI18nResolver().resolveRelative(getType(), getEntityFieldPath(field, I18nPaths.Type.column_header)))
        );

        // HACK:
        // Change Buttonvisibility and RowSelection if
        // datastore is changed elsewhere
        dataStore.addItemSetChangeListener(event -> {
            grid.getSelectedRows().stream()
                    .filter(itemID -> !event.getContainer().containsId(itemID))
                    .forEach(grid::deselect);
            setButtonVisability();
        });
    }

    private void datastoreEventhandler(reactor.bus.Event<?> event) {

       Datastore container = (Datastore) event.getData();
       setDatastore(container.getEntityContainer());

    }

}
