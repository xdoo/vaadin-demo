package de.muenchen.demo.service.domain;

import de.muenchen.auditing.AuditingUserEntity;
import de.muenchen.auditing.AuditingUserRepository;

import javax.persistence.Entity;

/**
 * Created by fabian.holtkoetter on 09.09.15.
 */
//@RepositoryRestResource(exported = false)
public interface AuditingUserRepositoryImpl extends AuditingUserRepository {
}


@Entity
class AuditingUserEntityImpl extends AuditingUserEntity {
}
