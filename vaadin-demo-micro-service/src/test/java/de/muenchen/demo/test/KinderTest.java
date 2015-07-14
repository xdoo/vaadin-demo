package de.muenchen.demo.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.config.InitApplication;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.api.BuergerResource;
import de.muenchen.demo.service.services.BuergerService;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class KinderTest {

    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    BuergerService service;
    private final Buerger bVater = new Buerger();
    private final Buerger son2 = new Buerger();
    private final Buerger son = new Buerger();

    @Autowired
    UserRepository usersRepo;
    @Autowired
    AuthorityRepository authRepo;
    @Autowired
    PermissionRepository permRepo;
    @Autowired
    UserAuthorityRepository userAuthRepo;
    @Autowired
    AuthorityPermissionRepository authPermRepo;
    @Autowired
    StaatsangehoerigkeitReferenceRepository staatRepo;
    @Autowired
    BuergerRepository buergerRepo;

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        buergerRepo.deleteAll();


        InitApplication initApplication = new InitApplication(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo);
        initApplication.init();
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));

        HttpClient httpClient = HttpClientBuilder.create()
                .setSSLSocketFactory(connectionFactory)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(requestFactory);

        bVater.setOid("20");
        bVater.setNachname("hans");
        bVater.setVorname("vater");

        son2.setOid("21");
        son2.setVorname("son");
        son2.setNachname("hans");

        son.setOid("22");
        son.setVorname("son2");
        son.setNachname("hans");
    }

    @Test
    public void BuergerKindTest() {

        String URL = "http://localhost:" + port + "/buerger/save";
        restTemplate.postForEntity(URL, bVater, BuergerResource.class);
        restTemplate.postForEntity(URL, son2, BuergerResource.class);

        String URL2 = "http://localhost:" + port + "/buerger/20";
        Buerger wo = restTemplate.getForEntity(URL2, Buerger.class).getBody();
        assertEquals("hans", wo.getNachname());


        /* Test methode createKindBuerger*/
        String URL3 = "http://localhost:" + port + "/buerger/create/kind/20";
        ResponseEntity<BuergerResource> response2 = restTemplate.postForEntity(URL3, son, BuergerResource.class);
        Buerger w = restTemplate.getForEntity(URL2, Buerger.class).getBody();
        assertEquals(false, w.getKinder().isEmpty());

        /* Test methode addKindBuerger*/
        String URL5 = "http://localhost:" + port + "/buerger/add/buerger/20/kind/21";
        BuergerResource response4 = restTemplate.getForEntity(URL5, BuergerResource.class).getBody();
        assertEquals(2, response4.getKinder().size());

    }

    @After
    public void TearDown() {
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        buergerRepo.deleteAll();


    }

}
