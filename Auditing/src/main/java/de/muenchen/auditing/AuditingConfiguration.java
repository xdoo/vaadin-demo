package de.muenchen.auditing;

import de.muenchen.eventbus.EventBus;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;

import javax.persistence.EntityManagerFactory;
import java.util.stream.Stream;

import static de.muenchen.eventbus.types.EventType.*;

/**
 * Created by fabian.holtkoetter on 08.09.15.
 */
public class AuditingConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(AuditingConfiguration.class);

    private EntityManagerFactory entityManagerFactory;
    EventBus eventbus;

    public AuditingConfiguration(EntityManagerFactory entityManagerFactory, EventBus eventbus) {
        this.entityManagerFactory = entityManagerFactory;
        this.eventbus = eventbus;
    }

    public void registerListeners() {
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
                        eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(AUDIT_READ, eventEntity)));
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
                            eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(AUDIT_DELETE, eventEntity)));
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
                            eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(AUDIT_CREATE, eventEntity)));
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
                            eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(AUDIT_UPDATE, eventEntity)));
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