package de.muenchen.auditing;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository-Zugriff für AuditingUserEntities.
 * Created by fabian.holtkoetter on 08.09.15.
 */
public interface AuditingUserRepository extends CrudRepository<AuditingUserEntity, Long> {
}