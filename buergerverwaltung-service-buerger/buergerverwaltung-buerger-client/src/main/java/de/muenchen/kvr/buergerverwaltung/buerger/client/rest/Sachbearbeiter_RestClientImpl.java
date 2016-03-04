package de.muenchen.kvr.buergerverwaltung.buerger.client.rest;

import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.Sachbearbeiter_DTO;
import de.muenchen.kvr.buergerverwaltung.buerger.client.hateoas.Sachbearbeiter_Assembler;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
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
public class Sachbearbeiter_RestClientImpl implements Sachbearbeiter_RestClient {
	
	public static final String FIND_FULL_TEXT_FUZZY = "findFullTextFuzzy";
	
	public static final String SEARCH = "search";
	
	/** Used to follow HATEOAS relations. */
    private final Traverson traverson;
    
    /** The restTemplate used for the HTTP Requests. */
    private final RestTemplate restTemplate;
    
    /** Assembler to switch from Sachbearbeiter_DTO Resource to Sachbearbeiter_ and vice versa. */
    private final Sachbearbeiter_Assembler sachbearbeiterAssembler = new Sachbearbeiter_Assembler();
	
	/**
	 * Create a new Sachbearbeiter_RestClient by RestTemplate of the server.
     * @param restTemplate The restTemplate for the HTTP Requests.
     * @param basePath The base URI of the REST Server.
     */
    public Sachbearbeiter_RestClientImpl(RestTemplate restTemplate, final URI basePath) {
        this.restTemplate = restTemplate;
        traverson = new Traverson(basePath, MediaTypes.HAL_JSON);
        traverson.setRestOperations(restTemplate);
    }
	
	@Override
	public List<Sachbearbeiter_> findFullTextFuzzy(String filter) {
		return traverson.follow(SACHBEARBEITERS, SEARCH, FIND_FULL_TEXT_FUZZY)
				.withTemplateParameters(Collections.singletonMap("q", filter))
				.toObject(Sachbearbeiter_Resource.LIST).getContent()
				.stream()
				.map(sachbearbeiterAssembler::toBean)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Sachbearbeiter_> findAll() {  
		return traverson
				.follow(SACHBEARBEITERS)
				.toObject(Sachbearbeiter_Resource.LIST).getContent()
				
				.stream()
				.map(sachbearbeiterAssembler::toBean)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Sachbearbeiter_> findAll(Link relation) {
		URI uri = URI.create(relation.getHref());
		return restTemplate
		 		.exchange(uri, HttpMethod.GET, null, Sachbearbeiter_Resource.LIST)
		 		.getBody()
		 		.getContent()
		 		
		 		.stream()
		 		.map(sachbearbeiterAssembler::toBean)
		 		.collect(Collectors.toList());
	}
		
	@Override
	public Optional<Sachbearbeiter_> findOne(Link link) {
	     URI uri = URI.create(link.getHref());
	     
		Sachbearbeiter_Resource resource;
		
		try {
			resource = restTemplate.exchange(uri, HttpMethod.GET, null, Sachbearbeiter_Resource.class).getBody();
		} catch (HttpClientErrorException e) {
			final HttpStatus statusCode = e.getStatusCode();
			if (!HttpStatus.NOT_FOUND.equals(statusCode))
				throw e;
			if (Link.REL_SELF.equals(link.getRel()))
				throw e;
			return Optional.empty();
		}
		
		return Optional.of(sachbearbeiterAssembler.toBean(resource));
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
    	Optional<Link> rel = Optional.ofNullable(relation);
		if (rel.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("text", "uri-list"));
			restTemplate.exchange(URI.create(endpoint.getHref()), HttpMethod.PUT, new HttpEntity<>(rel.map(Link::getHref).get(), headers), Void.class);
		} else {
			this.delete(endpoint);
		}
    }

		
	@Override
	public Sachbearbeiter_ create(Sachbearbeiter_ sachbearbeiter) {
		URI uri = URI.create(
		traverson.follow(SACHBEARBEITERS).asLink().getHref());
			Sachbearbeiter_DTO sachbearbeiterDTO = sachbearbeiterAssembler.toResource(sachbearbeiter).getContent();
			Sachbearbeiter_Resource resource = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(sachbearbeiterDTO), Sachbearbeiter_Resource.class).getBody();
			return sachbearbeiterAssembler.toBean(resource);
	}
	
	@Override
	public Sachbearbeiter_ update(Sachbearbeiter_ sachbearbeiter) {

        URI uri = URI.create(sachbearbeiter.getId().getHref());

        Sachbearbeiter_DTO sachbearbeiterDTO = sachbearbeiterAssembler.toResource(sachbearbeiter).getContent();

        Sachbearbeiter_Resource resource = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(sachbearbeiterDTO), Sachbearbeiter_Resource.class).getBody();

        return sachbearbeiterAssembler.toBean(resource);
    }
		
	@Override
	public void delete(Link id) {
		URI uri = URI.create(id.getHref());
		restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
	}
}