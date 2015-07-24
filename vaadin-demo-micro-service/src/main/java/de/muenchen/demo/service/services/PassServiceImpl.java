/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.PassRepository;
import de.muenchen.demo.service.util.QueryService;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class PassServiceImpl extends BaseService<Pass> implements PassService {

    public PassServiceImpl() {
    }

    @Autowired
    public PassServiceImpl(PassRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Pass.class, "passnummer", "kode","typ","austellungsdatum","gueltigBis","behoerde");
    }

}
