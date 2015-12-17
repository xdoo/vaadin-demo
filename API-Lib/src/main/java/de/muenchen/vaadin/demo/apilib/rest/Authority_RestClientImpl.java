package de.muenchen.vaadin.demo.apilib.rest;


import de.muenchen.vaadin.demo.apilib.domain.Authority_DTO;
import de.muenchen.vaadin.demo.apilib.hateoas.Authority_Assembler;
import de.muenchen.vaadin.demo.apilib.local.Authority_;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Authority_RestClientImpl implements Authority_RestClient {
	
	public static final String FIND_FULL_TEXT_FUZZY = "findFullTextFuzzy";
	public static final String SEARCH = "search";
	
	/**
     * Used to follow HATEOAS relations.
    */
    private final Traverson traverson;
    /** The restTemplate used for the HTTP Requests. */
    private final RestTemplate restTemplate;
    /** Assembler to switch from Authority_DTO Resource to Authority_ and vice versa. */
    private final Authority_Assembler authorityAssembler = new Authority_Assembler();
	
	/**
	 * Create a new Authority_RestClient by RestTemplate and baseUri of the server.
     * @param restTemplate The restTemplate for the HTTP Requests.
     * @param baseUri The base URI of the REST Server.
     */
    public Authority_RestClientImpl(RestTemplate restTemplate, URI baseUri) {
        this.restTemplate = restTemplate;
        traverson = new Traverson(baseUri, MediaTypes.HAL_JSON);
        traverson.setRestOperations(restTemplate);
    }
	
	@Override
	public List<Authority_> findFullTextFuzzy(String filter) {
	return traverson.follow(AUTHORITYS, SEARCH, FIND_FULL_TEXT_FUZZY)
	.withTemplateParameters(Collections.singletonMap("q", filter))
	.toObject(Authority_Resource.LIST).getContent()
	.stream()
	.map(authorityAssembler::toBean)
	.collect(Collectors.toList());
	}
	
	@Override
	public List<Authority_> findAll() {
		return traverson
				.follow(AUTHORITYS)
				.toObject(Authority_Resource.LIST).getContent()
				
				.stream()
				.map(authorityAssembler::toBean)
				.collect(Collectors.toList());
	}
	@Override
	public List<Authority_> findAll(Link relation) {
		URI uri = URI.create(relation.getHref());
			 return restTemplate
			 		.exchange(uri, HttpMethod.GET, null, Authority_Resource.LIST)
			 		.getBody()
			 		.getContent()
			 		
			 		.stream()
			 		.map(authorityAssembler::toBean)
			 		.collect(Collectors.toList());
	}
		
	@Override
	public Optional<Authority_> findOne(Link link) {
	     URI uri = URI.create(link.getHref());
		Authority_Resource resource = restTemplate
				.exchange(uri, HttpMethod.GET, null, Authority_Resource.class)
				.getBody();
				
		return Optional.of(authorityAssembler.toBean(resource));
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
	public Authority_ create(Authority_ authority) {
		URI uri = URI.create(
		traverson.follow(AUTHORITYS).asLink().getHref());
			Authority_DTO authorityDTO = authorityAssembler.toResource(authority).getContent();
			Authority_Resource resource = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(authorityDTO), Authority_Resource.class).getBody();
			return authorityAssembler.toBean(resource);
	}
	
	@Override
	public Authority_ update(Authority_ authority) {

        URI uri = URI.create(authority.getId().getHref());

        Authority_DTO authorityDTO = authorityAssembler.toResource(authority).getContent();

        Authority_Resource resource = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(authorityDTO), Authority_Resource.class).getBody();

        return authorityAssembler.toBean(resource);
    }
		
	@Override
	public void delete(Link id) {
		URI uri = URI.create(id.getHref());
		restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
	}
}
