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
import java.util.Map;
import java.util.function.Consumer;
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

    /** Components **/
    private Grid grid = new Grid();
    private TextField filter = new TextField();
    private Button search;
    private Button reset;
    private ActionButton read;
    private ActionButton edit;
    private ActionButton copy;
    private ActionButton delete;
    private ActionButton create;
    private Map<String, Button> customSingleSelectButtons = new HashMap<>();
    private Map<String, Button> customMultiSelectButtons = new HashMap<>();

    /**Components Layouts**/
    final CssLayout searchLayout = new CssLayout();
    final CssLayout buttonLayout = new CssLayout();
    final HorizontalLayout topComponentsLayout = new HorizontalLayout();

    /** Controller**/
    private EntityController controller;

    /** Navigation Paths **/
    private String navigateToRead;
    private String navigateToEdit;
    private String navigateToCreate;

    public GenericGrid(EntityController controller, BeanItemContainer<T> dataStore, String[] fields, String navigateToRead, String navigateToEdit, String navigateToCreate) {
        this.controller = controller;
        this.navigateToEdit = navigateToEdit;
        this.navigateToRead = navigateToRead;
        this.navigateToCreate = navigateToCreate;

        grid.setContainerDataSource(dataStore);

        createFilter();
        createReset();
        createSearch();
        createCreate();
        createRead();
        createEdit();
        createDelete();
        createCopy();

        grid.setColumns(fields);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);


        searchLayout.addStyleName("genericGrid-group");
        buttonLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);


        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    T buerger = ((BeanItem<T>) itemClickEvent.getItem()).getBean();
                    controller
                            .getEventbus()
                            .notify(controller
                                    .getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(buerger));
                    controller.getNavigator().navigateTo(navigateToRead);
                    return;
                }
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

        grid.addSelectionListener(selectionEvent -> setButtonVisability());

        searchLayout.addComponents(filter, search, reset);
        buttonLayout.addComponents(read, edit, copy, delete);
        buttonLayout.setSizeFull();
        searchLayout.setSizeFull();
        topComponentsLayout.addComponents(create, searchLayout, buttonLayout);

        topComponentsLayout.setSpacing(true);

        edit.setVisible(Boolean.FALSE);
        copy.setVisible(Boolean.FALSE);
        delete.setVisible(Boolean.FALSE);

        grid.setVisible(Boolean.TRUE);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(topComponentsLayout);
        layout.addComponent(grid);
        layout.setSpacing(true);

        // configure
        this.grid.setWidth("100%");
        this.grid.setHeightByRows(10);
        this.grid.getColumns().stream().forEach(c -> {
            c.setHidable(true);
        });

        Stream.of(fields).forEach(field ->
                        this.grid.getColumn(field).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(field, I18nPaths.Type.column_header)))
        );

        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_LIST));
        setCompositionRoot(layout);
    }

    private void createRead() {
        read = new ActionButton(controller, SimpleAction.read, null);
        read.addClickListener(clickEvent -> {
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(grid.getSelectedRows().toArray()[0]));
            controller.getNavigator().navigateTo(navigateToRead);
        });
        read.setVisible(false);
    }

    private void createCopy() {
        copy = new ActionButton(controller, SimpleAction.copy, null);
        copy.addClickListener(clickEvent -> {
            LOG.debug("copying selected items");
            if (grid.getSelectedRows() != null) {
                for (Object next : grid.getSelectedRows()) {
                    grid.deselect(next);
                    BeanItem<T> item = (BeanItem<T>) grid.getContainerDataSource().getItem(next);
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.CREATE), reactor.bus.Event.wrap(item.getBean()));
                }
            }
        });
    }

    private void createDelete() {
        delete = new ActionButton(controller, SimpleAction.delete, null);
        delete.addClickListener(clickEvent -> {
            LOG.debug("deleting selected items");
            if (grid.getSelectedRows() != null) {
                for (Object next : grid.getSelectedRows()) {
                    BeanItem<T> item = (BeanItem<T>) grid.getContainerDataSource().getItem(next);
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.DELETE), reactor.bus.Event.wrap(item.getBean()));
                    grid.deselect(next);
                }
            }
        });
    }

    private void createEdit() {
        edit = new ActionButton(controller, SimpleAction.update, navigateToEdit);
        edit.addClickListener(clickEvent -> {
            if (grid.getSelectedRows().size() != 1)
                return;
            LOG.debug("update selected");
            T buerger = ((BeanItem<T>) grid.getContainerDataSource().getItem(grid.getSelectedRows().toArray()[0])).getBean();
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(buerger));
            controller.getNavigator().navigateTo(navigateToEdit);
        });
    }

    private void createCreate() {
        create = new ActionButton(controller, SimpleAction.create, navigateToCreate);
        create.addClickListener(clickEvent -> {
            controller.getNavigator().navigateTo(navigateToCreate);
        });
        create.setVisible(Boolean.TRUE);
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
        read.setVisible(size == 1);
        edit.setVisible(size == 1);
        copy.setVisible(size > 0);
        delete.setVisible(size > 0);

        customSingleSelectButtons.values().stream().forEach(button -> button.setVisible(size == 1));
        customMultiSelectButtons.values().stream().forEach(button -> button.setVisible(size > 0));
    }

    //--------------
    //Configuration-Methods
    //--------------

    public void activateSearch(){
        //Remove to prevent double attachment
        topComponentsLayout.removeComponent(searchLayout);
        topComponentsLayout.addComponent(searchLayout);
    }
    public void deactivateSearch(){
        topComponentsLayout.removeComponent(searchLayout);
    }
    public void activateCreate(){
        //Remove to prevent double attachment
        topComponentsLayout.removeComponent(create);
        topComponentsLayout.addComponent(create);
    }
    public void deactivateCreate(){
        topComponentsLayout.removeComponent(create);
    }
    public void activateRead(){
        //Remove to prevent double attachment
        buttonLayout.removeComponent(read);
        buttonLayout.addComponent(read);
    }
    public void deactivateRead(){
        buttonLayout.removeComponent(read);
    }
    public void activateCopy(){
        //Remove to prevent double attachment
        buttonLayout.removeComponent(copy);
        buttonLayout.addComponent(copy);
    }
    public void deactivateCopy(){
        buttonLayout.removeComponent(copy);
    }
    public void activateEdit(){
        //Remove to prevent double attachment
        buttonLayout.removeComponent(edit);
        buttonLayout.addComponent(edit);
    }
    public void deactivateEdit(){
        buttonLayout.removeComponent(edit);
    }
    public void activateDelete(){
        //Remove to prevent double attachment
        buttonLayout.removeComponent(delete);
        buttonLayout.addComponent(delete);
    }
    public void deactivateDelete(){
        buttonLayout.removeComponent(delete);
    }
    public void addCustomMultiSelectButton(String buttonName, Consumer<T> consumer){
        Button button = new Button(buttonName);
        customMultiSelectButtons.put(buttonName, button);
        button.addClickListener(event -> {
            if (grid.getSelectedRows() != null) {
                grid.getSelectedRows().stream().map(item -> ((BeanItem<T>) grid.getContainerDataSource().getItem(item)).getBean())
                        .forEach(consumer::accept);
            }
        });
        buttonLayout.addComponent(button);
        setButtonVisability();
    }
    public void addCustomSingleSelectButton(String buttonName, Consumer<T> consumer){
        Button button = new Button(buttonName);
        customSingleSelectButtons.put(buttonName, button);
        button.addClickListener(event -> {
            if (grid.getSelectedRows().size() != 1) {
                consumer.accept(((BeanItem<T>) grid.getContainerDataSource().getItem(grid.getSelectedRows().toArray()[0])).getBean());
            }
        });
        buttonLayout.addComponent(button);
        setButtonVisability();
    }
    public void releaseCustomButton(String buttonName){
        //Remove button from singleSelect if button is single select
        Button button = customSingleSelectButtons.remove(buttonName);

        //Otherwise remove button from multiSelect if button is multi select
        if(button == null)
            button = customMultiSelectButtons.remove(buttonName);

        if(button != null)
            buttonLayout.removeComponent(button);

    }
}
