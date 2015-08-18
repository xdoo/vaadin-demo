/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Pass;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface PassService {

    public List<Pass> query();

    public List<Pass> query(String query);

    public Pass create();

    public Pass save(Pass pass);

    public Pass read(String oid);

    public Pass update(Pass pass);

    public void delete(String oid);

    public Pass copy(String oid);

    public Pass readStaat(String oid);

    public void deletePassStaat(String adresseOid);

}
