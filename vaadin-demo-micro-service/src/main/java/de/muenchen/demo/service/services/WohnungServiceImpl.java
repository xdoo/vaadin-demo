///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package de.muenchen.demo.service.services;
//
//import com.google.common.base.Preconditions;
//import com.google.common.collect.Lists;
//import de.muenchen.demo.service.domain.Mandant;
//import de.muenchen.demo.service.domain.User;
//import de.muenchen.demo.service.domain.Wohnung;
//import de.muenchen.demo.service.domain.WohnungRepository;
//import de.muenchen.demo.service.util.IdService;
//import de.muenchen.demo.service.util.QueryService;
//import java.util.List;
//import java.util.Objects;
//import javax.persistence.EntityManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
///**
// *
// * @author praktikant.tmar
// */
//@Service
//public class WohnungServiceImpl implements WohnungService {
//
//    @Autowired
//    private BuergerService buergerService;
//    private static final Logger LOG = LoggerFactory.getLogger(WohnungService.class);
//    WohnungRepository repo;
//    QueryService<Wohnung> search;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    MandantService mandantService;
//
//    public WohnungServiceImpl() {
//    }
//
//    @Autowired
//    public WohnungServiceImpl(WohnungRepository repo, UserService userService, EntityManager em) {
//        this.repo = repo;
//        this.search = new QueryService<>(userService, em, Wohnung.class, "adresseOid", "ausrichtung", "stock");
//    }
//
//    @Override
//    public void delete(String oid) {
//        Wohnung item = this.read(oid);
//        this.buergerService.releaseWohnungAllBuerger(oid);
//
//        this.repo.delete(item);
//
//    }
//
//    @Override
//    public Wohnung create() {
//        Wohnung wohnung = new Wohnung();
//        wohnung.setOid(IdService.next());
//        return wohnung;
//    }
//
//    @Override
//    public Wohnung save(Wohnung wohnung) {
//        LOG.info(wohnung.toString());
//        Preconditions.checkArgument(wohnung.getId() == null, "On save, the ID must be empty");
//        Mandant mandant = mandantService.read(readUser().getMandant().getOid());
//        wohnung.setMandant(mandant);
//        return this.repo.save(wohnung);
//    }
//
//    @Override
//    public Wohnung read(String oid) {
//        Wohnung result = this.repo.findFirstByOidAndMandantOid(oid, readUser().getMandant().getOid());
//
//        if (Objects.isNull(result)) {
//            LOG.warn(String.format("found no pass with oid '%s'", oid));
//            return null;
//        } else {
//            return result;
//        }
//    }
//
//    public User readUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String name = authentication.getName();
//        return userService.readByUsername(name);
//    }
//
//    @Override
//    public Wohnung update(Wohnung wohnung) {
//        return this.repo.save(wohnung);
//    }
//
//    @Override
//    public List<Wohnung> query() {
//        Iterable<Wohnung> all = this.repo.findByMandantOid(readUser().getMandant().getOid());
//        return Lists.newArrayList(all);
//    }
//
//    @Override
//    public List<Wohnung> query(String query) {
//        return this.search.query(query);
//    }
//
//    @Override
//    public Wohnung copy(String oid) {
//        Wohnung in = this.read(oid);
//        // map
//        Wohnung out = new Wohnung(in);
//        out.setOid(IdService.next());
//
//        // in DB speichern
//        this.save(out);
//        return out;
//    }
//
//    @Override
//    public Wohnung readAdresse(String oid) {
//        return repo.findByAdresseOidAndMandantOid(oid, readUser().getMandant().getOid());
//    }
//
//    @Override
//    public void deleteWohnungAdresse(String adresseOid) {
//        Wohnung wohnung = this.readAdresse(adresseOid);
//        if (wohnung != null) {
//            wohnung.setAdresse(null);
//            this.update(wohnung);
//        }
//    }
//}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.AdresseExterne;
import de.muenchen.demo.service.domain.AdresseExterneRepository;
import de.muenchen.demo.service.domain.AdresseInterne;
import de.muenchen.demo.service.domain.AdresseInterneRepository;
import de.muenchen.demo.service.domain.AdresseReference;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.domain.WohnungRepository;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.List;
import java.util.Objects;
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
public class WohnungServiceImpl implements WohnungService {

    @Autowired
    private BuergerService buergerService;
    private static final Logger LOG = LoggerFactory.getLogger(WohnungService.class);
    WohnungRepository repo;
    AdresseInterneRepository interneRepo;
    AdresseExterneRepository externeRepo;
    AdresseReferenceRepository referenceRepo;
    QueryService<Wohnung> search;
    @Value("${URL}")
    private String URL;
    RestTemplate restTemplate = new TestRestTemplate();
    private String plz;
    @Autowired
    private UserService userService;
    @Autowired
    MandantService mandantService;

    public WohnungServiceImpl() {
    }

    @Autowired
    public WohnungServiceImpl(WohnungRepository repo, AdresseInterneRepository interneRepo, AdresseExterneRepository externeRepo, AdresseReferenceRepository referenceRepo, UserService userService, EntityManager em) {
        this.repo = repo;
        this.externeRepo = externeRepo;
        this.interneRepo = interneRepo;
        this.referenceRepo = referenceRepo;
        this.search = new QueryService<>(userService, em, Wohnung.class, "adresseOid", "ausrichtung", "stock");
    }

