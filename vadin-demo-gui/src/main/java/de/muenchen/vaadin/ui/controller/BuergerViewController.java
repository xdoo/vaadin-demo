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
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import de.muenchen.vaadin.demo.i18nservice.ControllerContext;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
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

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.NotificationType;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;
import static reactor.bus.selector.Selectors.object;

/**
 * Der Controller ist die zentrale Klasse um die Logik im Kontext Buerger abzubilden.
 *
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class BuergerViewController implements Serializable, ControllerContext<LocalBuerger> {


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
	private BeanItem<LocalBuerger> current;

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

	public BeanItem<LocalBuerger> getCurrent() {
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

	public void registerToAppEvent(EventType type, Consumer<Event<AppEvent<LocalBuerger>>> consumer) {
		getEventbus().on(object(buildAppEvent(type)), consumer);
	}

	public void registerToAllAppEvents(Consumer<Event<AppEvent<LocalBuerger>>> consumer) {
		Stream.of(EventType.values()).forEach(type -> registerToAppEvent(type, consumer));
	}

	public void registerToComponentEvent(EventType type, Consumer<Event<ComponentEvent<LocalBuerger>>> consumer) {
		getEventbus().on(object(buildComponentEvent(type)), consumer);
	}

	public void registerToAllComponentEvents(Consumer<Event<ComponentEvent<LocalBuerger>>> consumer) {
		Stream.of(EventType.values()).forEach(type -> registerToComponentEvent(type, consumer));
	}

	@Override
	public AppEvent<LocalBuerger> buildAppEvent(EventType eventType) {
		return new AppEvent<LocalBuerger>(LocalBuerger.class, eventType);
	}

	@Override
	public ComponentEvent<LocalBuerger> buildComponentEvent(EventType eventType) {
		return new ComponentEvent<>(LocalBuerger.class, eventType);
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
	 * Kopiert eine vorhandene Instanz eines {@link Buerger} Objektes in eine
	 * neue Instanz. Die Kopie wird gleich in der DB gespeichert.
	 *
	 * @param id Buerger der kopiert werden soll
	 */
	public LocalBuerger copyBuerger(Link id) {
		return service.copy(id);
	}

	/**
	 * Speichert ein {@link Buerger} Objekt in der Datenbank.
	 *
	 * @param buerger Buerger der gespeichert werden soll
	 */
	public LocalBuerger save(LocalBuerger buerger) {
		return service.create(buerger);
	}

	/**
	 * Speichert ein Kind zu einem {@link Buerger} Objekt in der Datenbank.
	 *
	 * @param kind Zu speicherndes Kind
	 * @return Buerger
	 */
	public void saveBuergerKind(LocalBuerger kind) {
		LocalBuerger child = save(kind);
		addBuergerKind(child);
	}

	/**
	 * Speichert einen partner zu einem {@link Buerger} Objekt in der Datenbank.
	 *
	 * @param entity Zu speichernder partner
	 * @return Buerger
	 */
	public void saveBuergerPartner(LocalBuerger entity) {
		LocalBuerger newBuerger = save(entity);
		addBuergerPartner(newBuerger);
	}

	/**
	 * Zerstört die Verbindung zwischen einem Bürger und seinem Kind
	 *
	 * @param event
	 */
	private void releaseParent(LocalBuerger event) {
		//service.releaseElternteil(getCurrent().getBean(), event.getEntity());
	}

	/**
	 * Vernichted die Verbindung zwischen einem Bürger und seinem Partner
	 *
	 * @param event
	 */
	private void releasePartner(LocalBuerger event) {
		service.delete(event.getLink(BuergerResource.Rel.partner.name()));
	}


	/**
	 * Speichert eine Kindbeziehung zu einem {@link Buerger} Objekt in der Datenbank.
	 *
	 * @param kindEntity Kind
	 * @return Buerger
	 */
	public void addBuergerKind(LocalBuerger kindEntity) {
		Link link = current.getBean().getLink(BuergerResource.Rel.kinder.name());
		List<Link> kinder = Stream.concat(
				service.findAll(link)
						.stream()
						.map(LocalBuerger::getId),
				Stream.of(kindEntity.getId()))

				.collect(Collectors.toList());

		service.setRelations(link, kinder);
	}

	/**
	 * Speichert eine Partnerschaftesbeziehung zu einem {@link Buerger} Objekt in der Datenbank.
	 *
	 * @param partnerEntity Partner
	 * @return Buerger
	 */
	public void addBuergerPartner(LocalBuerger partnerEntity) {
		Link link = current.getBean().getLink(BuergerResource.Rel.partner.name());
		List<Link> partner = Stream.concat(
				service.findAll(link)
						.stream()
						.map(LocalBuerger::getId),
				Stream.of(partnerEntity.getId()))

				.collect(Collectors.toList());

		service.setRelations(link, partner);
	}

	/**
	 * Speichert die Änderungen an einem {@link Buerger} Objekt in der Datenbank.
	 *
	 * @param entity Buerger
	 * @return Buerger
	 */
	public LocalBuerger updateBuerger(LocalBuerger entity) {
		return service.update(entity);
	}

	/**
	 * Löscht ein {@link Buerger} Objekt.
	 *
	 * @param entity Buerger
	 */
	public void deleteBuerger(LocalBuerger entity) {
		service.delete(entity.getId());
	}

	public List<LocalBuerger> queryBuerger() {
		return service.findAll().stream().collect(Collectors.toList());
	}

	public List<LocalBuerger> queryBuerger(String query) {
		return queryBuerger(); //TODO
	}

	public List<LocalBuerger> queryKinder(LocalBuerger entity) {
		return service.findAll(entity.getLink(BuergerResource.Rel.kinder.name())).stream().collect(Collectors.toList());
	}

	public List<LocalBuerger> queryPartner(LocalBuerger entity) {
		return service.findAll(entity.getLink(BuergerResource.Rel.partner.name())).stream().collect(Collectors.toList());
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
		registerToAppEvent(EventType.RELEASE_PARENT, this::releaseParentHandler);
		registerToAppEvent(EventType.RELEASE_PARTNER, this::releasePartnerHandler);
		registerToAppEvent(EventType.SAVE_AS_PARTNER, this::saveAsPartnerEventHandler);
		registerToAppEvent(EventType.ADD_PARTNER, this::addPartnerEventHandler);
		registerToAppEvent(EventType.QUERY_PARTNER, this::queryPartnerEventHandler);

	}

	private void releasePartnerHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();
		this.releasePartner(event.getEntity());
		// UI Komponenten aktualisieren

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.label, "partner")),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.text, "partner")));
		succes.show(Page.getCurrent());

		postEvent(buildComponentEvent(EventType.DELETE).setItemID(event.getItemId()));
	}

	private void addPartnerEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateBuergerPartnerSearchTable()));
		postEvent(buildAppEvent(EventType.QUERY));
	}

	private void saveAsPartnerEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();
		this.addBuergerPartner(event.getEntity());
		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label, "partner")),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text, "partner")));
		succes.show(Page.getCurrent());
		postEvent(buildComponentEvent(EventType.UPDATE_PARTNER).addEntity(event.getEntity()));
	}

	private void releaseParentHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();
		// Service Operationen ausführen
		this.releaseParent(event.getEntity());
		// UI Komponenten aktualisieren

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.label, "child")),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.text, "child")));
		succes.show(Page.getCurrent());

		postEvent(buildComponentEvent(EventType.DELETE).setItemID(event.getItemId()));

	}

	private void addSearchedChildEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateChildSearchTable()));
		postEvent(buildAppEvent(EventType.QUERY));
	}

	private void saveAsChildEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		this.addBuergerKind(event.getEntity());
		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label, "child")),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text, "child")));
		succes.show(Page.getCurrent());
		postEvent(buildComponentEvent(EventType.UPDATE_CHILD).addEntity(event.getEntity()));
	}

	private void queryChildEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.QUERY_CHILD).addEntities(this.queryKinder(event.getEntity())));
	}

	private void queryEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		List<LocalBuerger> currentEntities;
		if (event.getQuery().isPresent()) {
			currentEntities = this.queryBuerger(event.getQuery().get());
		} else {
			currentEntities = this.queryBuerger();
		}

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.QUERY).addEntities(currentEntities));
	}

	private void select2ReadEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		this.current = event.getItem();

		// UI Komponente aktualisieren
		postEvent(buildComponentEvent(EventType.SELECT2READ).addEntity(event.getItem().getBean()));


	}

	private void select2UpdateEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		// Das ist notwendig, weil beim ersten Aufruf der UPDATE
		// Funktion erst die Komponente erstellt wird. Das Event
		// läuft also zuerst ins Leere und muss deshalb nochmal
		// wiederholt werden.
		this.current = event.getItem();

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.SELECT2UPDATE).addEntity(event.getItem().getBean()));

	}

	private void copyEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		// Service Operationen ausführen
		LocalBuerger copy = this.copyBuerger(event.getEntity().getId());

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.COPY).addEntity(copy));

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.text)));
		succes.show(Page.getCurrent());
	}

	private void deleteEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		Notification resultNotification;

		// Service Operationen ausführen
		try {
			this.deleteBuerger(event.getEntity());
			// UI Komponenten aktualisieren
			ComponentEvent<LocalBuerger> componentEvent = buildComponentEvent(EventType.DELETE);
			componentEvent.setItemID(event.getItemId());
			postEvent(componentEvent);
			resultNotification = new GenericSuccessNotification(
					resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.label)),
					resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.text)));
		} catch (HttpClientErrorException e) {
			LOG.warn(e.getMessage());
			if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
				resultNotification = new GenericErrorNotification(
						resolveRelative(getNotificationPath(NotificationType.failure, SimpleAction.delete, Type.label)),
						resolveRelative(getNotificationPath(NotificationType.failure, SimpleAction.delete, Type.text)));
			} else {
				resultNotification = new GenericErrorNotification(
						resolveRelative(getNotificationPath(NotificationType.failure, SimpleAction.delete, Type.label)),
						e.getLocalizedMessage()
				);
			}
		}

		resultNotification.show(Page.getCurrent());
	}

	private void saveChildEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		// Service Operation ausführen
		this.saveBuergerKind(event.getEntity());

		postEvent(buildComponentEvent(EventType.SAVE_CHILD).addEntity(event.getEntity()));

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
		succes.show(Page.getCurrent());
	}

	private void savePartnerEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		// Service Operation ausführen
		this.saveBuergerPartner(event.getEntity());

		postEvent(buildComponentEvent(EventType.SAVE_PARTNER).addEntity(event.getEntity()));

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
		succes.show(Page.getCurrent());
	}

	private void saveEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		// Service Operationen ausführen
		LocalBuerger newBuerger = this.save(event.getEntity());

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.SAVE).addEntity(newBuerger));

		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
		succes.show(Page.getCurrent());

	}

	private void updateEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();

		// Service Operationen ausführen
		this.updateBuerger(event.getEntity());

		// UI Komponenten aktualisieren
		postEvent(buildComponentEvent(EventType.UPDATE).addEntity(event.getEntity()));


		GenericSuccessNotification succes = new GenericSuccessNotification(
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.label)),
				resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.text)));
		succes.show(Page.getCurrent());

	}

	private void queryPartnerEventHandler(Event<AppEvent<LocalBuerger>> eventWrapper) {
		AppEvent<LocalBuerger> event = eventWrapper.getData();
		queryPartner(event.getEntity());
	}

	public EventBus getBus() {
		return eventbus;
	}
}
