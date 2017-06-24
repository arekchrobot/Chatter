package pl.ark.chr.simplechat.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Arek on 2017-06-22.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -5161848974240597639L;

    protected Long id;

    public BaseEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
