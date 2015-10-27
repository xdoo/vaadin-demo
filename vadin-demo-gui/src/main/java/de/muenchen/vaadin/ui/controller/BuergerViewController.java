package de.muenchen.vaadin.ui.controller;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolverImpl;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import reactor.bus.Event;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Der Controller ist die zentrale Klasse um die Logik im Kontext BuergerDTO abzubilden.
 *
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class BuergerViewController implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(BuergerViewController.class);
    /**
     * Die Service Klasse
     */
    private final BuergerService buergerService;
    /**
     * Model der Daten für den Eventbus
     */
    private final BuergerDatastore model = new BuergerDatastore();

    @Autowired
    private I18nResolverImpl resolver;
    @Autowired
    public BuergerViewController(BuergerService buergerService) {
        this.buergerService = buergerService;
    }

    @PostConstruct
    public void init() {
        initEventhandlers();
    }

    private EventBus getEventbus() {
        return BaseUI.getCurrentEventBus();
    }

    public I18nResolverImpl getResolver() {
        return resolver;
    }

    public BuergerDatastore getModel() {
        return model;
    }

    ////////////////////////
    // Service Operations //
    ////////////////////////

    private boolean releaseKind(Buerger kind) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.kinder.name());
        List<Link> kinder = buergerService.findAll(link)
                .stream()
                .map(Buerger::getId)
                .filter(id -> !id.equals(kind.getId()))
                .collect(Collectors.toList());

        return buergerService.setRelations(link, kinder);
    }

    private boolean releasePartner(Buerger event) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.partner.name());
        return buergerService.delete(link);

    }

    private boolean addBuergerKind(Buerger kindEntity) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.kinder.name());
        List<Link> kinder = Stream.concat(
                buergerService.findAll(link)
                        .stream()
                        .map(Buerger::getId),
                Stream.of(kindEntity.getId()))

                .collect(Collectors.toList());

        return buergerService.setRelations(link, kinder);
    }

    private boolean setBuergerPartner(Buerger partnerEntity) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.partner.name());
        return buergerService.setRelation(link, partnerEntity.getId());
    }

    private List<Buerger> queryBuerger() {
        return buergerService.findAll().stream().collect(Collectors.toList());
    }

    private List<Buerger> queryBuerger(String query) {
        return buergerService.queryBuerger(query);
    }

    private List<Buerger> queryKinder(Buerger entity) {
        return buergerService.findAll(entity.getLink(Buerger.Rel.kinder.name())).stream().collect(Collectors.toList());
    }

    private Buerger queryPartner(Buerger entity) {
        return buergerService.findOne(entity.getLink(Buerger.Rel.partner.name())).orElse(null);
    }


    /////////////////////
    // Event Steuerung //
    /////////////////////

    /**
     * Register all event handlers on the RequestEntityKey.
     */
    public void initEventhandlers() {
        getEventbus().on(getRequestKey(RequestEvent.CREATE).toSelector(), this::create);
        getEventbus().on(getRequestKey(RequestEvent.DELETE).toSelector(), this::delete);
        getEventbus().on(getRequestKey(RequestEvent.UPDATE).toSelector(), this::update);
        getEventbus().on(getRequestKey(RequestEvent.ADD_ASSOCIATION).toSelector(), this::addAssociation);
        getEventbus().on(getRequestKey(RequestEvent.REMOVE_ASSOCIATION).toSelector(), this::removeAssociation);
        getEventbus().on(getRequestKey(RequestEvent.READ_LIST).toSelector(), this::readList);
        getEventbus().on(getRequestKey(RequestEvent.READ_SELECTED).toSelector(), this::readSelected);
    }

    /**
     * Remove the specified Association from the specified Relation and update the DataStore. Update the Model and send
     * it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Association} as {@link Event#getData()}.
     */
    private void removeAssociation(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");
        if (data.getClass() != Association.class)
            throw new IllegalArgumentException("The event must be of " + Association.class);

        final Association association = (Association) event.getData();

        final Buerger.Rel rel = Buerger.Rel.valueOf(association.getRel());
        if (Buerger.Rel.kinder == rel) {
            Buerger buerger = (Buerger) association.getAssociation();
            if (releaseKind(buerger))
                getModel().getSelectedBuergerKinder().removeItem(buerger);
        }
        if (Buerger.Rel.partner == rel) {
            Buerger buerger = (Buerger) association.getAssociation();
            if (releasePartner(buerger))
                getModel().setSelectedBuergerPartner(Optional.empty());
        }

        notifyComponents();
    }

    /**
     * Add the specified Association to the specified Relation and update the DataStore. <p> If the {@link
     * Association#getAssociation()} has no {@link ResourceSupport#getId()} the Resouce will be created on the DataStore
     * first. </p> Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Association} as {@link Event#getData()}.
     */
    private void addAssociation(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");
        if (data.getClass() != Association.class)
            throw new IllegalArgumentException("The event must be of " + Association.class);

        final Association association = (Association) event.getData();

        final Buerger.Rel rel = Buerger.Rel.valueOf(association.getRel());
        if (Buerger.Rel.kinder == rel) {
            Buerger buerger = (Buerger) association.getAssociation();
            // If Buerger has no ID he has to be created in the backend
            if (buerger.getId() == null) {
                buerger = buergerService.create(buerger);
            }
            if (addBuergerKind(buerger))
                getModel().getSelectedBuergerKinder().addBean(buerger);
        }
        if (Buerger.Rel.partner == rel) {
            Buerger buerger = (Buerger) association.getAssociation();
            if (buerger.getId() == null) {
                // If Buerger has no ID he has to be created in the backend
                buerger = buergerService.create(buerger);
            }
            if (setBuergerPartner(buerger))
                getModel().setSelectedBuergerPartner(Optional.of(buerger));
        }

        refreshModelAssociations();
        notifyComponents();
    }

    /**
     * Create a new Buerger on the DataStore. Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Buerger} as {@link Event#getData()}.
     */
    private void create(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");
        if (data.getClass() != Buerger.class)
            throw new IllegalArgumentException("The event must be of " + Buerger.class);

        final Buerger buerger = (Buerger) event.getData();
        final Buerger fromREST = buergerService.create(buerger);

        getModel().getBuergers().addBean(fromREST);
        notifyComponents();
    }


    /**
     * Delete the Buerger on the DataStore. Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Buerger} as {@link Event#getData()}.
     */
    private void delete(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");
        if (data.getClass() != Buerger.class)
            throw new IllegalArgumentException("The event must be of " + Buerger.class);

        final Buerger buerger = (Buerger) event.getData();
        if (buerger.getId() == null)
            throw new IllegalArgumentException("The Buerger must have an ID.");

        if (buergerService.delete(buerger.getId())) {

            getModel().getSelectedBuerger().ifPresent(selectedBuerger -> {
                if (selectedBuerger.equals(buerger)) {
                    getModel().setSelectedBuerger(null);
                    getModel().getSelectedBuergerKinder().removeAllItems();
                    getModel().setSelectedBuergerPartner(Optional.empty());
                }
            });

            getModel().getBuergers().removeItem(buerger);
            notifyComponents();
        } else {
            //Fehler...
        }
    }

    /**
     * Update the Buerger on the DataStore. Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Buerger} as {@link Event#getData()}.
     */
    private void update(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");
        if (data.getClass() != Buerger.class)
            throw new IllegalArgumentException("The event must be of " + Buerger.class);

        final Buerger buerger = (Buerger) event.getData();
        if (buerger.getId() == null)
            throw new IllegalArgumentException("The Buerger must have an ID.");
        final Buerger fromREST = buergerService.update(buerger);

        refreshModelSelected();
        getModel().getBuergers().addBean(fromREST);
        notifyComponents();
    }

    /**
     * Refresh the {@link BuergerDatastore#buergers} list from the DataStore. <p/> <p> This method also filters by the
     * query (ifPresent). </p>
     */
    private void refreshModelList() {
        final Optional<String> query = getModel().getQuery();
        if (query.isPresent()) {
            getModel().getBuergers().removeAllItems();
            getModel().getBuergers().addAll(queryBuerger(query.get()));
        } else {
            getModel().getBuergers().removeAllItems();
            getModel().getBuergers().addAll(queryBuerger());
        }
    }

    /**
     * Refresh *all* the associations of the selected Buerger in the model.
     */
    private void refreshModelAssociations() {
        getModel().getSelectedBuerger().ifPresent(buerger -> {
            final List<Buerger> kinder = queryKinder(buerger);
            final Buerger partner = queryPartner(buerger);
            getModel().getSelectedBuergerKinder().removeAllItems();
            getModel().getSelectedBuergerKinder().addAll(kinder);
            getModel().setSelectedBuergerPartner(Optional.ofNullable(partner));
        });
    }

    /**
     * Refresh the current selected Buerger, but *not* its associations.
     */
    private void refreshModelSelected() {
        getModel().getSelectedBuerger().ifPresent(buerger -> getModel().setSelectedBuerger(buergerService.findOne(buerger.getId()).orElse(null)));
    }

    /**
     * Set the query based on the String sent in the Event. Update the Model and send it on the ResponseEntityKey if
     * necessary.
     *
     * @param event The event with a {@link String} query as {@link Event#getData()}.
     */
    private void readList(Event<?> event) {
        final Object data = event.getData();

        if (data instanceof String) {
            final String filter = (String) event.getData();
            getModel().setQuery(filter);
        } else {
            getModel().setQuery(null);
        }

        refreshModelList();
        notifyComponents();
    }

    /**
     * Read the Buerger in the Event from the DataStore and set it as the current selected Buerger. If called with null,
     * the current selected Buerger will only be refreshed from the DataStore. Update the Model and send it on the
     * ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Buerger} or *null* as {@link Event#getData()}.
     */
    private void readSelected(Event<?> event) {
        final Object data = event.getData();

        if (data instanceof Buerger) {
            final Buerger buerger = (Buerger) event.getData();
            getModel().setSelectedBuerger(buerger);
            refreshModelSelected();
            refreshModelAssociations();
        } else if (data == null) {
            refreshModelSelected();
            refreshModelAssociations();
        } else {
            throw new IllegalArgumentException("The event cannot be of Class " + event.getData().getClass());
        }

        notifyComponents();
    }

    /**
     * Notify all the Components.
     */
    public void notifyComponents() {
        getEventbus().notify(getResponseKey(), Event.wrap(getModel()));
    }

    /**
     * Get the RequestEntityKey for this Entity.
     *
     * @param event The disered event the Key will have.
     * @return The RequestEntityKey with the chosen RequestEvent.
     */
    public RequestEntityKey getRequestKey(RequestEvent event) {
        return new RequestEntityKey(event, Buerger.class);
    }

    /**
     * Get the ResponseEntityKey for this Entity.
     *
     * @return The ResponseEntityKey.
     */
    public ResponseEntityKey getResponseKey() {
        return new ResponseEntityKey(Buerger.class);
    }

}
