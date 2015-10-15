package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.buttons.SimpleAction;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import java.util.List;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;

/**
 * @author claus
 */
@SuppressWarnings("unchecked")
public class BuergerChildTab extends CustomComponent {

    private BuergerViewController controller;
    private GenericGrid grid;

    public BuergerChildTab(BuergerViewController controller, BuergerI18nResolver resolver, String navigateToForDetail, String navigateToForCreate, String navigateBack) {

        this.controller = controller;

        grid = controller.getViewFactory().generateChildTable(BuergerDetailView.NAME)
                .activateCreate(navigateToForCreate)
                .activateRead(BuergerDetailView.NAME)
                .addButton(
                        controller.getResolver().resolveRelative(
                                getFormPath(SimpleAction.add,
                                        I18nPaths.Component.button,
                                        I18nPaths.Type.label)),
                        () -> {
                            HorizontalLayout layout = new HorizontalLayout(controller.getViewFactory().generateChildSearchTable());
                            layout.setMargin(true);
                            getUI().addWindow(new TableSelectWindow(controller, controller.getResolver(), layout));
                        })
                .addMultiSelectButton(
                        controller.getResolver().resolveRelative(
                                getFormPath(SimpleAction.delete,
                                        I18nPaths.Component.button,
                                        I18nPaths.Type.label)),
                        buergers -> {
                            ((List) buergers).stream().forEach(buerger -> {
                                final Association<Buerger> association = new Association<>((Buerger) buerger, Buerger.Rel.kinder.name());
                                controller.getEventbus().notify(controller.getRequestKey(RequestEvent.REMOVE_ASSOCIATION), association.asEvent());
                            });
                        });

        HorizontalLayout layout = new HorizontalLayout(grid);
        layout.setSizeFull();
        layout.setMargin(true);
        setCompositionRoot(layout);


        setId(String.format("%s_%s_%s_CHILD_TAB", navigateToForDetail, navigateBack, controller.getResolver().getBasePath()));
    }


}
