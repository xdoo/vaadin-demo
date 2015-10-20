package de.muenchen.demo.service.rest.search;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.service.QueryService;
import org.hibernate.search.exception.EmptyQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by p.mueller on 24.09.15.
 */
@BasePathAwareController
@ExposesResourceFor(Buerger.class)
@RequestMapping("/buergers/search")
public class BuergerSearchController {

    private static final Logger LOG = LoggerFactory.getLogger(BuergerSearchController.class);

    @Autowired
    QueryService service;

    @Autowired
    BuergerRepository repository;

    @RequestMapping(method = RequestMethod.GET, value = "findFullTextFuzzy")
    @ResponseBody
    public ResponseEntity<?> findFullTextFuzzy(PersistentEntityResourceAssembler assembler, @Param("q") String q) {
        if (q == null)
            q = "";

        String[] annotatedFields = Stream
                .of(Buerger.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(org.hibernate.search.annotations.Field.class))
                .map(Field::getName)
                .toArray(size -> new String[size]);

        Stream<Buerger> buergerStream;
        try {
            buergerStream = service.query(q, Buerger.class, annotatedFields).stream();
        } catch (EmptyQueryException e) {
            buergerStream = StreamSupport.stream(repository.findAll().spliterator(), false);
        }

        final List<PersistentEntityResource> collect = buergerStream.map(assembler::toResource).collect(Collectors.toList());
        return new ResponseEntity<Object>(new Resources<>(collect), HttpStatus.OK);
    }
}
