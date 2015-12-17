package de.muenchen.vaadin.demo.apilib.rest;

import de.muenchen.vaadin.demo.apilib.domain.User_DTO;
import de.muenchen.vaadin.demo.apilib.hateoas.User_Assembler;
import de.muenchen.vaadin.demo.apilib.local.User_;
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
public class User_RestClientImpl implements User_RestClient {
	
	public static final String FIND_FULL_TEXT_FUZZY = "findFullTextFuzzy";
	public static final String SEARCH = "search";
	
	/**
     * Used to follow HATEOAS relations.
    */
    private final Traverson traverson;
    /** The restTemplate used for the HTTP Requests. */
    private final RestTemplate restTemplate;
    /** Assembler to switch from User_DTO Resource to User_ and vice versa. */
    private final User_Assembler userAssembler = new User_Assembler();
	
	/**
	 * Create a new User_RestClient by RestTemplate and baseUri of the server.
     * @param restTemplate The restTemplate for the HTTP Requests.
     * @param baseUri The base URI of the REST Server.
     */
    public User_RestClientImpl(RestTemplate restTemplate, URI baseUri) {
        this.restTemplate = restTemplate;
        traverson = new Traverson(baseUri, MediaTypes.HAL_JSON);
        traverson.setRestOperations(restTemplate);
    }
	
	@Override
	public List<User_> findFullTextFuzzy(String filter) {
	return traverson.follow(USERS, SEARCH, FIND_FULL_TEXT_FUZZY)
	.withTemplateParameters(Collections.singletonMap("q", filter))
	.toObject(User_Resource.LIST).getContent()
	.stream()
	.map(userAssembler::toBean)
	.collect(Collectors.toList());
	}
	
	@Override
	public List<User_> findAll() {  
		return traverson
				.follow(USERS)
				.toObject(User_Resource.LIST).getContent()
				
				.stream()
				.map(userAssembler::toBean)
				.collect(Collectors.toList());
	}
	@Override
	public List<User_> findAll(Link relation) {
		URI uri = URI.create(relation.getHref());
			 return restTemplate
			 		.exchange(uri, HttpMethod.GET, null, User_Resource.LIST)
			 		.getBody()
			 		.getContent()
			 		
			 		.stream()
			 		.map(userAssembler::toBean)
			 		.collect(Collectors.toList());
	}
		
	@Override
	public Optional<User_> findOne(Link link) {
	     URI uri = URI.create(link.getHref());
		User_Resource resource = restTemplate
				.exchange(uri, HttpMethod.GET, null, User_Resource.class)
				.getBody();
				
		return Optional.of(userAssembler.toBean(resource));
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
	public User_ create(User_ user) {
		URI uri = URI.create(
		traverson.follow(USERS).asLink().getHref());
			User_DTO userDTO = userAssembler.toResource(user).getContent();
			User_Resource resource = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(userDTO), User_Resource.class).getBody();
			return userAssembler.toBean(resource);
	}
	
	@Override
	public User_ update(User_ user) {

        URI uri = URI.create(user.getId().getHref());

        User_DTO userDTO = userAssembler.toResource(user).getContent();

        User_Resource resource = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(userDTO), User_Resource.class).getBody();

        return userAssembler.toBean(resource);
    }
		
	@Override
	public void delete(Link id) {
		URI uri = URI.create(id.getHref());
		restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
	}
}
