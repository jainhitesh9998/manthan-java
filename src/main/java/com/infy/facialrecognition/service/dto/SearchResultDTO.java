package com.infy.facialrecognition.service.dto;

import java.util.Objects;

public class SearchResultDTO {
    long id;

    long distance;

    public SearchResultDTO(long id, long distance) {
        this.id = id;
        this.distance = distance;
    }

    public SearchResultDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "SearchResultDTO{" +
            "id=" + id +
            ", distance=" + distance +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResultDTO that = (SearchResultDTO) o;
        return id == that.id &&
            distance == that.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distance);
    }
}
