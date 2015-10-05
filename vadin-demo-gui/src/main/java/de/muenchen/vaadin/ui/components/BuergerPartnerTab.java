package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericConfirmationWindow;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

/**
 * @author Maximilian Schug
 */
public class BuergerPartnerTab extends CustomComponent {

    private final BuergerViewController controller;
    private final BuergerI18nResolver resolver;
    private GenericGrid grid;
    private ActionButton delete;

    public BuergerPartnerTab(BuergerViewController controller, BuergerI18nResolver resolver, String navigateToForDetail, String navigateToForCreate, String navigateToForAdd, String from) {

        this.controller = controller;
        this.resolver = resolver;

        ActionButton create = new ActionButton(resolver, SimpleAction.create, navigateToForCreate);
        create.addClickListener(clickEvent -> {
            if (grid.getContainerDataSource().size() == 0) {
                controller.getNavigator().navigateTo(navigateToForCreate);
            } else {
                GenericConfirmationWindow window = new GenericConfirmationWindow(resolver, SimpleAction.override, e -> {
                    controller.getNavigator().navigateTo(navigateToForCreate);
                });
                getUI().addWindow(window);
                window.center();
                window.focus();
            }
        });
        ActionButton add = new ActionButton(resolver, SimpleAction.add, navigateToForAdd);
        add.addClickListener(clickEvent -> {
            if (grid.getContainerDataSource().size() == 0) {
                getUI().addWindow(new TableSelectWindow(controller, resolver, controller.getViewFactory().generateBuergerPartnerSearchTable()));
                //controller.postEvent(controller.buildAppEvent(EventType.ADD_PARTNER));
            } else {
                GenericConfirmationWindow window =
                        new GenericConfirmationWindow(resolver, SimpleAction.override, e -> getUI().addWindow(new TableSelectWindow(controller, resolver, controller.getViewFactory().generateBuergerPartnerSearchTable())));
                getUI().addWindow(window);
                window.center();
                window.focus();
            }
        });


        delete = new ActionButton(resolver, SimpleAction.delete, null);
        delete.addClickListener(clickEvent -> {
            if (grid.getSelectedRows() != null) {
                for (Object next : grid.getSelectedRows()) {
                    BeanItem<Buerger> item = (BeanItem<Buerger>) grid.getContainerDataSource().getItem(next);
                    Association<Buerger> association = new Association<Buerger>(item.getBean(), Buerger.Rel.partner.name());
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.REMOVE_ASSOCIATION), association.asEvent());
                    grid.deselect(next);
                }
            }
        });
        delete.setVisible(false);

        grid = controller.getViewFactory().generatePartnerTable(navigateToForDetail);
        grid.setColumns(Buerger.Field.getProperties());
        grid.addSelectionListener(selectionEvent -> setButtonVisability());

        // set headers
        this.grid.getColumn(Buerger.Field.vorname.name()).setHeaderCaption(resolver.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), I18nPaths.Type.column_header)));
        this.grid.getColumn(Buerger.Field.geburtsdatum.name()).setHeaderCaption(resolver.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), I18nPaths.Type.column_header)));
        this.grid.getColumn(Buerger.Field.nachname.name()).setHeaderCaption(resolver.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), I18nPaths.Type.column_header)));
        this.grid.getColumn(Buerger.Field.augenfarbe.name()).setHeaderCaption(resolver.resolveRelative(getEntityFieldPath(Buerger.Field.augenfarbe.name(), I18nPaths.Type.column_header)));

        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(create, add, delete);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, grid);
        vlayout.setSpacing(true);
        vlayout.setMargin(true);

        setId(String.format("%s_%s_%s_PARENT_TAB", navigateToForDetail, from, BuergerI18nResolver.I18N_BASE_PATH));
        setCompositionRoot(vlayout);
    }

    private void setButtonVisability() {
        if (grid.getSelectedRows().size() == 0)
            delete.setVisible(Boolean.FALSE);
        else
            delete.setVisible(Boolean.TRUE);
    }

    public GenericGrid getGrid() {
        return grid;
    }

    public void setGrid(GenericGrid grid) {
        this.grid = grid;
    }
}
