package de.muenchen.service;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by huningd on 16.12.15.
 */
public class TenantUtilsTest {

    @Test
    public void testExtractTenantFromUsernameWithPrefix() throws Exception {
        final String tenant = TenantUtils.extractTenantFromUsername("m01_hans");
        assertEquals("m01", tenant);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractTenantFromUsernameWithoutPrefix() throws Exception {
        TenantUtils.extractTenantFromUsername("hans");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractTenantFromUsernameWithoutCorrectPrefix() throws Exception {
        TenantUtils.extractTenantFromUsername("m001_hans");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractTenantFromUsernameWithSuffix() throws Exception {
        TenantUtils.extractTenantFromUsername("hans_m01_");
    }
}