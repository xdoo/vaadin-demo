package de.muenchen.kvr.buergerverwaltung.buerger.service.services.event;

import org.springframework.stereotype.Service;

import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Adresse_;
import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.services.event.Adresse_EventService;

/*
 * This file will NOT be overwritten by GAIA.
 * This file was automatically generated by GAIA.
 */
/**
 * Provides methods to implement logic before and after Events.
 * If used as generated by GAIA this service will be autowired and called by Adresse_EventListener.
 */
@Service
public class Adresse_EventServiceImpl implements Adresse_EventService{
	// If you need access to the database you can autowire a Repository.
	// Repositories are generated into the package: .gen.rest
	//
	// @Autowired
	// <EntityName>Repository repo;
	
	@Override
	public void onAfterCreate(Adresse_ entity) {
		// Add your logic here.
	}
	@Override
	public void onBeforeCreate(Adresse_ entity) {
		// Add your logic here.
	}
	@Override
	public void onBeforeSave(Adresse_ entity) {
		// Add your logic here.
	}
	@Override
	public void onAfterSave(Adresse_ entity) {
		// Add your logic here.
	}
	@Override
	public void onBeforeLinkSave(Adresse_ parent, Object linked) {
		// Add your logic here.
	}
	@Override
	public void onAfterLinkSave(Adresse_ parent, Object linked) {
		// Add your logic here.
	}
	@Override
	public void onBeforeLinkDelete(Adresse_ parent, Object linked) {
		// Add your logic here.
	}
	@Override
	public void onBeforeDelete(Adresse_ entity) {
		// Add your logic here.
	}
	@Override
	public void onAfterDelete(Adresse_ entity) {
		// Add your logic here.
	}
	@Override
	public void onAfterLinkDelete(Adresse_ parent, Object linked) {
		// Add your logic here.
	}
}
