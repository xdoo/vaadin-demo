package de.muenchen.auditing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.bus.Event;

import java.util.Date;
import java.util.Objects;

import static reactor.bus.selector.Selectors.T;

/**
 * Created by fabian.holtkoetter on 07.09.15.
 */
public class AuditingService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditingService.class);

    AuditingUserRepository repo;
    EventBus eventbus;

    public AuditingService(EventBus eventbus, AuditingUserRepository repo) {
        this.repo = repo;
        this.eventbus = eventbus;
        this.eventbus.on(T(AuditingEvent.class), this::eventHandler);
    }

    public void eventHandler(Event<AuditingEvent> eventWrapper) {
        //new Thread(() -> {
        AuditingUserEntity entity = createAuditingUserEntity(eventWrapper.getData());
        //repo.save(entity);
        //}).start();
    }

    private AuditingUserEntity createAuditingUserEntity(AuditingEvent event) {
        AuditingUserEntity entity = new AuditingUserEntity();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date(System.currentTimeMillis());
        String username;
        String auditingEvent = "";

        if (!Objects.isNull(authentication) && authentication.isAuthenticated()) {
            username = authentication.getName();
        } else {
            username = "Unauthenticated User";
        }

        try {
            auditingEvent = mapper.writeValueAsString(event.getEntity());
            //auditingEvent = auditingEvent.substring(0, Math.min(auditingEvent.length(), 0xFFFF));
        } catch (StackOverflowError err) {
            LOG.error("StackOverFlow occurred while Marshalling Entity. Make sure to use @JsonManagedReference and @JsonBackReference.");
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }

        entity.setDate(date);
        entity.setUsername(username);
        entity.setEntity(auditingEvent);
        entity.setChangeType(event.getEventType().name());

        return entity;
    }
}
