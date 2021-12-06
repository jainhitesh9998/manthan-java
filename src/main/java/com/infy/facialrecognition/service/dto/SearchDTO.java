package com.infy.facialrecognition.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.infy.facialrecognition.domain.Search} entity.
 */
public class SearchDTO implements Serializable {

    private Long id;

    private List<SearchResultDTO> result;

    private Instant time;

    private String initiatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SearchResultDTO> getResult() {
        return result;
    }

    public void setResult(List<SearchResultDTO> result) {
        this.result = result;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchDTO)) {
            return false;
        }

        SearchDTO searchDTO = (SearchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, searchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchDTO{" +
            "id=" + getId() +
            ", result='" + getResult() + "'" +
            ", time='" + getTime() + "'" +
            ", initiatedBy='" + getInitiatedBy() + "'" +
            "}";
    }
}
