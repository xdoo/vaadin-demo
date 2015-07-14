/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.services.StaatsangehoerigkeitService;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author praktikant.tmar
 */
@Controller
@ExposesResourceFor(StaatsangehoerigkeitController.class)
@RequestMapping("/staat")
public class StaatsangehoerigkeitController {

    private static final Logger LOG = LoggerFactory.getLogger(StaatsangehoerigkeitController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    StaatsangehoerigkeitService service;

    /**
     * Alle Staatsangehoerigkeiten suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryStaatsangehoerigkeit"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryStaatsangehoerigkeit() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query Staatsangehoerigkeit");
        }
        List<Staatsangehoerigkeit> entity = this.service.query();
        return ResponseEntity.ok(entity);
    }

    /**
     * Erzeugt eine Staatsangehoerigkeit zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_createStaatsangehoerigkeit"})
    @RequestMapping(value = "/create/{referencedOid}", method = {RequestMethod.GET})
    public ResponseEntity createStaatsangehoerigkeit(@PathVariable("referencedOid") String referencedOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new Staatsangehoerigkeit");
        }
        Staatsangehoerigkeit entity = this.service.create(referencedOid);
        return ResponseEntity.ok(entity);
    }

    /**
     * Liest eine Staatsangehoerigkeit zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readStaatsangehoerigkeit"})
    @RequestMapping(value = "/{referencedOid}", method = {RequestMethod.GET})
    public ResponseEntity readStaatsangehoerigkeit(@PathVariable("referencedOid") String referencedOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read Staatsangehoerigkeit");
        }
        Staatsangehoerigkeit entity = this.service.read(referencedOid);
        return ResponseEntity.ok(entity);
    }

    /**
     * Löscht eine Staatsangehoerigkeit.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deleteStaatsangehoerigkeit"})
    @RequestMapping(value = "/{referencedOid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteStaatsangehoerigkeit(@PathVariable("referencedOid") String referencedOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete Staatsangehoerigkeit");
        }
        this.service.delete(referencedOid);
        return ResponseEntity.ok().build();
    }

}
