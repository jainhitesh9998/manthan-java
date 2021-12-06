package com.infy.facialrecognition.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.infy.facialrecognition.domain.Embeddings} entity.
 */
public class EmbeddingsDTO implements Serializable {

    private Long id;

    private List<Double> embedding;

    private PersonDTO person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmbeddingsDTO)) {
            return false;
        }

        EmbeddingsDTO embeddingsDTO = (EmbeddingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, embeddingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmbeddingsDTO{" +
            "id=" + getId() +
            ", embedding='" + getEmbedding() + "'" +
            ", person=" + getPerson() +
            "}";
    }
}
