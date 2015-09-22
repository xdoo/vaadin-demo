package de.muenchen.auditing;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Queue zum Puffern von AuditingEvents
 * Created by fabian.holtkoetter on 17.09.15.
 */
public class EntitySaveQueue {
    private Queue<AuditingUserEntity> entities = new LinkedBlockingQueue<>();
    private boolean available = false;


    public synchronized AuditingUserEntity get() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new AssertionError("Can not happen.");
            }
        }
        available = !entities.isEmpty();
        return entities.poll();
    }

    public synchronized void put(AuditingUserEntity entity) {
        entities.add(entity);
        available = true;
        notifyAll();
    }
}
