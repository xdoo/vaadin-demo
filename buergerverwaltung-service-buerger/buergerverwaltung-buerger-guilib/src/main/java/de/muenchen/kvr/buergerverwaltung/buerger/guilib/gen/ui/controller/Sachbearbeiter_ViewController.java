package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.services.Sachbearbeiter_Service;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.services.model.Sachbearbeiter_Datastore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Link;
import reactor.bus.Event;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import de.muenchen.vaadin.guilib.BaseUI;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
@SpringComponent
@UIScope
public class Sachbearbeiter_ViewController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Sachbearbeiter_ViewController.class);
	
	/**
	 * Die Sachbearbeiter_Service Klasse
	 */
	@Autowired
	Sachbearbeiter_Service sachbearbeiterService;
	
	private final Sachbearbeiter_Datastore model = new Sachbearbeiter_Datastore();
	

	@PostConstruct
	private void init() {
		initEventhandlers();
	}
	
	private EventBus getEventbus() {
        return BaseUI.getCurrentEventBus();
    }
	
	public Sachbearbeiter_Datastore getModel() {
		return model;
	}

	////////////////////////
	// Service Operations //
	////////////////////////

	/**
	 * Speichert ein {@link Sachbearbeiter_} Objekt in der Datenbank.
	 *
	 * @param sachbearbeiter Sachbearbeiter_ der gespeichert werden soll
	 */
	public Sachbearbeiter_ save(Sachbearbeiter_ sachbearbeiter) {
		return sachbearbeiterService.create(sachbearbeiter);
	}
	/**
	 * Speichert die Änderungen an einem {@link Sachbearbeiter_} Objekt in der Datenbank.
	 *
	 * @param entity Sachbearbeiter_
	 * @return Sachbearbeiter_
	 */
	public Sachbearbeiter_ updateSachbearbeiter(Sachbearbeiter_ entity) {
		return sachbearbeiterService.update(entity);
	}

	/**
	 * Löscht ein {@link Sachbearbeiter_} Objekt.
	 *
	 * @param entity Sachbearbeiter_
	 */
	public void deleteSachbearbeiter(Sachbearbeiter_ entity) {
		sachbearbeiterService.delete(entity.getId());
	}

	public List<Sachbearbeiter_> querySachbearbeiter() {
		return sachbearbeiterService.findAll().stream().collect(Collectors.toList());
	}

	public List<Sachbearbeiter_> querySachbearbeiter(String query) {
		return sachbearbeiterService.querySachbearbeiter(query);
	}
	
	/////////////////////
	// Event Steuerung //
	/////////////////////

	/**
	 * Register all event handlers on the RequestEntityKey.
	 */
	private void initEventhandlers() {
		getEventbus().on(getRequestKey(RequestEvent.CREATE).toSelector(), this::create);
		getEventbus().on(getRequestKey(RequestEvent.DELETE).toSelector(), this::delete);	 
		getEventbus().on(getRequestKey(RequestEvent.UPDATE).toSelector(), this::update);
		getEventbus().on(getRequestKey(RequestEvent.ADD_ASSOCIATION).toSelector(), this::addAssociation);	 
		getEventbus().on(getRequestKey(RequestEvent.REMOVE_ASSOCIATION).toSelector(), this::removeAssociation);
		getEventbus().on(getRequestKey(RequestEvent.ADD_ASSOCIATIONS).toSelector(), this::addAssociations);
		getEventbus().on(getRequestKey(RequestEvent.REMOVE_ASSOCIATIONS).toSelector(), this::removeAssociations);
		getEventbus().on(getRequestKey(RequestEvent.READ_LIST).toSelector(), this::readList);
		getEventbus().on(getRequestKey(RequestEvent.READ_SELECTED).toSelector(), this::readSelected);
	}

	/**
	 * Remove the specified Association from the specified Relation and update the DataStore.
	 * Update the Model and send it on the ResponseEntityKey if necessary.
	 *
	 * @param event The event with an {@link Association} as {@link Event#getData()}.
	 */
	void removeAssociation(Event<?> event) {
		final Object data = event.getData();
		if (data == null)
			throw new NullPointerException("Event data must not be null!");
		if (data.getClass() != Association.class)
			throw new IllegalArgumentException("The event must be of " + Association.class);

		final Association<?> association = (Association<?>) event.getData();
		final Sachbearbeiter_.Rel rel = Sachbearbeiter_.Rel.valueOf(association.getRel());
		
		notifyComponents();
	}

	/**
	 * Add the specified Association to the specified Relation and update the DataStore.
	 * <p>	 
	 * If the {@link Association#getAssociation()} has no {@link ResourceSupport#getId()} the Resouce will be created
	 * on the DataStore first.
	 * </p>
	 * Update the Model and send it on the ResponseEntityKey if necessary.
	 *
	 * @param event The event with an {@link Association} as {@link Event#getData()}.
	 */
	private void addAssociation(Event<?> event) {
		final Object data = event.getData();
		if (data == null) 
			throw new NullPointerException("Event data must not be null!");
		if (data.getClass() != Association.class)
			throw new IllegalArgumentException("The event must be of " + Association.class);

		final Association<?> association = (Association<?>) event.getData();

		final Sachbearbeiter_.Rel rel = Sachbearbeiter_.Rel.valueOf(association.getRel());
		refreshModelAssociations();
		notifyComponents();
	}
	
	/**
	 * Remove the specified Associations from the specified Relation and update the DataStore.
	 * Update the Model and send it on the ResponseEntityKey if necessary.
	 *
	 * @param event The event with an {@link Association} as {@link Event#getData()}.
	 */
	@SuppressWarnings("unchecked")
	void removeAssociations(Event<?> event) {
		final Object data = event.getData();
		if (data == null)
			throw new NullPointerException("Event data must not be null!");
		if (!(data instanceof List))
			throw new IllegalArgumentException("The event must be of " + List.class);
		
		final List<?> dataList = (List<?>) event.getData();
		if (dataList.isEmpty())
			throw new IllegalArgumentException("No Data provided");
		if (!dataList.stream().map(Object::getClass).allMatch(Association.class::equals))
			throw new IllegalArgumentException("The event must be a list of " + Association.class);
		
		final List<Association<?>> associations = (List<Association<?>>) dataList;
		if(!associations.stream().map(Association::getRel).allMatch(associations.get(0).getRel()::equals))
			throw new IllegalArgumentException("Associations must be of same Relation");
		
		final Sachbearbeiter_.Rel rel = Sachbearbeiter_.Rel.valueOf(associations.get(0).getRel());
		
		notifyComponents();
	}
	
	/**
	 * Add the specified Associations to the specified Relation and update the DataStore.
	 * <p>
	 * If the {@link Association#getAssociation()} has no {@link ResourceSupport#getId()} the Resouce will be created
	 * on the DataStore first.
	 * </p>
	 * Update the Model and send it on the ResponseEntityKey if necessary.
	 *
	 * @param event The event with an {@link Association} as {@link Event#getData()}.
	 */
	@SuppressWarnings("unchecked")
	private void addAssociations(Event<?> event) {
		final Object data = event.getData();
		if (data == null)
			throw new NullPointerException("Event data must not be null!");
		if (! (data instanceof List))
			throw new IllegalArgumentException("The event must be of " + List.class);
		
		final List<?> dataList = (List<?>) event.getData();
		if (dataList.isEmpty())
			throw new IllegalArgumentException("No Data provided");
		if (!dataList.stream().map(Object::getClass).allMatch(Association.class::equals))
			throw new IllegalArgumentException("The event must be a list of " + Association.class);
			
		final List<Association<?>> associations = (List<Association<?>>) dataList;
		if(!associations.stream().map(Association::getRel).allMatch(associations.get(0).getRel()::equals))
			throw new IllegalArgumentException("Associations must be of same Relation");
		
		final Sachbearbeiter_.Rel rel = Sachbearbeiter_.Rel.valueOf(associations.get(0).getRel());
		refreshModelAssociations();
		notifyComponents();
	}

	/**	
	 * Create a new Buerger on the DataStore.
	 * Update the Model and send it on the ResponseEntityKey if necessary.
	 *
	 * @param event The event with an {@link Sachbearbeiter_} as {@link Event#getData()}.
	 */
	private void create(Event<?> event) {
		final Object data = event.getData();
		if (data == null) 
			throw new NullPointerException("Event data must not be null!");
		if (!(data instanceof Sachbearbeiter_))
			throw new IllegalArgumentException("The event must be of " + Sachbearbeiter_.class);
		final Sachbearbeiter_ sachbearbeiter = (Sachbearbeiter_) event.getData();
		final Sachbearbeiter_ fromREST = sachbearbeiterService.create(sachbearbeiter);
		getModel().getSachbearbeiters().addBean(fromREST);
		notifyComponents();
	}


	/**
	 * Delete the Sachbearbeiter_ on the DataStore.
	 * Update the Model and send it on the ResponseEntityKey if necessary.
	 *
	 * @param event The event with an {@link Sachbearbeiter_} as {@link Event#getData()}.
	 */
	private void delete(Event<?> event) {
		final Object data = event.getData();
		if (data == null) 
			throw new NullPointerException("Event data must not be null!");
		if (!(data instanceof Sachbearbeiter_))
			throw new IllegalArgumentException("The event must be of " + Sachbearbeiter_.class);
		final Sachbearbeiter_ sachbearbeiter = (Sachbearbeiter_) event.getData();
		if (sachbearbeiter.getId() == null)
			throw new IllegalArgumentException("The Sachbearbeiter_ must have an ID.");
		sachbearbeiterService.delete(sachbearbeiter.getId());
		getModel().getSelectedSachbearbeiter().ifPresent(selectedSachbearbeiter -> {
			if (selectedSachbearbeiter.equals(sachbearbeiter)) {
				getModel().setSelectedSachbearbeiter(null);
				// reset all selected relations
			}
		});
		getModel().getSachbearbeiters().removeItem(sachbearbeiter);
		notifyComponents();
	}

	/**
	 * Update the Sachbearbeiter_ on the DataStore.
	 * Update the Model and send it on the ResponseEntityKey if necessary.
	 *
	 * @param event The event with an {@link Sachbearbeiter_} as {@link Event#getData()}.
	 */
	private void update(Event<?> event) {
		final Object data = event.getData();	 
		if (data == null) 
			throw new NullPointerException("Event data must not be null!");
		if (!(data instanceof Sachbearbeiter_))
			throw new IllegalArgumentException("The event must be of " + Sachbearbeiter_.class);
		final Sachbearbeiter_ sachbearbeiter = (Sachbearbeiter_) event.getData();
		if (sachbearbeiter.getId() == null)
			throw new IllegalArgumentException("The Sachbearbeiter_ must have an ID.");
		final Sachbearbeiter_ fromREST = sachbearbeiterService.update(sachbearbeiter);
		refreshModelSelected();
		getModel().getSachbearbeiters().addBean(fromREST);
		notifyComponents();
	}

	/**
	 * Refresh the {@link Sachbearbeiter_Datastore#sachbearbeiters} list from the DataStore.
	 * <p/>	
	 * <p>
	 * This method also filters by the query (ifPresent).
	 * </p>
	 */
	private void refreshModelList() {
		final Optional<String> query = getModel().getQuery();
		if (query.isPresent()) {
			getModel().getSachbearbeiters().removeAllItems();
			getModel().getSachbearbeiters().addAll(querySachbearbeiter(query.get()));
		} else {
			getModel().getSachbearbeiters().removeAllItems();
			getModel().getSachbearbeiters().addAll(querySachbearbeiter());
		}
	}

	/**
	 * Refresh *all* the associations of the selected Sachbearbeiter_ in the model.
	 */
	void refreshModelAssociations() {
		getModel().getSelectedSachbearbeiter().ifPresent(sachbearbeiter -> {
		});	
	}

	/**
	 * Refresh the current selected Sachbearbeiter_, but *not* its associations.
	 */
	private void refreshModelSelected() {
		getModel().getSelectedSachbearbeiter().ifPresent(sachbearbeiter -> getModel().setSelectedSachbearbeiter(sachbearbeiterService.findOne(sachbearbeiter.getId()).orElse(null)));
	}

	/**	
	 * Set the query based on the String sent in the Event.
	 * Update the Model and send it on the ResponseEntityKey if necessary.
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
	 * Read the Sachbearbeiter_ in the Event from the DataStore and set it as the current selected Sachbearbeiter_.
	 * If called with null, the current selected Sachbearbeiter_ will only be refreshed from the DataStore.
	 * Update the Model and send it on the ResponseEntityKey if necessary.
	 *
	 * @param event The event with an {@link Sachbearbeiter_} or *null* as {@link Event#getData()}.
	 */
	private void readSelected(Event<?> event) {
		final Object data = event.getData();

		if (data instanceof Sachbearbeiter_) {
			final Sachbearbeiter_ sachbearbeiter = (Sachbearbeiter_) event.getData();
			getModel().setSelectedSachbearbeiter(sachbearbeiter);
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
		return new RequestEntityKey(event, Sachbearbeiter_.class);
	}

	/**
	 * Get the ResponseEntityKey for this Entity.
	 *
	 * @return The ResponseEntityKey.
	 */
	public ResponseEntityKey getResponseKey() {
		return new ResponseEntityKey(Sachbearbeiter_.class);
	}
}
