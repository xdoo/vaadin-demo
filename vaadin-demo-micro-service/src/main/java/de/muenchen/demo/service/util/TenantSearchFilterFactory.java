package de.muenchen.demo.service.util;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

public class TenantSearchFilterFactory {
	
    private String mandantoid;

    @Factory
    public Filter getFilter() {
        Query query = new TermQuery( new Term( "mandantoid", mandantoid ) );
        return new CachingWrapperFilter( new QueryWrapperFilter(query) );
    }

	public String getMandantoid() {
		return mandantoid;
	}

	public void setMandantoid(String mandantoid) {
		this.mandantoid = mandantoid;
	}
    
}