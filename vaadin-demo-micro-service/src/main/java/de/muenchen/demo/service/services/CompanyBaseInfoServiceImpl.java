/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.CompanyBaseInfo;
import de.muenchen.demo.service.domain.CompanyBaseInfoRepository;
import de.muenchen.demo.service.util.QueryService;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class CompanyBaseInfoServiceImpl extends BaseService<CompanyBaseInfo> implements CompanyBaseInfoService {
    
    private static final Logger LOG = LoggerFactory.getLogger(CompanyBaseInfoService.class);
    


    public CompanyBaseInfoServiceImpl() {
    }

    @Autowired
    public CompanyBaseInfoServiceImpl(CompanyBaseInfoRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, CompanyBaseInfo.class, "name","adresse");
    }
   
    
}

