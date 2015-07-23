package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author claus.straube
 */
public interface AdresseInterneRepository extends PagingAndSortingRepository<AdresseInterne, Long> {

    public List<AdresseInterne> findByOid(String oid);

    public List<AdresseInterne> findByMandantOid(String oid);

    public List<AdresseInterne> findByOidAndMandantOid(String Oid, String mOid);

}
