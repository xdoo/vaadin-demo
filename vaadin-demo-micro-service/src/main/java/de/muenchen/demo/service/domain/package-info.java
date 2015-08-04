/**
 * Configure full text search filters for domain classes.
 */
@FullTextFilterDef(name = "tenantSearchFilter", impl = TenantSearchFilterFactory.class)
package de.muenchen.demo.service.domain;

import org.hibernate.search.annotations.FullTextFilterDef;
import de.muenchen.demo.service.util.TenantSearchFilterFactory;
