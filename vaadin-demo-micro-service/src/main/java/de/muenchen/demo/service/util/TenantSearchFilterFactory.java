package de.muenchen.demo.service.util;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

public class TenantSearchFilterFactory {
	
    private String mandant_mid;

    @Factory
    public Filter getFilter() {
        Query query = new TermQuery( new Term( "mandant_mid", mandant_mid ) );
        return new CachingWrapperFilter( new QueryWrapperFilter(query) );
    }

	public String getMandant_mid() {
		return mandant_mid;
	}

	public void setMandant_mid(String mandant_mid) {
		this.mandant_mid = mandant_mid;
	}
    
}