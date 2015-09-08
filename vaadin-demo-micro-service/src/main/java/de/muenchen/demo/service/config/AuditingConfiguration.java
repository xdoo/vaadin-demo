package de.muenchen.demo.service.config;

import de.muenchen.demo.service.domain.BaseEntity;
import de.muenchen.demo.service.util.Eventbus;
import de.muenchen.demo.service.util.MUCAudited;
import de.muenchen.demo.service.util.events.AuditingEvent;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostCommitDeleteEventListener;
import org.hibernate.event.spi.PostCommitInsertEventListener;
import org.hibernate.event.spi.PostCommitUpdateEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import reactor.bus.Event;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import static de.muenchen.eventbus.types.EventType.AUDIT_CREATE;
import static de.muenchen.eventbus.types.EventType.AUDIT_DELETE;
import static de.muenchen.eventbus.types.EventType.AUDIT_READ;
import static de.muenchen.eventbus.types.EventType.AUDIT_UPDATE;

/**
 * Created by fabian.holtkoetter on 08.09.15.
 */
@Configuration
public class AuditingConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(AuditingConfiguration.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    Eventbus eventbus;

    @PostConstruct
    public void registerListeners() {
        HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) this.entityManagerFactory;
        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) hibernateEntityManagerFactory.getSessionFactory();
        EventListenerRegistry registry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.POST_LOAD).clear();
        registry.getEventListenerGroup(EventType.POST_COMMIT_DELETE).clear();
        registry.getEventListenerGroup(EventType.POST_COMMIT_INSERT).clear();
        registry.getEventListenerGroup(EventType.POST_COMMIT_UPDATE).clear();

        registry.getEventListenerGroup(EventType.POST_LOAD)
                .appendListener(postLoadEvent -> {
                    Object eventEntity = postLoadEvent.getEntity();
                    MUCAudited annotation = eventEntity.getClass().getAnnotation(MUCAudited.class);
                    if (annotation != null &&
                            (annotation.value().equals(MUCAudited.ALL) || annotation.value().equals(MUCAudited.READ))) {
                        BaseEntity entity = (BaseEntity) eventEntity;
                        eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(AUDIT_READ, entity)));
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
                        if (annotation != null &&
                                (annotation.value().equals(MUCAudited.ALL) || annotation.value().equals(MUCAudited.DELETE))) {
                            BaseEntity entity = (BaseEntity) eventEntity;
                            eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(AUDIT_DELETE, entity)));
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
                        if (annotation != null &&
                                (annotation.value().equals(MUCAudited.ALL) || annotation.value().equals(MUCAudited.CREATE))) {
                            BaseEntity entity = (BaseEntity) eventEntity;
                            eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(AUDIT_CREATE, entity)));
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
                        if (annotation != null &&
                                (annotation.value().equals(MUCAudited.ALL) || annotation.value().equals(MUCAudited.UPDATE))) {
                            BaseEntity entity = (BaseEntity) eventEntity;
                            eventbus.notify(AuditingEvent.class, Event.wrap(new AuditingEvent(AUDIT_UPDATE, entity)));
                        }
                    }

                    @Override
                    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
                        return false;
                    }
                });
    }
}
