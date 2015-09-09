/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface CompanyBaseInfoRepository  extends CrudRepository<CompanyBaseInfo, Long> {
        public List<CompanyBaseInfo> findByOid(String mid);


}
