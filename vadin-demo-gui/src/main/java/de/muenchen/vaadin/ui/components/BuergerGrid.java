package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.events.AppEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.ui.app.views.BuergerCreateView;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.app.views.BuergerUpdateView;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
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

    public BuergerGrid(final BuergerViewController controller) {

        this.controller = controller;

        VerticalLayout layout = new VerticalLayout();
//        layout.setMargin(true);

        this.grid = controller.getViewFactory().generateGrid();
//                new GenericGrid(controller,LocalBuerger.class);
        filter.setId(String.format("%s_QUERY_FIELD", BuergerViewController.I18N_BASE_PATH));
        filter.focus();
        filter.setWidth("100%");

        // Reset Schaltfläche
        reset = new Button(FontAwesome.TIMES);
        reset.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        reset.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        reset.addClickListener(e -> {
            controller.postEvent(controller.buildAppEvent(EventType.QUERY));
            filter.setValue("");
        });

        reset.setId(String.format("%s_RESET_BUTTON", BuergerViewController.I18N_BASE_PATH));
        // Suche Schaltfläche
        search = new Button(FontAwesome.SEARCH);
        search.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        search.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        search.addClickListener(e -> {
            LOG.debug("search clicked: " + filter.getValue());
            if (filter.getValue() != null && filter.getValue().length() > 0)
                controller.postEvent(controller.buildAppEvent(EventType.QUERY).query(filter.getValue()));
            else
                reset.click();
            refresh();
        });
        search.setId(String.format("%s_SEARCH_BUTTON", BuergerViewController.I18N_BASE_PATH));
        grid.setColumnOrder("vorname", "nachname", "geburtsdatum");
        grid.removeColumn("id");
        grid.removeColumn("links");
        grid.setSizeFull();

        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        CssLayout filterLayout = new CssLayout();
        filterLayout.addStyleName("v-component-group");
        CssLayout buttonlayout = new CssLayout();
        buttonlayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        HorizontalLayout horizon = new HorizontalLayout();

        ActionButton create = new ActionButton(controller, SimpleAction.create, BuergerCreateView.NAME);
        create.addClickListener(clickEvent -> {
            controller.postEvent(controller.buildAppEvent(EventType.CREATE));
            controller.getNavigator().navigateTo(BuergerCreateView.NAME);
        });
        create.setVisible(Boolean.TRUE);

        edit = new ActionButton(controller, SimpleAction.update, BuergerUpdateView.NAME);
        edit.addClickListener(clickEvent -> {
            if (grid.getSelectedRows().size() != 1)
                return;
            LOG.debug("update selected");
            AppEvent<LocalBuerger> event = controller.buildAppEvent(EventType.SELECT2UPDATE).setItem((BeanItem<LocalBuerger>) grid.getContainerDataSource().getItem(grid.getSelectedRows().toArray()[0]));
            controller.postEvent(event);
            controller.getNavigator().navigateTo(BuergerUpdateView.NAME);
        });

        delete = new ActionButton(controller, SimpleAction.delete, null);
        delete.addClickListener(clickEvent -> {
            LOG.debug("deleting selected items");
            if (grid.getSelectedRows() != null) {
                for (Object next : grid.getSelectedRows()) {
                    BeanItem<LocalBuerger> item = (BeanItem<LocalBuerger>) grid.getContainerDataSource().getItem(next);
                    AppEvent event = controller.buildAppEvent(EventType.DELETE).setItem(item);
                    controller.postEvent(event);
                    grid.deselect(next);
                    LOG.debug("item deleted");
                }
                refresh();
            }
        });

        copy = new ActionButton(controller, SimpleAction.copy, null);
        copy.addClickListener(clickEvent -> {
            LOG.debug("copying selected items");
            if (grid.getSelectedRows() != null) {
                for (Object next : grid.getSelectedRows()) {
                    BeanItem<LocalBuerger> item = (BeanItem<LocalBuerger>) grid.getContainerDataSource().getItem(next);
                    AppEvent event = controller.buildAppEvent(EventType.COPY).setItem(item);
                    controller.postEvent(event);

                    LOG.debug("item copied");
                }
                refresh();
            }
        });

        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    controller.postEvent(controller.buildAppEvent(EventType.SELECT2READ).setItem((BeanItem<LocalBuerger>) itemClickEvent.getItem()));
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
        this.grid.getColumn(Buerger.VORNAME).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.VORNAME, Type.column_header)));
        this.grid.getColumn(Buerger.GEBURTSDATUM).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.column_header)));
        this.grid.getColumn(Buerger.NACHNAME).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.column_header)));

        setCompositionRoot(layout);

    }

    private void setButtonVisability() {
        int size = grid.getSelectedRows().size();
        if (size == 0) {
            edit.setVisible(Boolean.FALSE);
            copy.setVisible(Boolean.FALSE);
            delete.setVisible(Boolean.FALSE);
        } else if (size == 1) {
            edit.setVisible(Boolean.TRUE);
            copy.setVisible(Boolean.TRUE);
            delete.setVisible(Boolean.TRUE);
        } else if (size > 1) {
            edit.setVisible(Boolean.FALSE);
            copy.setVisible(Boolean.TRUE);
            delete.setVisible(Boolean.TRUE);
        }
    }

    void refresh() {
        refresh(filter.getValue());
    }

    private void refresh(String stringFilter) {
        controller.postEvent(controller.buildAppEvent(EventType.QUERY).query((stringFilter == null) ? null : stringFilter));
    }

    public GenericGrid getGrid() {
        return grid;
    }
}
