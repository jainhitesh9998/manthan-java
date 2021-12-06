package com.infy.facialrecognition.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * A Embeddings.
 */
@Entity
@Table(name = "embeddings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Embeddings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "embedding", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<Double> embedding;

    @ManyToOne
    @JsonIgnoreProperties(value = { "locations", "embeddings" }, allowSetters = true)
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Embeddings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Double> getEmbedding() {
        return this.embedding;
    }

    public Embeddings embedding(List<Double> embedding) {
        this.setEmbedding(embedding);
        return this;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Embeddings person(Person person) {
        this.setPerson(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Embeddings)) {
            return false;
        }
        return id != null && id.equals(((Embeddings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Embeddings{" +
            "id=" + getId() +
            ", embedding='" + getEmbedding() + "'" +
            "}";
    }
}
