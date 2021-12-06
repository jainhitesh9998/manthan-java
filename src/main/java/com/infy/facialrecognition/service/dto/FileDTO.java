package com.infy.facialrecognition.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.infy.facialrecognition.domain.File} entity.
 */
public class FileDTO implements Serializable {

    private Long id;

    private String location;

    private String source;

    private String encoding;

    private Long identifier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDTO)) {
            return false;
        }

        FileDTO fileDTO = (FileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDTO{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", source='" + getSource() + "'" +
            ", encoding='" + getEncoding() + "'" +
            ", identifier=" + getIdentifier() +
            "}";
    }
}
