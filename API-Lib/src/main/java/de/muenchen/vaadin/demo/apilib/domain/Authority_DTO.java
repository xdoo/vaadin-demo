package de.muenchen.vaadin.demo.apilib.domain;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Authority_DTO extends BaseEntity {
	
		private String authority;
		
		// Getters and Setters
		public String getAuthority(){
			return authority;
		}
		
		public void setAuthority(String authority){
			this.authority = authority;
		}
		
		@Override
		public String toString() {
		   	return String.format("%s = {\"authority\": \"%s\"}", getClass(),
		   		this.authority);
		}
	}
