package de.muenchen.auditing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * AuditingServiceProducer registriert sich mit Listener auf Änderungen der Datenbank um bei Änderungenszugriffen Userdaten zu speichern.
 * Created by fabian.holtkoetter on 07.09.15.
 */
@Component
public class AuditingServiceProducer {

    private static final Logger LOG = LoggerFactory.getLogger(AuditingServiceProducer.class);

    private EntitySaveQueue queue;
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public AuditingServiceProducer(EntityManagerFactory entityManagerFactory, AuditingUserRepository auditingUserRepository) {
        this.entityManagerFactory = entityManagerFactory;
        queue = new EntitySaveQueue();
        this.init();
        new Thread(new AuditingServiceConsumer(auditingUserRepository, queue)).start();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Objects.nonNull(authentication) && authentication.isAuthenticated()) ? authentication.getName() : "Unauthenticated User";
    }

    private String marshallEntityToJSON(Object entity) {
        ObjectMapper mapper = new ObjectMapper();
        String json;

        try {
            json = mapper.writeValueAsString(entity);
        } catch (StackOverflowError err) {
            String message = "StackOverFlow occurred while Marshalling Entity. Make sure to use @JsonManagedReference and @JsonBackReference on circular dependencies.";
            LOG.error(message);
            json = message + "Class was: " + entity.getClass();
        } catch (JsonProcessingException e) {
            String message = "Fehler beim JSON erstellen. Fehlermeldung: " + e.getMessage();
            LOG.error(message);
            json = message;
        }

        return json;
    }

    public void init() {
        this.registerListeners();
    }

    private void pushToQueue(String event, Object data) {
        AuditingUserEntity entity = new AuditingUserEntity();
        entity.setUsername(getCurrentUsername());
        entity.setDate(new Date(System.currentTimeMillis()));
        entity.setChangeType(event);
        entity.setEntity(marshallEntityToJSON(data));

        queue.put(entity);
    }

    private void registerListeners() {
        HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) this.entityManagerFactory;
        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) hibernateEntityManagerFactory.getSessionFactory();
        EventListenerRegistry registry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.POST_LOAD)
                .appendListener(postLoadEvent -> {
                    Object eventEntity = postLoadEvent.getEntity();
                    MUCAudited annotation = eventEntity.getClass().getAnnotation(MUCAudited.class);
                    if (shouldBeAudited(MUCAudited.READ, annotation)) {
                        pushToQueue(MUCAudited.READ, eventEntity);
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
                            pushToQueue(MUCAudited.DELETE, eventEntity);
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
                            pushToQueue(MUCAudited.CREATE, eventEntity);
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
                            pushToQueue(MUCAudited.UPDATE, eventEntity);
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
