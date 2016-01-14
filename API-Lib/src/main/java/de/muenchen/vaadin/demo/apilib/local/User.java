package de.muenchen.vaadin.demo.apilib.local;

/*
 * This file will NOT be overwritten by GAIA.
 * This file was automatically generated by GAIA.
 */

import org.hibernate.validator.constraints.Email;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.stream.Stream;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class User extends ResourceSupport {
	
	@NotNull
	@Pattern(regexp="[a-zA-Z0-9_\\.-]*")
	@Size(min=1)
	private String username;
	
	@NotNull
	@Pattern(regexp="\\p{L}*")
	@Size(min=1)
	private String forname;
	
	@NotNull
	@Pattern(regexp="\\p{L}*")
	@Size(min=1)
	private String surname;
	
	@NotNull
	private java.util.Date birthdate;
	
	@NotNull
	@Email
	private String email;

	@NotNull
	private boolean userEnabled;
	
	public User(){}
	
	/**
     * Create a new User_ with the  username, forname, surname, passwort, birthdate, email, userEnabled.
     *
     * @param username the username of the User_.
     * @param forname the forname of the User_.
     * @param surname the surname of the User_.
     * @param birthdate the birthdate of the User_.
     * @param email the email of the User_.
     * @param userEnabled the userEnabled of the User_.
     */
    public User(String username, String forname, String surname, java.util.Date birthdate, String email, boolean userEnabled) {
        this.setUsername(username);
        this.setForname(forname);
        this.setSurname(surname);
        this.setBirthdate(birthdate);
        this.setEmail(email);
        this.setUserEnabled(userEnabled);
    }
	
	// Getters and Setters
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getForname(){
		return forname;
	}
	
	public void setForname(String forname){
		this.forname = forname;
	}
	
	public String getSurname(){
		return surname;
	}
	
	public void setSurname(String surname){
		this.surname = surname;
	}
	
	public java.util.Date getBirthdate(){
		return birthdate;
	}
	
	public void setBirthdate(java.util.Date birthdate){
		this.birthdate = birthdate;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public boolean getUserEnabled(){
		return userEnabled;
	}
	
	public void setUserEnabled(boolean userEnabled){
		this.userEnabled = userEnabled;
	}
	
	 /**
      * A simple Enum for all the Fields of this User_.
      * <p>
      *     You can use {@link Field#name()} for the String.
      * </p>
      */
	public enum Field {
        username, forname, surname, birthdate, email, userEnabled(false);

        private final boolean field;

        Field() {
			this(true);
		}

		Field(boolean field) {
			this.field = field;
		}

		public boolean isField() {
			return field;
		}

		public static String[] getProperties() {
			return Stream.of(values()).filter(Field::isField).map(Field::name).toArray(String[]::new);
		}
	}
	
	 /**
	  * A simple Enum for all the Relations ({@link User#getLink(String)} of the User_.
	  * <p>
	  *     You can use {@link Rel#name()} for the String.
	  * </p>
	  */
	 public enum Rel {
		 authorities;
    }
	
	@Override
	public String toString(){
		String s = "";
		s += "String username: " + this.getUsername();
		s += "String forname: " + this.getForname();
		s += "String surname: " + this.getSurname();
		s += "java.util.Date birthdate: " + this.getBirthdate();
		s += "String email: " + this.getEmail();
		s += "boolean userEnabled: " + this.getUserEnabled();
		return s;
	}
}