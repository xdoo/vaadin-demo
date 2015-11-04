package de.muenchen.service;

import org.springframework.data.rest.webmvc.RepositorySearchesResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by p.mueller on 28.09.15.
 */
@Component
public class SearchResourcesProcessor implements ResourceProcessor<RepositorySearchesResource> {

    @Override
    public RepositorySearchesResource process(RepositorySearchesResource repositorySearchesResource) {
        final String search = repositorySearchesResource.getId().getHref();
        final Link findFullTextFuzzy = new Link(search + "/findFullTextFuzzy{?q}").withRel("findFullTextFuzzy");
        repositorySearchesResource.add(findFullTextFuzzy);

        return repositorySearchesResource;
    }
}