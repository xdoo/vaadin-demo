///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package de.muenchen.demo.service.services;
//
//import de.muenchen.demo.service.domain.Adresse;
//import de.muenchen.demo.service.domain.AdresseExterne;
//import de.muenchen.demo.service.domain.AdresseExterneRepository;
//import de.muenchen.demo.service.domain.AdresseInterne;
//import de.muenchen.demo.service.domain.AdresseInterneRepository;
//import de.muenchen.demo.service.domain.AdresseReference;
//import de.muenchen.demo.service.domain.AdresseReferenceRepository;
//import de.muenchen.demo.service.util.IdService;
//import de.muenchen.demo.service.util.QueryService;
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.EntityManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.TestRestTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
///**
// *
// * @author praktikant.tmar
// */
//@Service
//public class AdresseServiceImpl implements AdresseService {
//
//    private static final Logger LOG = LoggerFactory.getLogger(AdresseService.class);
//
//    AdresseInterneRepository interneRepo;
//    AdresseExterneRepository externeRepo;
//    AdresseReferenceRepository referenceRepo;
//    QueryService<Adresse> search;
//    @Value("${URL}")
//    private String URL;
//    RestTemplate restTemplate = new TestRestTemplate();
//    private String plz;
//
//    public AdresseServiceImpl() {
//    }
//
//    @Autowired
//    public AdresseServiceImpl(AdresseInterneRepository interneRepo, AdresseExterneRepository externeRepo, AdresseReferenceRepository referenceRepo, EntityManager em) {
//        this.externeRepo = externeRepo;
//        this.interneRepo = interneRepo;
//        this.referenceRepo = referenceRepo;
//        this.search = new QueryService<>(em, Adresse.class, "stadt", "hausnummer", "strasse", "plz");
//    }
//
//    @Override
//    public List<Adresse> query() {
//        Iterable<AdresseExterne> allExterne = this.externeRepo.findAll();
//        Iterable<AdresseInterne> allInterne = this.interneRepo.findAll();
//        ArrayList<Adresse> list = new ArrayList();
//        for (AdresseExterne ad : allExterne) {
//            list.add(read(ad.getOid()));
//
//        }
//
//        for (AdresseInterne ad : allInterne) {
//            list.add(read(ad.getOid()));
//        }
//
//        return list;
//    }
//
//    @Override
//    public Adresse create() {
//        Adresse adresse = new Adresse();
//        adresse.setOid(IdService.next());
//        return adresse;
//    }
//
//    @Override
//    public Adresse save(Adresse adresse) {
//        LOG.info(adresse.toString());
//        plz = Integer.toString(adresse.getPlz());
//        if (adresse.getStadt().equals("München")) {
//            String URL2 = URL + "adresse/" + plz;
//            Adresse[] responseListe = this.restTemplate.getForEntity(URL2, Adresse[].class).getBody();
//            for (Adresse ad : responseListe) {
//                if (ad.getStrasse().equals(adresse.getStrasse()) && ad.getHausnummer().equals(adresse.getHausnummer())) {
//                    this.referenceRepo.save(toReferenceInterne(ad));
//                    return adresse;
//                }
//
//            }
//        } else {
//            this.referenceRepo.save(toReferenceExterne(adresse));
//            return adresse;
//        }
//        LOG.warn(String.format("found no adresse in münchen"));
//        return null;
//    }
//
//    @Override
//    public Adresse read(String oid) {
//        List<AdresseExterne> resultExterne = this.externeRepo.findByOid(oid);
//        if (resultExterne.isEmpty()) {
//            List<AdresseInterne> resultInterne = this.interneRepo.findByOid(oid);
//            if (resultInterne.isEmpty()) {
//                // TODO
//                LOG.warn(String.format("found no users with oid '%s'", oid));
//                return null;
//            } else {
//                return fromInterne(resultInterne.get(0));
//            }
//        } else {
//            return fromExterne(resultExterne.get(0));
//        }
//    }
//
//    @Override
//    public AdresseReference readReference(String oid) {
//        List<AdresseReference> result = this.referenceRepo.findByOid(oid);
//        if (result.isEmpty()) {
//            // TODO
//            LOG.warn(String.format("found no users with oid '%s'", oid));
//            return null;
//        } else {
//            return result.get(0);
//        }
//    }
//
//    @Override
//    public void delete(String oid) {
//        List<AdresseExterne> itemExterne = this.externeRepo.findByOid(oid);
//        List<AdresseReference> itemReference = this.referenceRepo.findByOid(oid);
//
//        if (!itemExterne.isEmpty()) {
//            this.referenceRepo.delete(itemReference);
//            this.externeRepo.delete(itemExterne);
//        }
//
//        List<AdresseInterne> itemInterne = this.interneRepo.findByOid(oid);
//
//        if (!itemInterne.isEmpty()) {
//            this.referenceRepo.delete(itemReference);
//            this.interneRepo.delete(itemInterne);
//
//        }
//    }
//
//    @Override
//    public Adresse copy(String oid) {
//        Adresse source = this.read(oid);
//        Adresse result = null;
//        Adresse clone = new Adresse();
//        clone.setOid(IdService.next());
//        // start mapping
//        clone.setHausnummer(source.getHausnummer());
//        clone.setPlz(source.getPlz());
//        clone.setStadt(source.getStadt());
//        clone.setStrasse(source.getStrasse());
//
//        // end mapping
//        LOG.info("clone --> " + clone.toString());
//        result = this.save(clone);
//        return result;
//    }
//
//    @Override
//    public Adresse update(Adresse adresse) {
//        return this.save(adresse);
//    }
//
//    public AdresseExterne toExterne(Adresse adresse) {
//        AdresseExterne adresseExterne = new AdresseExterne();
//        adresseExterne.setHausnummer(adresse.getHausnummer());
//        adresseExterne.setOid(adresse.getOid());
//        adresseExterne.setPlz(adresse.getPlz());
//        adresseExterne.setStadt(adresse.getStadt());
//        adresseExterne.setStrasse(adresse.getStrasse());
//        return adresseExterne;
//    }
//
//    public Adresse fromExterne(AdresseExterne adresseexterne) {
//        Adresse adresse = new Adresse();
//        adresse.setHausnummer(adresseexterne.getHausnummer());
//        adresse.setOid(adresseexterne.getOid());
//        adresse.setPlz(adresseexterne.getPlz());
//        adresse.setStadt(adresseexterne.getStadt());
//        adresse.setStrasse(adresseexterne.getStrasse());
//        return adresse;
//    }
//
//    public AdresseInterne toInterne(Adresse adresse) {
//        AdresseInterne adresseInterne = new AdresseInterne();
//        adresseInterne.setHausnummer(adresse.getHausnummer());
//        adresseInterne.setOid(adresse.getOid());
//        adresseInterne.setPlz(adresse.getPlz());
//        adresseInterne.setStadt(adresse.getStadt());
//        adresseInterne.setStrasse(adresse.getStrasse());
//        return adresseInterne;
//    }
//
//    public Adresse fromInterne(AdresseInterne adresseInterne) {
//        Adresse adresse = new Adresse();
//        adresse.setHausnummer(adresseInterne.getHausnummer());
//        adresse.setOid(adresseInterne.getOid());
//        adresse.setPlz(adresseInterne.getPlz());
//        adresse.setStadt(adresseInterne.getStadt());
//        adresse.setStrasse(adresseInterne.getStrasse());
//        return adresse;
//    }
//
//    public AdresseReference toReferenceInterne(Adresse adresse) {
//
//        AdresseReference adresseRefernece = new AdresseReference();
//        adresseRefernece.setAdresseInterne(toInterne(adresse));
//        adresseRefernece.setOid(adresse.getOid());
//
//        return adresseRefernece;
//    }
//
//    public AdresseReference toReferenceExterne(Adresse adresse) {
//
//        AdresseReference adresseReference = new AdresseReference();
//        adresseReference.setOid(adresse.getOid());
//        adresseReference.setAdresseExterne(toExterne(adresse));
//
//        return adresseReference;
//    }
//
//}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.springframework.http.ResponseEntity;
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
    UserService userService;
    @Autowired
    MandantService madantService;
    AdresseInterneRepository interneRepo;
    AdresseExterneRepository externeRepo;
    AdresseReferenceRepository referenceRepo;
    QueryService<Adresse> search;
    @Value("${URL}")
    private String URL;
    RestTemplate restTemplate = new TestRestTemplate();
    private String plz;
    
    public AdresseServiceImpl() {
    }
    
    @Autowired
    public AdresseServiceImpl(AdresseInterneRepository interneRepo, AdresseExterneRepository externeRepo, AdresseReferenceRepository referenceRepo, EntityManager em) {
        this.externeRepo = externeRepo;
        this.interneRepo = interneRepo;
        this.referenceRepo = referenceRepo;
        this.search = new QueryService<>(em, Adresse.class, "stadt", "hausnummer", "strasse", "plz");
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
            list.add(read(ad.getReferencedOid()));
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
                if (ad.getStrasse().equals(adresse.getStrasse()) && ad.getHausnummer().equals(adresse.getHausnummer())) {
                    Mandant mandant = madantService.read(readUser().getMandant().getOid());
                    ad.setMandant(mandant);
                    AdresseReference a = toReferenceInterne(ad);
                    this.referenceRepo.save(toReferenceInterne(ad));
                    return ad;
                }
                
            }
        } else {
            Mandant mandant = madantService.read(readUser().getMandant().getOid());
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
                List<AdresseInterne> resultInterne = this.interneRepo.findByReferencedOidAndMandantOid(oid, readUser().getMandant().getOid());
                String URL2 = URL + "adresse/" + resultInterne.get(0).getReferencedOid();
                ResponseEntity<Adresse> result2 = this.restTemplate.getForEntity(URL2, Adresse.class);
                return result2.getBody();
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
                this.referenceRepo.delete(resultReference);
                this.externeRepo.delete(resultExterne);
            } else {
                List<AdresseInterne> resultInterne = this.interneRepo.findByReferencedOid(oid);
                this.referenceRepo.delete(resultReference);
                this.interneRepo.delete(resultInterne);
            }
        }
    }
    
    @Override
    public Adresse copy(String oid) {
        Adresse source = this.read(oid);
        Adresse result = null;
        Adresse clone = new Adresse();
        clone.setOid(IdService.next());
        // start mapping
        clone.setHausnummer(source.getHausnummer());
        clone.setPlz(source.getPlz());
        clone.setStadt(source.getStadt());
        clone.setStrasse(source.getStrasse());

        // end mapping
        LOG.info("clone --> " + clone.toString());
        result = this.save(clone);
        return result;
    }
    
    @Override
    public Adresse update(Adresse adresse) {
        return this.save(adresse);
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
        
        adresseInterne.setReferencedOid(adresse.getOid());
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
