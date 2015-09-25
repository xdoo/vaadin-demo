package de.muenchen.demo.service.rest.search;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.util.QueryService;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.exception.EmptyQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by p.mueller on 24.09.15.
 */
@BasePathAwareController
@Component
@RequestMapping("/buergers")
public class BuergerSearchController implements ResourceProcessor<Resources<Buerger>> {

    private static final Logger LOG = LoggerFactory.getLogger(BuergerSearchController.class);

    @Autowired
    QueryService service;

    @Autowired
    EntityLinks entityLinks;

    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<?> find(PersistentEntityResourceAssembler assembler, @Param("s") String s) {
        if (Objects.isNull(s))
            return new ResponseEntity<Object>("No Filter given", HttpStatus.BAD_REQUEST);

        String[] annotatedFields = Stream
                .of(Buerger.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(IndexedEmbedded.class))
                .map(Field::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);

        try {
            List<Buerger> list = service.query(s, Buerger.class, annotatedFields);
            final List<PersistentEntityResource> collect = list.stream().map(assembler::toResource).collect(Collectors.toList());
            return new ResponseEntity<Object>(new Resources<>(collect), HttpStatus.OK);
        } catch (EmptyQueryException e) {
            return new ResponseEntity<Object>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Resources<Buerger> process(Resources<Buerger> resource) {
        Link link = new Link(resource.getLink("self").getHref() + "/find", "find");
        LOG.error("ASDF: " + link.getHref());
        resource.add(link);
        return resource;
    }
}
