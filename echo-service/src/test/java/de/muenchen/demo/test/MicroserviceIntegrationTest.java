package de.muenchen.demo.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Test driver for microservice integration test which uses JWT.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest()
@TestPropertySource(properties = {
        "server.port=0",
        "security.oauth2.resource.jwt.keyValue=test"
})
public class MicroserviceIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private RestTemplate template = new TestRestTemplate();

    private MacSigner signer = new MacSigner("test");
    private ObjectMapper objectMapper = new ObjectMapper();


    public String tokenValueForToken(JwtToken token) throws JsonProcessingException {
        return JwtHelper.encode(objectMapper.writeValueAsString(token), signer).getEncoded();
    }

    public ResponseEntity<String> postRequest(String resource, String requestBody, JwtToken token) throws JsonProcessingException {
        final URI uri = URI.create("http://localhost:" + port + resource);
        final MultiValueMap<String, String> headers = new HttpHeaders();
        if (token != null) {
            final String tokenValue = tokenValueForToken(token);
            headers.add("Authorization", "Bearer " + tokenValue);
        }
        RequestEntity<String> request = new RequestEntity<>(requestBody, headers, HttpMethod.POST, uri);
        return template.exchange(request, String.class);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public RestTemplate getTemplate() {
        return template;
    }
}
