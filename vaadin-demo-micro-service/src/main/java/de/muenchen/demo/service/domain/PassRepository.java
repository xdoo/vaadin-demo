/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.domain.permissions.Perm;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
@Secured({Perm.READ + "Pass"})
public interface PassRepository extends CrudRepository<Pass, Long> {

    String Pass_CACHE = "PASS_CACHE";


    @Override
    @CachePut(value = Pass_CACHE, key = "#p0.oid + #p0.mandant.oid")
    @Secured({Perm.WRITE + "Pass"})
    Pass save(Pass entity);

    @Override
    @Secured({Perm.DELETE + "Pass"})
    void delete(Long aLong);

    @Override
    @Secured({Perm.DELETE + "Pass"})
    void delete(Iterable<? extends Pass> iterable);

    @Override
    @Secured({Perm.DELETE + "Pass"})
    @CacheEvict(value = Pass_CACHE, key = "#p0.oid + #p0.mandant.oid")
    void delete(Pass authorityPermission);

    @Override
    @Secured({Perm.DELETE + "Pass"})
    void deleteAll();

    @Cacheable(value = Pass_CACHE, key = "#p0 + #p1")
    Pass findFirstByOidAndMandantOid(String oid, String mid);


    public List<Pass> findByMandantOid(String mid);

    List<Pass> findByOid(String oid);

    public Pass findFirstByStaatsangehoerigkeitReferenceReferencedOidAndMandantOid(String oid, String mid);

}
