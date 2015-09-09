package de.muenchen.demo.service.auditing;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by fabian.holtkoetter on 08.09.15.
 */
public interface AuditingUserRepository extends CrudRepository<AuditingUserEntity, Long> {
}
