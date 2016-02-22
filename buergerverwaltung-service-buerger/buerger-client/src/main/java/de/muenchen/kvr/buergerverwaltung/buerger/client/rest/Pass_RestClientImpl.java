package de.muenchen.kvr.buergerverwaltung.buerger.client.rest;

import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.Pass_DTO;
import de.muenchen.kvr.buergerverwaltung.buerger.client.hateoas.Pass_Assembler;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;
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
public class Pass_RestClientImpl implements Pass_RestClient {
	
	public static final String FIND_FULL_TEXT_FUZZY = "findFullTextFuzzy";
	
	public static final String SEARCH = "search";
	
	/** Used to follow HATEOAS relations. */
    private final Traverson traverson;
    
    /** The restTemplate used for the HTTP Requests. */
    private final RestTemplate restTemplate;
    
    /** Assembler to switch from Pass_DTO Resource to Pass_ and vice versa. */
    private final Pass_Assembler passAssembler = new Pass_Assembler();
	
	/**
	 * Create a new Pass_RestClient by RestTemplate of the server.
     * @param restTemplate The restTemplate for the HTTP Requests.
     * @param basePath The base URI of the REST Server.
     */
    public Pass_RestClientImpl(RestTemplate restTemplate, final URI basePath) {
        this.restTemplate = restTemplate;
        traverson = new Traverson(basePath, MediaTypes.HAL_JSON);
        traverson.setRestOperations(restTemplate);
    }
	
	@Override
	public List<Pass_> findFullTextFuzzy(String filter) {
		return traverson.follow(PASSS, SEARCH, FIND_FULL_TEXT_FUZZY)
				.withTemplateParameters(Collections.singletonMap("q", filter))
				.toObject(Pass_Resource.LIST).getContent()
				.stream()
				.map(passAssembler::toBean)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Pass_> findAll() {  
		return traverson
				.follow(PASSS)
				.toObject(Pass_Resource.LIST).getContent()
				
				.stream()
				.map(passAssembler::toBean)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Pass_> findAll(Link relation) {
		URI uri = URI.create(relation.getHref());
		return restTemplate
		 		.exchange(uri, HttpMethod.GET, null, Pass_Resource.LIST)
		 		.getBody()
		 		.getContent()
		 		
		 		.stream()
		 		.map(passAssembler::toBean)
		 		.collect(Collectors.toList());
	}
		
	@Override
	public Optional<Pass_> findOne(Link link) {
	     URI uri = URI.create(link.getHref());
		Pass_Resource resource = restTemplate
				.exchange(uri, HttpMethod.GET, null, Pass_Resource.class)
				.getBody();
				
		return Optional.of(passAssembler.toBean(resource));
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
	public Pass_ create(Pass_ pass) {
		URI uri = URI.create(
		traverson.follow(PASSS).asLink().getHref());
			Pass_DTO passDTO = passAssembler.toResource(pass).getContent();
			Pass_Resource resource = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(passDTO), Pass_Resource.class).getBody();
			return passAssembler.toBean(resource);
	}
	
	@Override
	public Pass_ update(Pass_ pass) {

        URI uri = URI.create(pass.getId().getHref());

        Pass_DTO passDTO = passAssembler.toResource(pass).getContent();

        Pass_Resource resource = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(passDTO), Pass_Resource.class).getBody();

        return passAssembler.toBean(resource);
    }
		
	@Override
	public void delete(Link id) {
		URI uri = URI.create(id.getHref());
		restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
	}
}
