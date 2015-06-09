package de.muenchen.demo.service.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author claus.straube
 */
public interface BurgerRepository extends PagingAndSortingRepository<Buerger, Long> {
    
}
