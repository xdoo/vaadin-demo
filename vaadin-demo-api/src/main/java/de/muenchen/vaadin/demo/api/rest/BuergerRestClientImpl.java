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
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<BuergerResource> exchange = restTemplate
                .exchange(uri, HttpMethod.GET, null, BuergerResource.class);
        return Optional.of(buergerAssembler.toBean(exchange.getBody(), exchange.getHeaders()));
    }

    @Override
    public void setRelations(Link endpoint, Collection<Link> links) {
        String relations = links.stream().map(Link::getHref).collect(Collectors.joining("\n"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "uri-list"));
        restTemplate.exchange(URI.create(endpoint.getHref()),
                HttpMethod.PUT,
                new HttpEntity<>(relations, headers),
                Void.class);
    }

    @Override
    public Buerger create(Buerger buerger) {
        URI uri = URI.create(
                traverson.follow("buergers")
                        .asLink().getHref()
        );

        BuergerDTO buergerDTO = buergerAssembler.toResource(buerger).getContent();

        ResponseEntity<BuergerResource> exchange = restTemplate.exchange(uri,
                HttpMethod.POST,
                new HttpEntity<>(buergerDTO),
                BuergerResource.class);

        return buergerAssembler.toBean(exchange.getBody(), exchange.getHeaders());
    }


    @Override
    public Buerger update(Buerger buerger) {
        URI uri = URI.create(buerger.getId().getHref());

        BuergerDTO buergerDTO = buergerAssembler.toResource(buerger).getContent();


        ResponseEntity<BuergerResource> exchange = restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                new HttpEntity<>(buergerDTO, getHttpHeaders(buerger)),
                BuergerResource.class);

        return buergerAssembler.toBean(exchange.getBody(), exchange.getHeaders());
    }

    @Override
    public void delete(Buerger buerger) {
        URI uri = URI.create(buerger.getId().getHref());

        restTemplate.exchange(
                uri,
                HttpMethod.DELETE,
                new HttpEntity<>(getHttpHeaders(buerger)),
                Void.class);
    }

    private HttpHeaders getHttpHeaders(Buerger buerger) {
        HttpHeaders httpHeaders = new HttpHeaders();
        buerger.getHeaders()
                .ifPresent(headers -> httpHeaders.add(HttpHeaders.IF_MATCH, headers.getETag()));

        return httpHeaders;
    }
}
