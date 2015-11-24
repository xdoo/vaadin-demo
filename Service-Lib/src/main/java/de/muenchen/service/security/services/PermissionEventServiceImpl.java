package de.muenchen.service.security.services;


import de.muenchen.service.security.entities.Permission;
import org.springframework.stereotype.Service;

/*
 * This file will NOT be overwritten by GAIA.
 * This file was automatically generated by GAIA.
 */
/**
 * Provides methods to implement logic before and after Events.
 * If used as generated by GAIA this service will be autowired and called by PermissionEventListener.
 */
@Service
public class PermissionEventServiceImpl implements PermissionEventService{
    // If you need access to the database you can autowire a Repository.
    // Repositories are generated into the package: .gen.rest
    //
    // @Autowired
    // <EntityName>Repository repo;

    @Override
    public void onAfterCreate(Permission entity) {
        // Add your logic here.
    }
    @Override
    public void onBeforeCreate(Permission entity) {
        // Add your logic here.
    }
    @Override
    public void onBeforeSave(Permission entity) {
        // Add your logic here.
    }
    @Override
    public void onAfterSave(Permission entity) {
        // Add your logic here.
    }
    @Override
    public void onBeforeLinkSave(Permission parent, Object linked) {
        // Add your logic here.
    }
    @Override
    public void onAfterLinkSave(Permission parent, Object linked) {
        // Add your logic here.
    }
    @Override
    public void onBeforeLinkDelete(Permission parent, Object linked) {
        // Add your logic here.
    }
    @Override
    public void onBeforeDelete(Permission entity) {
        // Add your logic here.
    }
    @Override
    public void onAfterDelete(Permission entity) {
        // Add your logic here.
    }
    @Override
    public void onAfterLinkDelete(Permission parent, Object linked) {
        // Add your logic here.
    }
}