package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.AdresseExterne;
import de.muenchen.demo.service.domain.AdresseExterneRepository;
import de.muenchen.demo.service.domain.AdresseInterne;
import de.muenchen.demo.service.domain.AdresseInterneRepository;
import de.muenchen.demo.service.domain.AdresseReference;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class AdresseServiceImpl implements AdresseService {

    private static final Logger LOG = LoggerFactory.getLogger(AdresseService.class);

    @Autowired
    MandantService mandantService;
    UserService userService;
    AdresseInterneRepository interneRepo;
    AdresseExterneRepository externeRepo;
    AdresseReferenceRepository referenceRepo;
    QueryService<Adresse> search;
    @Value("${URL}")
    private String URL;
    RestTemplate restTemplate = new TestRestTemplate();
    private String plz;
    @Autowired
    private WohnungService wohnungService;

    public AdresseServiceImpl() {
    }

    @Autowired
    public AdresseServiceImpl(AdresseInterneRepository interneRepo, AdresseExterneRepository externeRepo, AdresseReferenceRepository referenceRepo, UserService userService, EntityManager em) {
        this.externeRepo = externeRepo;
        this.interneRepo = interneRepo;
        this.referenceRepo = referenceRepo;
        this.userService = userService;
        this.search = new QueryService<>(userService, em, Adresse.class, "stadt", "hausnummer", "strasse", "plz");
    }

    @Override
    public List<Adresse> query() {
        Iterable<AdresseExterne> allExterne = this.externeRepo.findByMandantOid(readUser().getMandant().getOid());
        Iterable<AdresseInterne> allInterne = this.interneRepo.findByMandantOid(readUser().getMandant().getOid());
        ArrayList<Adresse> list = new ArrayList();
        for (AdresseExterne ad : allExterne) {
            list.add(read(ad.getOid()));

        }

        for (AdresseInterne ad : allInterne) {
            list.add(read(ad.getOid()));
        }

        return list;
    }

    @Override
    public Adresse create() {
        Adresse adresse = new Adresse();
        adresse.setOid(IdService.next());
        return adresse;
    }

    @Override
    public Adresse save(Adresse adresse) {
        LOG.info(adresse.toString());
        plz = Integer.toString(adresse.getPlz());
        if (adresse.getStadt().equals("München")) {
            String URL2 = URL + "adresse/" + plz;
            Adresse[] responseListe = this.restTemplate.getForEntity(URL2, Adresse[].class).getBody();
            for (Adresse ad : responseListe) {
                if (ad.getStrasse().equals(adresse.getStrasse())) {
                    Mandant mandant = mandantService.read(readUser().getMandant().getOid());
                    ad.setMandant(mandant);
                    ad.setHausnummer(adresse.getHausnummer());
                    ad.setOid(adresse.getOid());
                    AdresseReference a = toReferenceInterne(ad);
                    this.referenceRepo.save(a);
                    return ad;
                }

            }
        } else {
            Mandant mandant = mandantService.read(readUser().getMandant().getOid());
            adresse.setMandant(mandant);
            this.referenceRepo.save(toReferenceExterne(adresse));
            return adresse;
        }
        LOG.warn(String.format("found no adresse in münchen"));
        return null;
    }

    @Override
    public Adresse read(String oid) {
        List<AdresseReference> resultReference = this.referenceRepo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (resultReference.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", oid));
            return null;

        } else {
            if (resultReference.get(0).getAdresseInterne() == null) {
                List<AdresseExterne> resultExterne = this.externeRepo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
                return fromExterne(resultExterne.get(0));

            } else {
                List<AdresseInterne> resultInterne = this.interneRepo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
                String URL2 = URL + "adresse/" + resultInterne.get(0).getStrasseReference();
                Adresse result2 = this.restTemplate.getForEntity(URL2, Adresse.class).getBody();
                result2.setHausnummer(resultInterne.get(0).getHausnummer());
                result2.setOid(oid);
                return result2;
            }
        }
    }

    @Override
    public AdresseReference readReference(String oid) {
        List<AdresseReference> result = this.referenceRepo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void delete(String oid) {

        List<AdresseReference> resultReference = this.referenceRepo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (resultReference.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", oid));

        } else {
            if (resultReference.get(0).getAdresseInterne() == null) {
                List<AdresseExterne> resultExterne = this.externeRepo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
                this.wohnungService.deleteWohnungAdresse(oid);
                this.referenceRepo.delete(resultReference);
                this.externeRepo.delete(resultExterne);
            } else {
                List<AdresseInterne> resultInterne = this.interneRepo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
                this.wohnungService.deleteWohnungAdresse(oid);
                this.referenceRepo.delete(resultReference);
                this.interneRepo.delete(resultInterne);
            }
        }
    }

    @Override
    public Adresse copy(String oid) {

        Adresse in = this.read(oid);

        // map
        Adresse out = new Adresse(in);
        out.setOid(IdService.next());

        // in DB speichern
        this.save(out);

        return out;

    }

    @Override
    public Adresse update(Adresse adresse) {
        LOG.info(adresse.toString());
        plz = Integer.toString(adresse.getPlz());
        this.delete(adresse.getOid());
        return (this.save(adresse));

    }

    public AdresseExterne toExterne(Adresse adresse) {
        AdresseExterne adresseExterne = new AdresseExterne();
        adresseExterne.setHausnummer(adresse.getHausnummer());
        adresseExterne.setOid(adresse.getOid());
        adresseExterne.setMandant(adresse.getMandant());
        adresseExterne.setPlz(adresse.getPlz());
        adresseExterne.setStadt(adresse.getStadt());
        adresseExterne.setStrasse(adresse.getStrasse());
        return adresseExterne;
    }

    public Adresse fromExterne(AdresseExterne adresseExterne) {
        Adresse adresse = new Adresse();
        adresse.setHausnummer(adresseExterne.getHausnummer());
        adresse.setOid(adresseExterne.getOid());
        adresse.setPlz(adresseExterne.getPlz());
        adresse.setStadt(adresseExterne.getStadt());
        adresse.setMandant(adresseExterne.getMandant());
        adresse.setStrasse(adresseExterne.getStrasse());
        return adresse;
    }

    public AdresseInterne toInterne(Adresse adresse) {
        AdresseInterne adresseInterne = new AdresseInterne();

        adresseInterne.setOid(adresse.getOid());
        adresseInterne.setStrasseReference(adresse.getStrasseReference());
        adresseInterne.setHausnummer(adresse.getHausnummer());
        adresseInterne.setMandant(adresse.getMandant());
        return adresseInterne;
    }

    public AdresseReference toReferenceInterne(Adresse adresse) {

        AdresseReference adresseRefernece = new AdresseReference();
        adresseRefernece.setAdresseInterne(toInterne(adresse));
        adresseRefernece.setOid(adresse.getOid());
        adresseRefernece.setMandant(adresse.getMandant());

        return adresseRefernece;
    }

    public AdresseReference toReferenceExterne(Adresse adresse) {

        AdresseReference adresseReference = new AdresseReference();
        adresseReference.setOid(adresse.getOid());
        adresseReference.setAdresseExterne(toExterne(adresse));
        adresseReference.setMandant(adresse.getMandant());

        return adresseReference;
    }

    public User readUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.readByUsername(name);
    }
}
