package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author p.mueller
 */
public class BuergerRestClientImpl implements BuergerRestClient {

    public static final String BUERGERS = "buergers";
    private final Traverson traverson;
    private final RestTemplate restTemplate;

    public BuergerRestClientImpl(RestTemplate restTemplate, URI baseUri) {
        this.restTemplate = restTemplate;

        traverson = new Traverson(baseUri, MediaTypes.HAL_JSON);
        traverson.setRestOperations(restTemplate);
    }

    @Override
    public Resources<BuergerResource> findAll() {
        return traverson
                .follow(BUERGERS)
                .toObject(BuergerResource.LIST);
    }

    @Override
    public Resources<BuergerResource> findAll(Link relation) {
        URI uri = URI.create(relation.getHref());

        return restTemplate
                .exchange(uri, HttpMethod.GET, null, BuergerResource.LIST)
                .getBody();
    }

    @Override
    public Optional<BuergerResource> findOne(Link link) {
        URI uri = URI.create(link.getHref());

        BuergerResource resource = restTemplate
                .exchange(uri, HttpMethod.GET, null, BuergerResource.class)
                .getBody();

        return Optional.of(resource);
    }

    @Override
    public void setRelations(Link link, Collection<Link> links) {
        String relations = links.stream().map(Link::getHref).collect(Collectors.joining("\n"));

        restTemplate.exchange(URI.create(link.getHref()), HttpMethod.PUT, new HttpEntity<Object>(relations), Void.class);
    }

    @Override
    public void create(Buerger buerger) {
        URI uri = URI.create(
                traverson.follow("buergers")
                        .asLink().getHref()
        );

        restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(buerger), Void.class);
    }


    @Override
    public void update(BuergerResource buerger) {
        URI uri = URI.create(buerger.getId().getHref());

        restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(buerger.getContent()), Void.class);
    }

    @Override
    public void delete(Link id) {
        URI uri = URI.create(id.getHref());
        restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
    }
}
