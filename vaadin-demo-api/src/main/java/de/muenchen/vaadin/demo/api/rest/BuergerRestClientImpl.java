package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.BuergerDTO;
import de.muenchen.vaadin.demo.api.hateoas.BuergerAssembler;
import de.muenchen.vaadin.demo.api.local.Buerger;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides a simple (and Spring specific) implementation for a {@link BuergerRestClient}.
 *
 * @author p.mueller
 * @version 1.0
 */
public class BuergerRestClientImpl implements BuergerRestClient {

    /**
     * Used to follow HATEOAS relations.
     */
    private final Traverson traverson;
    /** The restTemplate used for the HTTP Requests. */
    private final RestTemplate restTemplate;
    /** Assembler to switch from BuergerDTO Resource to Buerger and vice versa. */
    private final BuergerAssembler buergerAssembler = new BuergerAssembler();

    /**
     * Create a new BuergerRestClient by RestTemplate and baseUri of the server.
     * @param restTemplate The restTemplate for the HTTP Requests.
     * @param baseUri The base URI of the REST Server.
     */
    public BuergerRestClientImpl(RestTemplate restTemplate, URI baseUri) {
        if (restTemplate == null) throw new NullPointerException("RestTemplate must not be null!");
        if (baseUri == null) throw new NullPointerException("BaseUri must not be null!");
        this.restTemplate = restTemplate;

        traverson = new Traverson(baseUri, MediaTypes.HAL_JSON);
        traverson.setRestOperations(restTemplate);
    }

    @Override
    public List<Buerger> findAll() {
        return traverson
                .follow(BUERGERS)
                .toObject(BuergerResource.LIST).getContent()

                .stream()
                .map(buergerAssembler::toBean)
                .collect(Collectors.toList());
    }

    @Override
    public List<Buerger> findAll(Link relation) {
        URI uri = URI.create(relation.getHref());

        return restTemplate
                .exchange(uri, HttpMethod.GET, null, BuergerResource.LIST)
                .getBody()
                .getContent()

                .stream()
                .map(buergerAssembler::toBean)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Buerger> findOne(Link link) {
        URI uri = URI.create(link.getHref());
        try {
            BuergerResource resource = restTemplate
                    .exchange(uri, HttpMethod.GET, null, BuergerResource.class)
                    .getBody();
            return Optional.of(buergerAssembler.toBean(resource));
        } catch (HttpClientErrorException e){
            return Optional.empty();
        }
    }

    @Override
    public void setRelations(Link endpoint, Collection<Link> links) {
        String relations = links.stream().map(Link::getHref).collect(Collectors.joining("\n"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "uri-list"));
        restTemplate.exchange(URI.create(endpoint.getHref()), HttpMethod.PUT, new HttpEntity<>(relations, headers), Void.class);
    }

    @Override
    public void setRelation(Link endpoint, Link relation) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "uri-list"));
        restTemplate.exchange(URI.create(endpoint.getHref()), HttpMethod.PUT, new HttpEntity<>(relation.getHref(), headers), Void.class);
    }

    @Override
    public Buerger create(Buerger buerger) {
        URI uri = URI.create(
                traverson.follow("buergers")
                        .asLink().getHref()
        );

        BuergerDTO buergerDTO = buergerAssembler.toResource(buerger).getContent();

        BuergerResource resource = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(buergerDTO), BuergerResource.class).getBody();

        return buergerAssembler.toBean(resource);
    }


    @Override
    public Buerger update(Buerger buerger) {
        URI uri = URI.create(buerger.getId().getHref());

        BuergerDTO buergerDTO = buergerAssembler.toResource(buerger).getContent();

        BuergerResource resource = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(buergerDTO), BuergerResource.class).getBody();

        return buergerAssembler.toBean(resource);
    }

    @Override
    public void delete(Link id) {
        URI uri = URI.create(id.getHref());
        restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
    }
}
