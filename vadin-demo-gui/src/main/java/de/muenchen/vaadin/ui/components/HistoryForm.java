package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.I18nPaths;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Optional;

import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getFormPath;

/**
 * Created by arne.schoentag on 02.09.15.
 */
public class HistoryForm  extends CustomComponent implements Consumer<Event<ComponentEvent<Buerger>>>{

    /**
     * Logger
     */
    protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(HistoryForm.class);

    final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
    final BuergerViewController controller;

   // private final String navigateToUpdate;
    private String back;
    private final String from;

    /**
     * Formular zum Lesen eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'zurück' und 'bearbeiten' Schaltflächen
     * erstellt werden.
     *
     * @param controller
     * @param navigateFrom
     */
    public HistoryForm(final BuergerViewController controller, final String navigateFrom) {

        this.controller = controller;
        //this.navigateToUpdate = navigateToUpdate;

        this.from = navigateFrom;
        this.back = from;

        // create form
        this.createForm();

    }

    private void createForm() {
        LOG.debug("createFrom started");
        FormLayout layout = new FormLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        layout.setMargin(true);

        // headline
        Label headline = new Label(controller.resolveRelative(getFormPath(SimpleAction.history, I18nPaths.Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);


        ActionButton backButton = new ActionButton(controller, SimpleAction.back,this.back);
        backButton.addClickListener((clickEvent -> {
            controller.getNavigator().navigateTo(getNavigateBack());
        }));
        layout.addComponent(backButton);
        setCompositionRoot(layout);
    }

    @Override
    public void accept(reactor.bus.Event<ComponentEvent<Buerger>> eventWrapper) {
        ComponentEvent event = eventWrapper.getData();

        if (event.getEventType().equals(EventType.HISTORY)) {
            LOG.debug("seleted buerger to modify.");
            Optional<BeanItem<Buerger>> opt = event.getItem();
            if (opt.isPresent()) {
                this.binder.setItemDataSource(opt.get());
            } else {
                LOG.warn("No item present.");
            }
        }
    }

    public String getNavigateBack() {
        return back;
    }

    public void setNavigateBack(String back) {
        this.back = back;
    }
}