    @Override
    public void delete(String wOid) {
        Wohnung item = this.read(wOid);
        this.buergerService.releaseWohnungAllBuerger(wOid);
        this.repo.delete(item);
        String aOid = item.getAdresse().getOid();

        List<AdresseReference> resultReference = this.referenceRepo.findByOidAndMandantOid(aOid, readUser().getMandant().getOid());
        if (resultReference.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no adresse with oid '%s'", aOid));

        } else {
            if (resultReference.get(0).getAdresseInterne() == null) {
                List<AdresseExterne> resultExterne = this.externeRepo.findByOidAndMandantOid(aOid, readUser().getMandant().getOid());
                this.referenceRepo.delete(resultReference);
                this.externeRepo.delete(resultExterne);
            } else {
                List<AdresseInterne> resultInterne = this.interneRepo.findByOidAndMandantOid(aOid, readUser().getMandant().getOid());
                this.referenceRepo.delete(resultReference);
                this.interneRepo.delete(resultInterne);
            }
        }

    }

    @Override
    public Wohnung create() {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid(IdService.next());
        return wohnung;
    }

    @Override
    public Wohnung save(Wohnung wohnung, Adresse adresse) {
        LOG.info(wohnung.toString());
        Preconditions.checkArgument(wohnung.getId() == null, "On save, the ID must be empty");
        Mandant mandant = mandantService.read(readUser().getMandant().getOid());
        wohnung.setMandant(mandant);
        adresse.setOid(IdService.next());

        if (Objects.isNull(adresse.getStrasseReference())) {

            adresse.setMandant(mandant);
            wohnung.setAdresse(toReferenceExterne(adresse));
        } else {
            adresse.setMandant(mandant);
            wohnung.setAdresse(toReferenceInterne(adresse));
        }
//        plz = Integer.toString(adresse.getPlz());
//        if (adresse.getStadt().equals("München")) {
//            String URL2 = URL + "adresse/" + plz;
//            Adresse[] responseListe = this.restTemplate.getForEntity(URL2, Adresse[].class).getBody();
//            for (Adresse ad : responseListe) {
//                if (ad.getStrasse().equals(adresse.getStrasse())) {
//                    ad.setMandant(mandant);
//                    ad.setHausnummer(adresse.getHausnummer());
//                    ad.setOid(adresse.getOid());
//                    AdresseReference a = toReferenceInterne(ad);
//                    wohnung.setAdresse(a);
//
//                }
//
//            }
//        } else {
//            adresse.setMandant(mandant);
//            wohnung.setAdresse(toReferenceExterne(adresse));
//        }

        return this.repo.save(wohnung);

    }

    @Override
    public Wohnung read(String oid) {
        Wohnung result = this.repo.findFirstByOidAndMandantOid(oid, readUser().getMandant().getOid());

        if (Objects.isNull(result)) {
            LOG.warn(String.format("found no pass with oid '%s'", oid));
            return null;
        } else {
            return result;
        }
    }

    @Override
    public Adresse readAdresse(String wOid) {
        String oid = this.read(wOid).getAdresse().getOid();
        List<AdresseReference> resultReference = this.referenceRepo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (resultReference.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no Adresse with oid '%s'", oid));
            return null;

        } else {
            if (resultReference.get(0).getAdresseInterne() == null) {
                List<AdresseExterne> resultExterne = this.externeRepo.findByOidAndMandantOid(resultReference.get(0).getAdresseExterne().getOid(), readUser().getMandant().getOid());
                return fromExterne(resultExterne.get(0));

            } else {
                List<AdresseInterne> resultInterne = this.interneRepo.findByOidAndMandantOid(resultReference.get(0).getAdresseInterne().getOid(), readUser().getMandant().getOid());
                String URL2 = URL + "adresse/" + resultInterne.get(0).getStrasseReference();
                Adresse[] result2 = this.restTemplate.getForEntity(URL2, Adresse[].class).getBody();
                for (Adresse result21 : result2) {
                    if (resultInterne.get(0).getHausnummer().equals(result21.getHausnummer())){
                    result21.setOid(resultInterne.get(0).getOid());
                    return result21;
                    }
                }

            }
        }
        return null;
    }
    

    public User readUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.readByUsername(name);
    }

    @Override
    public Wohnung update(Wohnung wohnung, Adresse adresse) {
        this.delete(wohnung.getOid());
        adresse.setMandant(wohnung.getMandant());
        if (Objects.isNull(adresse.getStrasseReference())) {
            wohnung.setAdresse(toReferenceExterne(adresse));
        } else {

            wohnung.setAdresse(toReferenceInterne(adresse));
        }
        return this.repo.save(wohnung);
    }

    @Override
    public List<Wohnung> query() {
        Iterable<Wohnung> all = this.repo.findByMandantOid(readUser().getMandant().getOid());
        return Lists.newArrayList(all);
    }

    @Override
    public List<Wohnung> query(String query) {
        return this.search.query(query);
    }

    @Override
    public Wohnung copy(String oid) {
        Wohnung in = this.read(oid);
        // map
        Wohnung out = new Wohnung(in);
        Adresse adresse = this.readAdresse(oid);
        adresse.setOid(IdService.next());
        out.setOid(IdService.next());

        // in DB speichern
        this.save(out, adresse);
        return out;
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
}
