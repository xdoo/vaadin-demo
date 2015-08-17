package de.muenchen.demo.service.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.envers.NotAudited;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 *
 * @author claus.straube
 */
@MappedSuperclass
public abstract class BaseEntity implements Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(length = 30, unique = true, nullable = false, name = "OID")
	private String oid;

	@IndexedEmbedded(depth = 1, prefix = "mandant")
	@NotAudited
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
