package de.muenchen.kvr.buergerverwaltung.buerger.service.gen.services.event;


import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Buerger_;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
/**
 * Provides methods to implement logic before and after Events.
 */
public interface Buerger_EventService {
	void onAfterCreate(Buerger_ entity);
	void onBeforeCreate(Buerger_ entity);
	void onBeforeSave(Buerger_ entity);
	void onAfterSave(Buerger_ entity);
	void onBeforeLinkSave(Buerger_ parent, Object linked);
	void onAfterLinkSave(Buerger_ parent, Object linked);
	void onBeforeLinkDelete(Buerger_ parent, Object linked);
	void onBeforeDelete(Buerger_ entity);
	void onAfterDelete(Buerger_ entity);
	void onAfterLinkDelete(Buerger_ parent, Object linked);
}