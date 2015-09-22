package de.muenchen.auditing;

import java.util.Objects;

/**
 * AuditingServiceConsumer läuft im eigenen Thread und ist zum asynchronen SPeichern der Auditing-Daten in der Datenbank zuständig.
 * Created by fabian.holtkoetter on 17.09.15.
 */
public class AuditingServiceConsumer implements Runnable {

    AuditingUserRepository repo;
    EntitySaveQueue queue;

    public AuditingServiceConsumer(AuditingUserRepository repo, EntitySaveQueue queue) {
        this.repo = repo;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            AuditingUserEntity entity = queue.get();
            if (Objects.nonNull(entity))
                repo.save(entity);
        }
    }
}