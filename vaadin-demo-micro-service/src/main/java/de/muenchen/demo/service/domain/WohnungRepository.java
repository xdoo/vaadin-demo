package de.muenchen.demo.service.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * @author claus.straube
 */
@PreAuthorize("hasRole('PERM_READ_Wohnung')")
public interface WohnungRepository extends CrudRepository<Wohnung, Long> {

    String Wohnung_CACHE = "WOHNUNG_CACHE";

    Wohnung findFirstByOidAndMandantOid(String oid, String mid);


    @Override
    @PreAuthorize("hasRole('PERM_WRITE_Wohnung')")
    Wohnung save(Wohnung entity);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Wohnung')")
    void delete(Wohnung entity);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Wohnung')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Wohnung')")
    void delete(Iterable<? extends Wohnung> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Wohnung')")
    void deleteAll();

    Wohnung findByAdresseOidAndMandantOid(String oid, String mid);

    List<Wohnung> findByMandantOid(String oid);

}
