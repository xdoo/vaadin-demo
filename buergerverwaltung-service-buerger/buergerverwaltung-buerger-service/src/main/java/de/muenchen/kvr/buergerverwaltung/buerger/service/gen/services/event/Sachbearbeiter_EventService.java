package de.muenchen.kvr.buergerverwaltung.buerger.service.gen.services.event;


import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Sachbearbeiter_;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
/**
 * Provides methods to implement logic before and after Events.
 */
public interface Sachbearbeiter_EventService {
	void onAfterCreate(Sachbearbeiter_ entity);
	void onBeforeCreate(Sachbearbeiter_ entity);
	void onBeforeSave(Sachbearbeiter_ entity);
	void onAfterSave(Sachbearbeiter_ entity);
	void onBeforeLinkSave(Sachbearbeiter_ parent, Object linked);
	void onAfterLinkSave(Sachbearbeiter_ parent, Object linked);
	void onBeforeLinkDelete(Sachbearbeiter_ parent, Object linked);
	void onBeforeDelete(Sachbearbeiter_ entity);
	void onAfterDelete(Sachbearbeiter_ entity);
	void onAfterLinkDelete(Sachbearbeiter_ parent, Object linked);
}
