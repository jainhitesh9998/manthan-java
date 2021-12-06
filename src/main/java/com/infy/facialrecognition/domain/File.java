package com.infy.facialrecognition.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "source")
    private String source;

    @Column(name = "encoding")
    private String encoding;

    @Column(name = "identifier")
    private Long identifier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public File id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return this.location;
    }

    public File location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSource() {
        return this.source;
    }

    public File source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public File encoding(String encoding) {
        this.setEncoding(encoding);
        return this;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Long getIdentifier() {
        return this.identifier;
    }

    public File identifier(Long identifier) {
        this.setIdentifier(identifier);
        return this;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return id != null && id.equals(((File) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "File{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", source='" + getSource() + "'" +
            ", encoding='" + getEncoding() + "'" +
            ", identifier=" + getIdentifier() +
            "}";
    }
}
