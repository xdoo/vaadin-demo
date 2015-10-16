import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 *
 * @author claus.straube
 */
@MappedSuperclass
public abstract class BaseEntity implements Cloneable, Serializable {

    @Column(name = "OID")
    @Id
    @GeneratedValue
    private Long oid;

    @IndexedEmbedded(depth = 1, prefix = "mandant")
    @Column(length = 30, unique = true, nullable = true, name = "mandant")
    @JsonIgnore
    private String mandant;

    public String getMandant() {
        return mandant;
    }

    public void setMandant(String mandant) {
        this.mandant = mandant;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
