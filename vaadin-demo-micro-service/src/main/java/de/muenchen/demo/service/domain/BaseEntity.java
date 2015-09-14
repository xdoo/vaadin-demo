package de.muenchen.demo.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author claus.straube
 */
@Audited
@MappedSuperclass
public abstract class BaseEntity implements Cloneable, Serializable {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(unique = true, nullable = true, name = "OID")
	@Size(max = 30)
	private String oid;

	@IndexedEmbedded(depth = 1, prefix = "mandant")

	@NotAudited
	@Column(unique = true, nullable = true, name = "mandant")
	@Size(max = 30)
	@JsonIgnore
	private String mandant;

	public String getMandant() {
		return mandant;
	}

	public void setMandant(String mandant) {
		this.mandant = mandant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
