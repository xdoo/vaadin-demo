package de.muenchen.auditing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.selector.RequestKey;
import de.muenchen.eventbus.types.RequestEvent;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.bus.Event;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

import static de.muenchen.eventbus.types.EventType.*;
import static reactor.bus.selector.Selectors.$;
import static reactor.bus.selector.Selectors.T;

/**
 * AuditingServiceProducer registriert sich mit Listener auf Änderungen der Datenbank um bei Änderungenszugriffen Userdaten zu speichern.
 * Created by fabian.holtkoetter on 07.09.15.
 */
@Component
public class AuditingServiceProducer {

    private static final Logger LOG = LoggerFactory.getLogger(AuditingServiceProducer.class);

    private EntitySaveQueue queue;
    private EventBus eventbus;
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public AuditingServiceProducer(EntityManagerFactory entityManagerFactory, EventBus eventbus, AuditingUserRepository auditingUserRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.eventbus = eventbus;
        queue = new EntitySaveQueue();
        this.init();
        new Thread(new AuditingServiceConsumer(auditingUserRepository, queue)).start();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Objects.nonNull(authentication) && authentication.isAuthenticated()) ? authentication.getName() : "Unauthenticated User";
    }

    private String marshallEntityToJSON(AuditingEvent event) {
        ObjectMapper mapper = new ObjectMapper();
        String json;

        try {
            json = mapper.writeValueAsString(event.getEntity());
        } catch (StackOverflowError err) {
            LOG.error("StackOverFlow occurred while Marshalling Entity. Make sure to use @JsonManagedReference and @JsonBackReference on circular dependencies.");
            json = event.getEntity().toString();
        } catch (JsonProcessingException e) {
            LOG.error("Fehler beim JSON erstellen. Event war <" + event.getEventType() + ">. Fehlermeldung: " + e.getMessage());
            json = event.getEntity().toString();
        }

        return json;
    }

    public void init() {
        this.eventbus.on($(AuditingEvent.class), this::eventHandler);
        this.registerListeners();
    }

    private void eventHandler(Event<AuditingEvent> event) {
        AuditingEvent audEvent = event.getData();

        AuditingUserEntity entity = new AuditingUserEntity();
        entity.setUsername(getCurrentUsername());
        entity.setDate(new Date(System.currentTimeMillis()));
        entity.setChangeType(audEvent.getEventType().name());
        entity.setEntity(marshallEntityToJSON(audEvent));

        queue.put(entity);
    }

    private void registerListeners() {
        HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) this.entityManagerFactory;
        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) hibernateEntityManagerFactory.getSessionFactory();
        EventListenerRegistry registry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.POST_LOAD).clear();
        registry.getEventListenerGroup(EventType.POST_DELETE).clear();
        registry.getEventListenerGroup(EventType.POST_INSERT).clear();
        registry.getEventListenerGroup(EventType.POST_UPDATE).clear();

        registry.getEventListenerGroup(EventType.POST_LOAD)
                .appendListener(postLoadEvent -> {
                    Object eventEntity = postLoadEvent.getEntity();
                    MUCAudited annotation = eventEntity.getClass().getAnnotation(MUCAudited.class);
                    if (shouldBeAudited(MUCAudited.READ, annotation)) {
                        //noinspection unchecked
                        eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(RequestEvent.READ_LIST, eventEntity)));
                    }
                });
        registry.getEventListenerGroup(EventType.POST_DELETE)
                .appendListener(new PostCommitDeleteEventListener() {
                    @Override
                    public void onPostDeleteCommitFailed(PostDeleteEvent postDeleteEvent) {
                    }

                    @Override
                    public void onPostDelete(PostDeleteEvent postDeleteEvent) {
                        Object eventEntity = postDeleteEvent.getEntity();
                        MUCAudited annotation = eventEntity.getClass().getAnnotation(MUCAudited.class);
                        if (shouldBeAudited(MUCAudited.DELETE, annotation)) {
                            //noinspection unchecked
                            eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(RequestEvent.DELETE, eventEntity)));
                        }
                    }

                    @Override
                    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
                        return false;
                    }
                });
        registry.getEventListenerGroup(EventType.POST_INSERT)
                .appendListener(new PostCommitInsertEventListener() {
                    @Override
                    public void onPostInsertCommitFailed(PostInsertEvent postInsertEvent) {
                    }

                    @Override
                    public void onPostInsert(PostInsertEvent postInsertEvent) {
                        Object eventEntity = postInsertEvent.getEntity();
                        MUCAudited annotation = eventEntity.getClass().getAnnotation(MUCAudited.class);
                        if (shouldBeAudited(MUCAudited.CREATE, annotation)) {
                            //noinspection unchecked
                            eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(RequestEvent.CREATE, eventEntity)));
                        }
                    }

                    @Override
                    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
                        return false;
                    }
                });
        registry.getEventListenerGroup(EventType.POST_UPDATE)
                .appendListener(new PostCommitUpdateEventListener() {
                    @Override
                    public void onPostUpdateCommitFailed(PostUpdateEvent postUpdateEvent) {
                    }

                    @Override
                    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
                        Object eventEntity = postUpdateEvent.getEntity();
                        MUCAudited annotation = eventEntity.getClass().getAnnotation(MUCAudited.class);
                        if (shouldBeAudited(MUCAudited.UPDATE, annotation)) {
                            //noinspection unchecked
                            eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(RequestEvent.UPDATE, eventEntity)));
                        }
                    }

                    @Override
                    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
                        return false;
                    }
                });
    }

    private boolean shouldBeAudited(String event, MUCAudited annotation) {
        if (annotation == null) {
            return false;
        }
        String[] values = annotation.value();
        return Stream.of(values).anyMatch(s -> s.equals(MUCAudited.ALL) || s.equals(event));
    }
}
