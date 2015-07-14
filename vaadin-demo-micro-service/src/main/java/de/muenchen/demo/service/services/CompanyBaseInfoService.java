/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.CompanyBaseInfo;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface CompanyBaseInfoService {
    
    public List<CompanyBaseInfo> query();
    
    public List<CompanyBaseInfo> query(String query);
    
    public CompanyBaseInfo create();
    
    public CompanyBaseInfo save(CompanyBaseInfo companyBaseInfos);
    
    public CompanyBaseInfo read(String oid);
    
    public CompanyBaseInfo update(CompanyBaseInfo companyBaseInfos);
    
    public void delete(String oid);
    
}
