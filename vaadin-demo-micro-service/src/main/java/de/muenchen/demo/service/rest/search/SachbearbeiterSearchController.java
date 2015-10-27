package de.muenchen.demo.service.rest.search;


import de.muenchen.demo.service.gen.domain.Sachbearbeiter;
import de.muenchen.demo.service.gen.rest.SachbearbeiterRepository;
import de.muenchen.service.QueryService;
import org.hibernate.search.exception.EmptyQueryException;
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

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
@BasePathAwareController
@ExposesResourceFor(Sachbearbeiter.class)
@RequestMapping("/sachbearbeiters/search")
public class SachbearbeiterSearchController {

    @Autowired
    QueryService service;

    @Autowired
    SachbearbeiterRepository repository;

    @RequestMapping(method = RequestMethod.GET, value = "findFullTextFuzzy")
    @ResponseBody
    public ResponseEntity<?> findFullTextFuzzy(PersistentEntityResourceAssembler assembler, @Param("q") String q) {
        if (q == null)
            q = "";

        String[] annotatedFields = Stream
                .of(Sachbearbeiter.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(org.hibernate.search.annotations.Field.class))
                .map(Field::getName)
                .toArray(size -> new String[size]);

        Stream<Sachbearbeiter> sachbearbeiterStream;
        try {
            sachbearbeiterStream = service.query(q, Sachbearbeiter.class, annotatedFields).stream();
        } catch (EmptyQueryException e) {
            sachbearbeiterStream = StreamSupport.stream(repository.findAll().spliterator(), false);
        }

        final List<PersistentEntityResource> collect = sachbearbeiterStream.map(assembler::toResource).collect(Collectors.toList());
        return new ResponseEntity<Object>(new Resources<>(collect), HttpStatus.OK);
    }
}

