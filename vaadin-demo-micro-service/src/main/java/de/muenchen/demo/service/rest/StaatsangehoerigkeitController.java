package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.rest.api.StaatsangehoerigkeitResourceAssembler;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.rest.StaatsangehoerigkeitResource;
import de.muenchen.demo.service.services.StaatsangehoerigkeitService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
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
    @Autowired
    StaatsangehoerigkeitResourceAssembler assembler;

    /**
     * Alle Staatsangehoerigkeiten suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryStaatsangehoerigkeit"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryStaatsangehoerigkeit() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query permission");
        }
        SearchResultResource<StaatsangehoerigkeitResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(StaatsangehoerigkeitController.class).queryStaatsangehoerigkeit()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt eine Staatsangehoerigkeit zur OID.
     *
     * @param referencedOid
     * @return
     */
    @RolesAllowed({"PERM_createStaatsangehoerigkeit"})
    @RequestMapping(value = "/create/{referencedOid}", method = {RequestMethod.GET})
    public ResponseEntity createStaatsangehoerigkeit(@PathVariable("referencedOid") String referencedOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new Staatsangehoerigkeit");
        }
        Staatsangehoerigkeit entity = this.service.create(referencedOid);
        StaatsangehoerigkeitResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest eine Staatsangehoerigkeit zur OID.
     *
     * @param referencedOid
     * @return
     */
    @RolesAllowed({"PERM_readStaatsangehoerigkeit"})
    @RequestMapping(value = "/{referencedOid}", method = {RequestMethod.GET})
    public ResponseEntity readStaatsangehoerigkeit(@PathVariable("referencedOid") String referencedOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read Staatsangehoerigkeit");
        }
        Staatsangehoerigkeit entity = this.service.read(referencedOid);
        StaatsangehoerigkeitResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine Staatsangehoerigkeit.
     *
     * @param referencedOid
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
