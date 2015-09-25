package de.muenchen.demo.service.rest.search;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.util.QueryService;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/buergers/search/")
public class BuergerSearchController {

    @Autowired
    QueryService service;

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<?> findBuergerFuzzy(PersistentEntityResourceAssembler assembler, @Param("s") String s) {
        if (Objects.isNull(s) || s.length() < 3)
            throw new IllegalArgumentException("Search String must be at least 3 chars long.");

        String[] annotatedFields = Stream
                .of(Buerger.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(IndexedEmbedded.class))
                .map(Field::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);

        List<Buerger> list = service.query(s, Buerger.class, annotatedFields);
        final List<PersistentEntityResource> collect = list.stream().map(assembler::toResource).collect(Collectors.toList());
        return new ResponseEntity<Object>(new Resources<>(collect), HttpStatus.OK);
    }
}
