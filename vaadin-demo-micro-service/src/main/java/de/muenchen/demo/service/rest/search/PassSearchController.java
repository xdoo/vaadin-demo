package de.muenchen.demo.service.rest.search;


import de.muenchen.demo.service.gen.domain.Buerger;
import de.muenchen.demo.service.gen.domain.Pass;
import de.muenchen.demo.service.gen.rest.PassRepository;
import de.muenchen.service.QueryService;
import org.hibernate.search.exception.EmptyQueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
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
@ExposesResourceFor(Pass.class)
@RequestMapping("/passs/search")
public class PassSearchController {

    @Autowired
    QueryService service;

    @Autowired
    PassRepository repository;

    @Autowired
    PagedResourcesAssembler pagedResourcesAssembler;

    @RequestMapping(method = RequestMethod.GET, value = "findFullTextFuzzy")
    @ResponseBody
    public PagedResources<?> findFullTextFuzzy(PersistentEntityResourceAssembler assembler, Pageable p, @Param("q") String q) {
        if (q == null)
            q = "";

        String[] annotatedFields = Stream
                .of(Pass.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(org.hibernate.search.annotations.Field.class))
                .map(Field::getName)
                .toArray(String[]::new);

        List<Pass> passResults;
        try {
            passResults = service.query(q, Pass.class, annotatedFields);
        } catch (EmptyQueryException e) {
            passResults = StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
        }

        final List<Pass> passPage = passResults.subList(p.getOffset(), Math.min(p.getOffset() + p.getPageSize(), passResults.size()));

        final Page<?> page = new PageImpl<>(passPage, p, passResults.size());
        final PagedResources<?> collect = pagedResourcesAssembler.toResource(page, assembler);
        return collect;
    }
}


