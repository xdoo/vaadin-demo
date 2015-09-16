package de.muenchen.vaadin.ui.app.views;

import com.vaadin.data.util.BeanItem;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import de.muenchen.eventbus.events.AppEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.demo.i18nservice.buttons.TableAction;
import de.muenchen.vaadin.demo.i18nservice.buttons.TableActionButton;
import de.muenchen.vaadin.guilib.components.GenericTable;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.BuergerSearchTable;
import de.muenchen.vaadin.ui.components.BuergerTable;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerTableView.NAME)
@UIScope
public class BuergerTableView extends DefaultBuergerView {
    
    public static final String NAME = "buerger_table_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerCreateView.class);

    Grid grid;
    private TextField filter = new TextField();
    private GenericTable table;

    @Autowired
    public BuergerTableView(BuergerViewController controller, MainUI ui) {
        super(controller, ui);
        LOG.debug("creating 'buerger_table_view'");
        
    }

    @Override
    protected void site() {
        grid = new Grid();
        filter.setInputPrompt("Filter contacts...");
        filter.addTextChangeListener(e -> refresh(e.getText()));


        table = controller.getViewFactory().generateTable(null);

        grid.setContainerDataSource(table.container);
        grid.setColumnOrder("vorname", "nachname", "geburtsdatum");
        grid.removeColumn("id");
        grid.removeColumn("links");
        grid.removeColumn("oid");

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.isDoubleClick()) {
                controller.postEvent(controller.buildAppEvent(EventType.SELECT2READ).setItem((BeanItem<Buerger>) itemClickEvent.getItem()));
                controller.getNavigator().navigateTo(BuergerDetailView.NAME);

            }
            boolean isClicked = grid.isSelected(itemClickEvent.getItemId());
            if (!itemClickEvent.isCtrlKey()) {
                grid.getSelectedRows().stream().forEach(row -> grid.deselect(row));
            }
            if (!isClicked)
                grid.select(itemClickEvent.getItemId());
            else
                grid.deselect(itemClickEvent.getItemId());

        });
        refresh();

        HorizontalLayout horizon = new HorizontalLayout();

        ActionButton create = new ActionButton(controller, SimpleAction.create,BuergerCreateView.NAME);
        create.addClickListener(clickEvent -> {
            controller.postEvent(controller.buildAppEvent(EventType.CREATE));
            controller.getNavigator().navigateTo(BuergerCreateView.NAME);
        });
        create.setVisible(Boolean.TRUE);

        ActionButton delete = new ActionButton(controller, SimpleAction.delete, null);
        delete.addClickListener(clickEvent -> {
            LOG.warn("deleting selected items");
            for (Object next : grid.getSelectedRows()) {
                BeanItem<Buerger> item = table.container.getItem(next);
                AppEvent event = controller.buildAppEvent(EventType.DELETE).setItem(item);
                controller.postEvent(event);

                LOG.warn("item deleted");
            /*GenericConfirmationWindow win = new GenericConfirmationWindow(*//*
                    controller.buildAppEvent(EventType.DELETE).setItem(item).setItemId(id),
                    controller, SimpleAction.delete);
            controller.getNavigator().getUI().addWindow(win);
            win.center();
            win.focus();*/

            }
            refresh();
        });
        delete.setVisible(Boolean.TRUE);

        horizon.addComponent(create);
        horizon.addComponent(delete);
        grid.setVisible(Boolean.TRUE);

        addComponent(horizon);
        addComponent(grid);
        refresh();
    }

    void refresh() {
        refresh(filter.getValue());
    }

    private void refresh(String stringFilter) {
        controller.postEvent(controller.buildAppEvent(EventType.QUERY));
        grid.setContainerDataSource(table.container);

    }
}
