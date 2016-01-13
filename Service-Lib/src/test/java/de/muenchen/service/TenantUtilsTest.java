package de.muenchen.service;

import de.muenchen.service.security.UserInfo;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for class TenantUtils.
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

    @Test
    public void testExtractTenantFromPrincipal() {
        String expected = "m01";
        UserInfo principal = new UserInfo("Bnutzer", "N/A", expected, new HashSet<>());
        String tenant = TenantUtils.extractTenantFromPrincipal(principal);
        assertEquals(expected, tenant);
    }

    @Test
    public void testExtractUsernameFromName() {
        String username = TenantUtils.extractUsernameFromName("m01_hans");
        assertEquals("hans", username);

        username = TenantUtils.extractUsernameFromName("m01_hans_02");
        assertEquals("hans_02", username);
    }
}