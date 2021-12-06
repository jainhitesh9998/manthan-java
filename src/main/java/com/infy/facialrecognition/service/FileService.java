package com.infy.facialrecognition.service;

import com.infy.facialrecognition.service.dto.FileDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.infy.facialrecognition.domain.File}.
 */
public interface FileService {
    /**
     * Save a file.
     *
     * @param fileDTO the entity to save.
     * @return the persisted entity.
     */
    FileDTO save(FileDTO fileDTO);

    /**
     * Partially updates a file.
     *
     * @param fileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileDTO> partialUpdate(FileDTO fileDTO);

    /**
     * Get all the files.
     *
     * @return the list of entities.
     */
    List<FileDTO> findAll();

    /**
     * Get the "id" file.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileDTO> findOne(Long id);

    /**
     * Delete the "id" file.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
