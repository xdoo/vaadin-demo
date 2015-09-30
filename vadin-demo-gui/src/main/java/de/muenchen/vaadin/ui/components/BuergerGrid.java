package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.types.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.ui.app.views.BuergerCreateView;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.app.views.BuergerUpdateView;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

/**
 *
 * @author claus.straube, arne.schoentag
 */
public class BuergerGrid extends CustomComponent {

    protected static final Logger LOG = LoggerFactory.getLogger(BuergerGrid.class);
    private BuergerViewController controller;

    private GenericGrid grid;
    private TextField filter = new TextField();
    private Button search;
    private Button reset;
    private ActionButton edit;
    private ActionButton copy;
    private ActionButton delete;
    private ActionButton create;

    public BuergerGrid(final BuergerViewController controller) {

        this.controller = controller;

        VerticalLayout layout = new VerticalLayout();
        this.grid = controller.getViewFactory().generateGrid();

        createFilter();
        createReset();
        createSearch();
        createCreate();
        createEdit();
        createDelete();
        createCopy();

        grid.setColumns(Buerger.Field.getProperties());
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        CssLayout filterLayout = new CssLayout();
        filterLayout.addStyleName("v-component-group");
        CssLayout buttonlayout = new CssLayout();
        buttonlayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        HorizontalLayout horizon = new HorizontalLayout();


        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    Buerger buerger = ((BeanItem<Buerger>) itemClickEvent.getItem()).getBean();
                    controller
                            .getEventbus()
                            .notify(controller
                                    .getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(buerger));
                    controller.getNavigator().navigateTo(BuergerDetailView.NAME);
                    return;
                }
                boolean isClicked = grid.isSelected(itemClickEvent.getItemId());
                if (!itemClickEvent.isCtrlKey()) {
                    grid.getSelectedRows().stream().forEach(row -> grid.deselect(row));
                }
                if (!isClicked)
                    grid.select(itemClickEvent.getItemId());
                else
                    grid.deselect(itemClickEvent.getItemId());
            }
        });

        grid.addSelectionListener(selectionEvent -> setButtonVisability());

        filterLayout.addComponents(filter, search, reset);
        buttonlayout.addComponents(edit, copy, delete);
        buttonlayout.setSizeFull();
        filterLayout.setSizeFull();
        horizon.addComponents(create, filterLayout, buttonlayout);

        horizon.setSpacing(true);

        edit.setVisible(Boolean.FALSE);
        copy.setVisible(Boolean.FALSE);
        delete.setVisible(Boolean.FALSE);

        grid.setVisible(Boolean.TRUE);

        layout.addComponents(horizon);
        layout.addComponent(grid);
        layout.setSpacing(true);
        refresh();

        // configure
        this.grid.setWidth("100%");
        this.grid.setHeightByRows(10);
        this.grid.getColumns().stream().forEach(c -> {
            c.setHidable(true);
        });

        // set headers
        this.grid.getColumn(Buerger.Field.vorname.name()).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), I18nPaths.Type.column_header)));
        this.grid.getColumn(Buerger.Field.geburtsdatum.name()).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), I18nPaths.Type.column_header)));
        this.grid.getColumn(Buerger.Field.nachname.name()).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), I18nPaths.Type.column_header)));
        this.grid.getColumn(Buerger.Field.augenfarbe.name()).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.Field.augenfarbe.name(), I18nPaths.Type.column_header)));

        setCompositionRoot(layout);
    }

    private void createCopy() {
        copy = new ActionButton(controller, SimpleAction.copy, null);
        copy.addClickListener(clickEvent -> {
            LOG.debug("copying selected items");
            if (grid.getSelectedRows() != null) {
                for (Object next : grid.getSelectedRows()) {
                    BeanItem<Buerger> item = (BeanItem<Buerger>) grid.getContainerDataSource().getItem(next);
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.CREATE), reactor.bus.Event.wrap(item.getBean()));
                    //AppEvent event = controller.buildAppEvent(EventType.COPY_BUERGER).setItem(item);
                    //controller.postEvent(event);
                    //TODO copy fehlt noch
                }
                refresh();
            }
        });
    }

    private void createDelete() {
        delete = new ActionButton(controller, SimpleAction.delete, null);
        delete.addClickListener(clickEvent -> {
            LOG.debug("deleting selected items");
            if (grid.getSelectedRows() != null) {
                for (Object next : grid.getSelectedRows()) {
                    BeanItem<Buerger> item = (BeanItem<Buerger>) grid.getContainerDataSource().getItem(next);
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.DELETE), reactor.bus.Event.wrap(item.getBean()));
                    grid.deselect(next);
                }
            }
        });
    }

    private void createEdit() {
        edit = new ActionButton(controller, SimpleAction.update, BuergerUpdateView.NAME);
        edit.addClickListener(clickEvent -> {
            if (grid.getSelectedRows().size() != 1)
                return;
            LOG.debug("update selected");
            Buerger buerger = ((BeanItem<Buerger>) grid.getContainerDataSource().getItem(grid.getSelectedRows().toArray()[0])).getBean();
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(buerger));
            controller.getNavigator().navigateTo(BuergerUpdateView.NAME);
        });
    }

    private void createCreate() {
        create = new ActionButton(controller, SimpleAction.create, BuergerCreateView.NAME);
        create.addClickListener(clickEvent -> {
            controller.getNavigator().navigateTo(BuergerCreateView.NAME);
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
            refresh();
        });
        search.setId(String.format("%s_SEARCH_BUTTON", BuergerViewController.I18N_BASE_PATH));
    }

    private void createFilter() {
        filter.setId(String.format("%s_QUERY_FIELD", BuergerViewController.I18N_BASE_PATH));
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
        reset.setId(String.format("%s_RESET_BUTTON", BuergerViewController.I18N_BASE_PATH));
    }

    private void setButtonVisability() {
        int size = grid.getSelectedRows().size();
        edit.setVisible(size == 1);
        copy.setVisible(size > 0);
        delete.setVisible(size > 0);
    }

    //TODO wirklich nötig?
    void refresh() {
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_LIST));
    }

    public GenericGrid getGrid() {
        return grid;
    }
}
