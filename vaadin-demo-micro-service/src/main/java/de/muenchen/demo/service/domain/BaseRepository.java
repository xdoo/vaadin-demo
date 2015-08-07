/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author praktikant.tmar
 * @param <T>
 */
@NoRepositoryBean
public interface BaseRepository <T> extends CrudRepository<T, Long>  {


    public List<T> findByMandantMid(String mid);
    public List<T> findByOidAndMandantMid(String oid, String mid);

}
