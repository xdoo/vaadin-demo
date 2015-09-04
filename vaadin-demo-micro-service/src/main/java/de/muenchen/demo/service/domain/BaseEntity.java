package de.muenchen.demo.service.domain;

import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
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

	@Column(length = 30, unique = true, nullable = false, name = "OID")
	private String oid;

	@IndexedEmbedded(depth = 1, prefix = "mandant")

	@OneToOne
	private Mandant mandant;

	public Mandant getMandant() {
		return mandant;
	}

	public void setMandant(Mandant mandant) {
		this.mandant = mandant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOid() {
		return oid;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
