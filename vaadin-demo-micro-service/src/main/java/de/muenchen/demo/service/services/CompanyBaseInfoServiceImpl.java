/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.CompanyBaseInfo;
import de.muenchen.demo.service.domain.CompanyBaseInfoRepository;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.List;
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
public class CompanyBaseInfoServiceImpl implements CompanyBaseInfoService {
    
    private static final Logger LOG = LoggerFactory.getLogger(CompanyBaseInfoService.class);
    
    CompanyBaseInfoRepository repo;
    QueryService<CompanyBaseInfo> search;

    public CompanyBaseInfoServiceImpl() {
    }

    @Autowired
    public CompanyBaseInfoServiceImpl(CompanyBaseInfoRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, CompanyBaseInfo.class, "adresseOid","ausrichtung","stock");
    }
    
    @Override
    public CompanyBaseInfo create() {
        CompanyBaseInfo companyBaseInfos = new CompanyBaseInfo();
        companyBaseInfos.setOid(IdService.next());
        return companyBaseInfos;
    }

    @Override
    public CompanyBaseInfo save(CompanyBaseInfo companyBaseInfos) {
        LOG.info(companyBaseInfos.toString());
        Preconditions.checkArgument(companyBaseInfos.getId() == null, "On save, the ID must be empty");
        return this.repo.save(companyBaseInfos);
    }
    
    @Override
    public CompanyBaseInfo read(String oid) {
        List<CompanyBaseInfo> result = this.repo.findByOid(oid);
        if(result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no companyBaseInfos with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }
    
    @Override
    public CompanyBaseInfo update(CompanyBaseInfo companyBaseInfos) {
        return this.repo.save(companyBaseInfos);
    }
    
    @Override
    public void delete(String oid) {
        CompanyBaseInfo item = this.read(oid);
        this.repo.delete(item);
    }

    @Override
    public List<CompanyBaseInfo> query() {
        Iterable<CompanyBaseInfo> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<CompanyBaseInfo> query(String query) { 
        return this.search.query(query);
    }

    
    
}

