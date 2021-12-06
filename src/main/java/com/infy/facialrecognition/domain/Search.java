package com.infy.facialrecognition.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

import com.infy.facialrecognition.service.dto.SearchResultDTO;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * A Search.
 */
@Entity
@Table(name = "search")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Search implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "result", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<SearchResultDTO> result;

    @Column(name = "time")
    private Instant time;

    @Column(name = "initiated_by")
    private String initiatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Search id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SearchResultDTO> getResult() {
        return this.result;
    }

    public Search result(List<SearchResultDTO> result) {
        this.setResult(result);
        return this;
    }

    public void setResult(List<SearchResultDTO> result) {
        this.result = result;
    }

    public Instant getTime() {
        return this.time;
    }

    public Search time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getInitiatedBy() {
        return this.initiatedBy;
    }

    public Search initiatedBy(String initiatedBy) {
        this.setInitiatedBy(initiatedBy);
        return this;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Search)) {
            return false;
        }
        return id != null && id.equals(((Search) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Search{" +
            "id=" + getId() +
            ", result='" + getResult() + "'" +
            ", time='" + getTime() + "'" +
            ", initiatedBy='" + getInitiatedBy() + "'" +
            "}";
    }
}
