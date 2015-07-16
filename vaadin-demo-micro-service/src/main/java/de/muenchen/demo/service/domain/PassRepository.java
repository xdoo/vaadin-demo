/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author praktikant.tmar
 */
public interface PassRepository extends PagingAndSortingRepository<Pass, Long> {
    
    List<Pass> findByOid(String oid);
    
}
