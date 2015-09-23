package de.muenchen.vaadin.ui.controller;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.muenchen.eventbus.events.AppEvent;
import de.muenchen.eventbus.events.ComponentEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.api.domain.BuergerDTO;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.ControllerContext;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericFailureNotification;
import de.muenchen.vaadin.guilib.components.GenericSuccessNotification;
import de.muenchen.vaadin.guilib.services.MessageService;
import de.muenchen.vaadin.guilib.util.VaadinUtil;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.controller.factorys.BuergerViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;
import static reactor.bus.selector.Selectors.object;

/**
 * Der Controller ist die zentrale Klasse um die Logik im Kontext BuergerDTO abzubilden.
 *
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class BuergerViewController implements Serializable, ControllerContext<Buerger> {


	// TODO entweder hier oder im I18nServiceConfigImpl angeben
	public static final String I18N_BASE_PATH = "buerger";
	private static final long serialVersionUID = 1L;
	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BuergerViewController.class);

	/**
	 * Die Service Klasse
	 */
	private final BuergerService service;

	/**
	 * Werkzeuge für Vaadin
	 */
	private final VaadinUtil util;

	/**
	 * Event Bus zur Kommunikation
	 */
	@Autowired
	private EventBus eventbus;

	/**
	 * {@link MessageService} zur Auflösung der Platzhalter
	 */
	@Autowired
	private MessageService msg;
	/**
	 * {@link UI} {@link Navigator}
	 */
	private Navigator navigator;
	/**
	 * BuergerViewFactory zum erstellen der Components
	 **/
	@Autowired
	private BuergerViewFactory buergerViewFactory;
	// item cache
	private BeanItem<Buerger> current;

	@Autowired
	public BuergerViewController(BuergerService service, VaadinUtil util) {
		this.service = service;
		this.util = util;
	}

	@PostConstruct
	public void init() {
		//Set Controller in Factory after Construct.
		//to prevent circular reference
		buergerViewFactory.setController(this);
		initEventhandlers();
	}


	/**
	 * Die Main wird über die {@link de.muenchen.vaadin.ui.app.views.DefaultBuergerView} registriert. Dies
	 * ist notwendig, da die Vaadin abhängigen Beans zum Zeitpunkt der Instanzierung
	 * noch nicht vorhanden sind. Aus der Main UI wird der {@link Navigator} geholt,
	 * um über ihn eine Navigation zwischen einzelnen Views zu ermöglichen.
	 *
	 * @param ui MainUI
	 */
	public void registerUI(MainUI ui) {
		this.navigator = ui.getNavigator();
	}

	public EventBus getEventbus() {
		return eventbus;
	}

	public VaadinUtil getUtil() {
		return util;
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public BeanItem<Buerger> getCurrent() {
		return current;
	}

	public BuergerViewFactory getViewFactory() {
		return buergerViewFactory;
	}


	/**
	 * Resolve the path (e.g. "asdf.label").
	 *
	 * @param path the path.
	 * @return the resolved String.
	 */
	@Override
	public String resolve(String path) {
		return msg.get(path);
	}

	/**
	 * Resolve the relative path (e.g. "asdf.label").
	 * <p/>
	 * The base path will be appended at start and then read from the properties.
	 *
	 * @param relativePath the path to add to the base path.
	 * @return the resolved String.
	 */
	@Override
	public String resolveRelative(String relativePath) {
		return msg.get(I18N_BASE_PATH + "." + relativePath);
	}

	@Override
	public void postEvent(Object event) {
		getEventbus().notify(event, Event.wrap(event));
	}

	public void registerToAppEvent(EventType type, Consumer<Event<AppEvent<Buerger>>> consumer) {
		getEventbus().on(object(buildAppEvent(type)), consumer);
	}

	public void registerToAllAppEvents(Consumer<Event<AppEvent<Buerger>>> consumer) {
		Stream.of(EventType.values()).forEach(type -> registerToAppEvent(type, consumer));
	}

	public void registerToComponentEvent(EventType type, Consumer<Event<ComponentEvent<Buerger>>> consumer) {
		getEventbus().on(object(buildComponentEvent(type)), consumer);
	}

	public void registerToAllComponentEvents(Consumer<Event<ComponentEvent<Buerger>>> consumer) {
		Stream.of(EventType.values()).forEach(type -> registerToComponentEvent(type, consumer));
	}

	@Override
	public AppEvent<Buerger> buildAppEvent(EventType eventType) {
		return new AppEvent<Buerger>(Buerger.class, eventType);
	}

	@Override
	public ComponentEvent<Buerger> buildComponentEvent(EventType eventType) {
		return new ComponentEvent<>(Buerger.class, eventType);
	}

	@Override
	public String getBasePath() {
		return I18N_BASE_PATH;
	}

	/**
	 * Resolve the relative path (e.g. ".asdf.label") to a icon.
	 * <p/>
	 * The base path will be appended at start and then read from the properties.
	 *
	 * @param relativePath the path to add to the base path.
	 * @return the resolved String.
	 */
	@Override
	public FontAwesome resolveIcon(String relativePath) {
		return msg.getFontAwesome(I18N_BASE_PATH + "." + relativePath + ".icon");
	}

	////////////////////////
	// Service Operations //
	////////////////////////

	/**
	 * Kopiert eine vorhandene Instanz eines {@link BuergerDTO} Objektes in eine
	 * neue Instanz. Die Kopie wird gleich in der DB gespeichert.
	 *
	 * @param id BuergerDTO der kopiert werden soll
	 */
	public Buerger copyBuerger(Link id) {
		return service.copy(id);
	}

	/**
	 * Speichert ein {@link BuergerDTO} Objekt in der Datenbank.
	 *
	 * @param buerger BuergerDTO der gespeichert werden soll
	 */
	public Buerger save(Buerger buerger) {
		return service.create(buerger);
	}

	/**
	 * Speichert ein Kind zu einem {@link BuergerDTO} Objekt in der Datenbank.
	 *
	 * @param kind Zu speicherndes Kind
	 * @return BuergerDTO
	 */
	public void saveBuergerKind(Buerger kind) {
		Buerger child = save(kind);
		addBuergerKind(child);
	}

	/**
	 * Speichert einen partner zu einem {@link BuergerDTO} Objekt in der Datenbank.
	 *
	 * @param entity Zu speichernder partner
	 * @return BuergerDTO
	 */
	public void saveBuergerPartner(Buerger entity) {
		Buerger newBuerger = save(entity);
		addBuergerPartner(newBuerger);
	}

	/**
	 * Zerstört die Verbindung zwischen einem Bürger und seinem Kind
	 *
	 * @param event
	 */
	private void releaseKind(Buerger event) {
		Link link = current.getBean().getLink(Buerger.Rel.kinder.name());
		List<Link> kinder = service.findAll(link)
				.stream()
				.map(Buerger::getId)
				.filter(id -> !id.equals(event.getId()))
				.collect(Collectors.toList());

		service.setRelations(link, kinder);
	}

	/**
	 * Vernichted die Verbindung zwischen einem Bürger und seinem Partner
	 *
	 * @param event
	 */
	private void releasePartner(Buerger event) {
		Link link = current.getBean().getLink(Buerger.Rel.partner.name());
		List<Link> partner = service.findAll(link)
				.stream()
				.map(Buerger::getId)
				.filter(id -> !id.equals(event.getId()))
				.collect(Collectors.toList());

		service.setRelations(link, partner);
	}


	/**
	 * Speichert eine Kindbeziehung zu einem {@link BuergerDTO} Objekt in der Datenbank.
	 *
	 * @param kindEntity Kind
	 * @return BuergerDTO
	 */
	public void addBuergerKind(Buerger kindEntity) {
		Link link = current.getBean().getLink(Buerger.Rel.kinder.name());
		List<Link> kinder = Stream.concat(
				service.findAll(link)
						.stream()
						.map(Buerger::getId),
				Stream.of(kindEntity.getId()))

				.collect(Collectors.toList());

		service.setRelations(link, kinder);
	}

	/**
	 * Speichert eine Partnerschaftesbeziehung zu einem {@link BuergerDTO} Objekt in der Datenbank.
	 *
	 * @param partnerEntity Partner
	 * @return BuergerDTO
	 */
	public void addBuergerPartner(Buerger partnerEntity) {
		Link link = current.getBean().getLink(Buerger.Rel.partner.name());
		List<Link> partner = Stream.concat(
				service.findAll(link)
						.stream()
						.map(Buerger::getId),
				Stream.of(partnerEntity.getId()))

				.collect(Collectors.toList());

		service.setRelations(link, partner);
	}

	/**
	 * Speichert die Änderungen an einem {@link BuergerDTO} Objekt in der Datenbank.
	 *
	 * @param entity BuergerDTO
	 * @return BuergerDTO
	 */
	public Buerger updateBuerger(Buerger entity) {
		return service.update(entity);
	}

	/**
	 * Löscht ein {@link BuergerDTO} Objekt.
	 *
	 * @param entity BuergerDTO
	 */
	public void deleteBuerger(Buerger entity) {
		service.delete(entity.getId());
	}

	public List<Buerger> queryBuerger() {
		return service.findAll().stream().collect(Collectors.toList());
	}

	public List<Buerger> queryBuerger(String query) {
		return queryBuerger(); //TODO
	}

	public List<Buerger> queryKinder(Buerger entity) {
		return service.findAll(entity.getLink(Buerger.Rel.kinder.name())).stream().collect(Collectors.toList());
	}

	public List<Buerger> queryPartner(Buerger entity) {
		return service.findAll(entity.getLink(Buerger.Rel.partner.name())).stream().collect(Collectors.toList());
	}


	/////////////////////
	// Event Steuerung //
	/////////////////////

	public void initEventhandlers() {

		registerToAppEvent(EventType.UPDATE, this::updateEventHandler);
		registerToAppEvent(EventType.SAVE, this::saveEventHandler);
		registerToAppEvent(EventType.DELETE, this::deleteEventHandler);
		registerToAppEvent(EventType.SELECT2UPDATE, this::select2UpdateEventHandler);
		registerToAppEvent(EventType.SELECT2READ, this::select2ReadEventHandler);
		registerToAppEvent(EventType.COPY, this::copyEventHandler);
		registerToAppEvent(EventType.QUERY, this::queryEventHandler);
		registerToAppEvent(EventType.QUERY_CHILD, this::queryChildEventHandler);
		registerToAppEvent(EventType.SAVE_CHILD, this::saveChildEventHandler);
		registerToAppEvent(EventType.SAVE_PARTNER, this::savePartnerEventHandler);
		registerToAppEvent(EventType.SAVE_AS_CHILD, this::saveAsChildEventHandler);
		registerToAppEvent(EventType.ADD_SEARCHED_CHILD, this::addSearchedChildEventHandler);
		registerToAppEvent(EventType.RELEASE_KIND, this::releaseKindHandler);
		registerToAppEvent(EventType.RELEASE_PARTNER, this::releasePartnerHandler);
		registerToAppEvent(EventType.SAVE_AS_PARTNER, this::saveAsPartnerEventHandler);
		registerToAppEvent(EventType.ADD_PARTNER, this::addPartnerEventHandler);
		registerToAppEvent(EventType.QUERY_PARTNER, this::queryPartnerEventHandler);

	}

	private void releasePartnerHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();
		this.releasePartner(event.getEntity());
		// UI Komponenten aktualisieren

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.label, "partner")),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.text, "partner")));
		succes.show(Page.getCurrent());

		postEvent(buildComponentEvent(EventType.DELETE).setItemID(event.getEntity()));
	}

	private void addPartnerEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateBuergerPartnerSearchTable()));
		postEvent(buildAppEvent(EventType.QUERY));
	}

	private void saveAsPartnerEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();
		this.addBuergerPartner(event.getEntity());
		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label, "partner")),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text, "partner")));
		succes.show(Page.getCurrent());
		postEvent(buildComponentEvent(EventType.UPDATE_PARTNER).addEntity(event.getEntity()));
	}

	private void releaseKindHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();
		// Service Operationen ausführen
		this.releaseKind(event.getEntity());
		// UI Komponenten aktualisieren

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.label, "child")),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.text, "child")));
		succes.show(Page.getCurrent());

		postEvent(buildComponentEvent(EventType.DELETE).setItemID(event.getEntity()));

	}

	private void addSearchedChildEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateChildSearchTable()));
		postEvent(buildAppEvent(EventType.QUERY));
	}

	private void saveAsChildEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		this.addBuergerKind(event.getEntity());
		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label, "child")),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text, "child")));
		succes.show(Page.getCurrent());
		postEvent(buildComponentEvent(EventType.UPDATE_CHILD).addEntity(event.getEntity()));
	}

	private void queryChildEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.QUERY_CHILD).addEntities(this.queryKinder(event.getEntity())));
	}

	private void queryEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		List<Buerger> currentEntities;
		if (event.getQuery().isPresent()) {
			currentEntities = this.queryBuerger(event.getQuery().get());
		} else {
			currentEntities = this.queryBuerger();
		}

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.QUERY).addEntities(currentEntities));
	}

	private void select2ReadEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		this.current = event.getItem();

		// UI Komponente aktualisieren
		postEvent(buildComponentEvent(EventType.SELECT2READ).addEntity(event.getItem().getBean()));


	}

	private void select2UpdateEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		// Das ist notwendig, weil beim ersten Aufruf der UPDATE
		// Funktion erst die Komponente erstellt wird. Das Event
		// läuft also zuerst ins Leere und muss deshalb nochmal
		// wiederholt werden.
		this.current = event.getItem();

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.SELECT2UPDATE).addEntity(event.getItem().getBean()));

	}

	private void copyEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		// Service Operationen ausführen
		Buerger copy = this.copyBuerger(event.getEntity().getId());

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.COPY).addEntity(copy));

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.text)));
		succes.show(Page.getCurrent());
	}

	private void deleteEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		Notification resultNotification;

		// Service Operationen ausführen
		try {
			this.deleteBuerger(event.getEntity());
			// UI Komponenten aktualisieren
			ComponentEvent<Buerger> componentEvent = buildComponentEvent(EventType.DELETE);
			componentEvent.setItemID(event.getEntity());
			postEvent(componentEvent);
			resultNotification = new GenericSuccessNotification(
					resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.label)),
					resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.text)));
		} catch (HttpClientErrorException e) {
			LOG.warn("Exception: " + e.getMessage());
			if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
				resultNotification = new GenericFailureNotification(
						resolveRelative(getNotificationPath(NotificationType.failure, SimpleAction.delete, Type.label)),
						resolveRelative(getNotificationPath(NotificationType.failure, SimpleAction.delete, Type.text)));
			} else {
				resultNotification = new GenericFailureNotification(
						resolveRelative(getNotificationPath(NotificationType.failure, SimpleAction.delete, Type.label)),
						e.getLocalizedMessage()
				);
			}
		}

		resultNotification.show(Page.getCurrent());
	}

	private void saveChildEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		// Service Operation ausführen
		this.saveBuergerKind(event.getEntity());

		postEvent(buildComponentEvent(EventType.SAVE_CHILD).addEntity(event.getEntity()));

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
		succes.show(Page.getCurrent());
	}

	private void savePartnerEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		// Service Operation ausführen
		this.saveBuergerPartner(event.getEntity());

		postEvent(buildComponentEvent(EventType.SAVE_PARTNER).addEntity(event.getEntity()));

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
		succes.show(Page.getCurrent());
	}

	private void saveEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		// Service Operationen ausführen
		Buerger newBuerger = this.save(event.getEntity());

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.SAVE).addEntity(newBuerger));

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
		succes.show(Page.getCurrent());

	}

	private void updateEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();

		// Service Operationen ausführen
		this.updateBuerger(event.getEntity());

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.UPDATE).addEntity(event.getEntity()));


		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.text)));
		succes.show(Page.getCurrent());

	}

	private void queryPartnerEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
		AppEvent<Buerger> event = eventWrapper.getData();
		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.QUERY_PARTNER).addEntities(this.queryPartner(event.getEntity())));
	}

	public EventBus getBus() {
		return eventbus;
	}
}
