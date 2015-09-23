package de.muenchen.vaadin.demo.api.local;

import org.springframework.http.HttpHeaders;

import java.util.Optional;

/**
 * Provides a simple interface for an Object that can, but must not contain some HttpHeaders.
 */
public interface HTTPResource {
    Optional<HttpHeaders> getHeaders();

    void setHeaders(HttpHeaders headers);
}
