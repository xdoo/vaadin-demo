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
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author p.mueller
 */
public class BuergerRestClientImpl implements BuergerRestClient {

    public static final String BUERGERS = "buergers";
    private final Traverson traverson;
    private final RestTemplate restTemplate;

    private final BuergerAssembler buergerAssembler = new BuergerAssembler();

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

        BuergerResource resource = restTemplate
                .exchange(uri, HttpMethod.GET, null, BuergerResource.class)
                .getBody();

        return Optional.of(buergerAssembler.toBean(resource));
    }

    @Override
    public void setRelations(Link link, Collection<Link> links) {
        String relations = links.stream().map(Link::getHref).collect(Collectors.joining("\n"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "uri-list"));
        restTemplate.exchange(URI.create(link.getHref()), HttpMethod.PUT, new HttpEntity<>(relations, headers), Void.class);
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
