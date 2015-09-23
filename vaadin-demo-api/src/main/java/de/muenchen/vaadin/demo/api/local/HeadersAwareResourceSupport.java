package de.muenchen.vaadin.demo.api.local;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

/**
 * Provides a simple ResourceSupport that also contains an Etag for safer or more efficient HTTP Operations.
 *
 * @author p.mueller
 * @version 1.0
 */
public class HeadersAwareResourceSupport extends ResourceSupport implements HTTPResource {

    /**
     * The possible dataEtag of this Resource.
     */
    private Optional<HttpHeaders> headers = Optional.empty();

    @Override
    public Optional<HttpHeaders> getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(HttpHeaders headers) {
        this.headers = Optional.ofNullable(headers);
    }
}
