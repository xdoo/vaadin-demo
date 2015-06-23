/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catify.vaadin.demo.api.rest;

import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author claus.straube
 * @param <E>
 */
public class SearchResultResource<E> extends ResourceSupport {
    
    private List<E> result = Lists.newArrayList();
    
    public void add(E e) {
        this.result.add(e);
    }

    public List<E> getResult() {
        return result;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }
    
}
