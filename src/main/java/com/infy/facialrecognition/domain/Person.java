package com.infy.facialrecognition.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Long age;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person" }, allowSetters = true)
    private Set<Embeddings> embeddings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Person name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return this.age;
    }

    public Person age(Long age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getEmail() {
        return this.email;
    }

    public Person email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Person phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        if (this.locations != null) {
            this.locations.forEach(i -> i.setPerson(null));
        }
        if (locations != null) {
            locations.forEach(i -> i.setPerson(this));
        }
        this.locations = locations;
    }

    public Person locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Person addLocation(Location location) {
        this.locations.add(location);
        location.setPerson(this);
        return this;
    }

    public Person removeLocation(Location location) {
        this.locations.remove(location);
        location.setPerson(null);
        return this;
    }

    public Set<Embeddings> getEmbeddings() {
        return this.embeddings;
    }

    public void setEmbeddings(Set<Embeddings> embeddings) {
        if (this.embeddings != null) {
            this.embeddings.forEach(i -> i.setPerson(null));
        }
        if (embeddings != null) {
            embeddings.forEach(i -> i.setPerson(this));
        }
        this.embeddings = embeddings;
    }

    public Person embeddings(Set<Embeddings> embeddings) {
        this.setEmbeddings(embeddings);
        return this;
    }

    public Person addEmbeddings(Embeddings embeddings) {
        this.embeddings.add(embeddings);
        embeddings.setPerson(this);
        return this;
    }

    public Person removeEmbeddings(Embeddings embeddings) {
        this.embeddings.remove(embeddings);
        embeddings.setPerson(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
