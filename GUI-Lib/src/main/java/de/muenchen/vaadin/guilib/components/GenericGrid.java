package de.muenchen.vaadin.guilib.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.controller.EntityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

/**
 * Created by rene.zarwel on 07.10.15.
 */
@SuppressWarnings("unchecked")
public class GenericGrid<T> extends CustomComponent {
    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GenericGrid.class);

    /**Default Components **/
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

    /**Custom Buttons**/
    private Map<String, Button> customSingleSelectButtons = new HashMap<>();
    private Map<String, Button> customMultiSelectButtons = new HashMap<>();
    private Map<String, Button> customButtons = new HashMap<>();

    /**Components Layout**/
    final HorizontalLayout topComponentsLayout = new HorizontalLayout();

    /** Controller**/
    private EntityController controller;

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

        this.grid.getColumns().stream().forEach(c ->
                        c.setHidable(true)
        );

        Stream.of(fields).forEach(field ->
                        this.grid.getColumn(field).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(field, I18nPaths.Type.column_header)))
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
        ActionButton readButton = new ActionButton(controller, SimpleAction.read, null);
        readButton.addClickListener(clickEvent -> {
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(grid.getSelectedRows().toArray()[0]));
            controller.getNavigator().navigateTo(navigateToRead);
        });
        readButton.setVisible(false);
        read = Optional.of(readButton);
    }

    private void createCopy() {
        ActionButton copyButton = new ActionButton(controller, SimpleAction.copy, null);
        copyButton.addClickListener(clickEvent -> {
            LOG.debug("copying selected items");
            if (grid.getSelectedRows() != null) {
                grid.getSelectedRows().stream()
                        .peek(grid::deselect)
                        .map(itemID -> (BeanItem<T>) grid.getContainerDataSource().getItem(itemID))
                        .forEach(beanItem ->
                                        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.CREATE), reactor.bus.Event.wrap(beanItem.getBean()))
                        );


            }
        });
        copy = Optional.of(copyButton);
    }

    private void createDelete() {
        ActionButton deleteButton = new ActionButton(controller, SimpleAction.delete, null);
        deleteButton.addClickListener(clickEvent -> {
            LOG.debug("deleting selected items");
            if (grid.getSelectedRows() != null) {
                grid.getSelectedRows().stream()
                        .peek(grid::deselect)
                        .map(itemID -> (BeanItem<T>) grid.getContainerDataSource().getItem(itemID))
                        .forEach(beanItem ->
                                        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.DELETE), reactor.bus.Event.wrap(beanItem.getBean()))
                        );


            }
        });
        delete = Optional.of(deleteButton);
    }

    private void createEdit(String navigateToEdit) {
        ActionButton editButton = new ActionButton(controller, SimpleAction.update, navigateToEdit);
        editButton.addClickListener(clickEvent -> {
            if (grid.getSelectedRows().size() == 1) {
                LOG.debug("update selected");
                T buerger = ((BeanItem<T>) grid.getContainerDataSource().getItem(grid.getSelectedRows().toArray()[0])).getBean();
                controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(buerger));
                controller.getNavigator().navigateTo(navigateToEdit);
            }
        });
        edit = Optional.of(editButton);
    }

    private void createCreate(String navigateToCreate) {
        ActionButton createButton = new ActionButton(controller, SimpleAction.create, navigateToCreate);
        createButton.addClickListener(clickEvent -> {
            controller.getNavigator().navigateTo(navigateToCreate);
        });
        createButton.setVisible(Boolean.TRUE);
        create = Optional.of(createButton);
    }

    private void createSearch() {
        search = new Button(FontAwesome.SEARCH);
        search.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        search.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        search.addClickListener(e -> {
            LOG.debug("search clicked: " + filter.getValue());
            if (filter.getValue() != null && filter.getValue().length() > 0)
                controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_LIST), reactor.bus.Event.wrap(filter.getValue()));
            else
                reset.click();
        });
        search.setId(String.format("%s_SEARCH_BUTTON", controller.getBasePath()));
    }

    private void createFilter() {
        filter.setId(String.format("%s_QUERY_FIELD", controller.getBasePath()));
        filter.focus();
        filter.setWidth("100%");
    }

    private void createReset() {
        reset = new Button(FontAwesome.TIMES);
        reset.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        reset.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        reset.addClickListener(e -> {
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_LIST));
            filter.setValue("");
        });
        reset.setId(String.format("%s_RESET_BUTTON", controller.getBasePath()));
    }

    private void setButtonVisability() {
        int size = grid.getSelectedRows().size();
        if(read.isPresent()) read.get().setVisible(size == 1);
        if(edit.isPresent()) edit.get().setVisible(size == 1);
        if(copy.isPresent()) copy.get().setVisible(size > 0);
        if(delete.isPresent()) delete.get().setVisible(size > 0);

        customSingleSelectButtons.values().stream().forEach(button -> button.setVisible(size == 1));
        customMultiSelectButtons.values().stream().forEach(button -> button.setVisible(size > 0));
    }

    //--------------
    //Configuration-Methods
    //--------------

    public GenericGrid activateDoubleClickToRead(String navigateToRead){
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
    public GenericGrid addDoubleClickListener(Consumer consumer){
        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.isDoubleClick()) {
                consumer.accept(((BeanItem<T>) grid.getContainerDataSource().getItem(itemClickEvent.getItemId())).getBean());
            }
        });
        return this;
    }

    public GenericGrid activateSearch(){

        if(!searchLayout.isPresent()){
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
    public GenericGrid deactivateSearch(){
        if(searchLayout.isPresent())
            topComponentsLayout.removeComponent(searchLayout.get());
        return this;
    }
    public GenericGrid activateCreate(String navigateToCreate){
        if(create.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(create.get());
        }
        createCreate(navigateToCreate);
        topComponentsLayout.addComponent(create.get());

        setButtonVisability();
        return this;
    }
    public GenericGrid deactivateCreate(){
        if(create.isPresent()) {
            topComponentsLayout.removeComponent(create.get());
            create = Optional.empty();
        }
        return this;
    }
    public GenericGrid activateRead(String navigateToRead){
        if(read.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(read.get());
        }

        createRead(navigateToRead);
        topComponentsLayout.addComponent(read.get());

        setButtonVisability();
        return this;
    }
    public GenericGrid deactivateRead(){
        if(edit.isPresent()) {
            topComponentsLayout.removeComponent(read.get());
            read = Optional.empty();
        }
        return this;
    }
    public GenericGrid activateCopy(){
        if(copy.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(copy.get());
        } else {
            createCopy();
        }
        topComponentsLayout.addComponent(copy.get());

        setButtonVisability();
        return this;
    }
    public GenericGrid deactivateCopy(){
        if(copy.isPresent()) {
            topComponentsLayout.removeComponent(copy.get());
        }
        return this;
    }
    public GenericGrid activateEdit(String navigateToEdit){
        if(edit.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(edit.get());
        }
        createEdit(navigateToEdit);
        topComponentsLayout.addComponent(edit.get());

        setButtonVisability();
        return this;
    }
    public GenericGrid deactivateEdit(){
        if(edit.isPresent()) {
            topComponentsLayout.removeComponent(edit.get());
            edit = Optional.empty();
        }
        return this;
    }
    public GenericGrid activateDelete(){
        if(delete.isPresent()) {
            //Remove to prevent double attachment
            topComponentsLayout.removeComponent(delete.get());
        } else {
            createDelete();
        }
        topComponentsLayout.addComponent(delete.get());

        setButtonVisability();
        return this;
    }
    public GenericGrid deactivateDelete(){
        if(delete.isPresent()) {
            topComponentsLayout.removeComponent(delete.get());
        }
        return this;
    }
    public GenericGrid addCustomMultiSelectButton(String buttonName, Consumer<List<T>> consumer){
        Button button = new Button(buttonName);
        customMultiSelectButtons.put(buttonName, button);
        button.addClickListener(event -> {
            if (grid.getSelectedRows() != null) {
                consumer.accept(grid.getSelectedRows().stream()
                            .map(item -> ((BeanItem<T>) grid.getContainerDataSource().getItem(item)).getBean())
                            .collect(Collectors.toList()));
            }
        });
        topComponentsLayout.addComponent(button);
        setButtonVisability();
        return this;
    }
    public GenericGrid addCustomSingleSelectButton(String buttonName, Consumer<T> consumer){
        Button button = new Button(buttonName);
        customSingleSelectButtons.put(buttonName, button);
        button.addClickListener(event -> {
            if (grid.getSelectedRows().size() == 1) {
                consumer.accept(((BeanItem<T>) grid.getContainerDataSource().getItem(grid.getSelectedRows().toArray()[0])).getBean());
            }
        });
        topComponentsLayout.addComponent(button);
        setButtonVisability();
        return this;
    }

    public GenericGrid addCustomButton(String buttonName, Runnable runnable){
        Button button = new Button(buttonName);
        customButtons.put(buttonName, button);
        button.addClickListener(event -> runnable.run());
        topComponentsLayout.addComponent(button);
        button.setVisible(Boolean.TRUE);
        return this;
    }

    public GenericGrid releaseCustomButton(String buttonName){
        //Remove button from singleSelect if button is single select
        Button button = customSingleSelectButtons.remove(buttonName);

        //Otherwise remove button from multiSelect if button is multi select
        if(button == null)
            button = customMultiSelectButtons.remove(buttonName);

        //Otherwise remove button from custom buttons
        if(button == null)
            button = customButtons.remove(buttonName);

        if(button != null)
            topComponentsLayout.removeComponent(button);

        return this;

    }

}
