package com.infy.facialrecognition.service;

import com.infy.facialrecognition.service.dto.SearchDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.infy.facialrecognition.domain.Search}.
 */
public interface SearchService {
    /**
     * Save a search.
     *
     * @param searchDTO the entity to save.
     * @return the persisted entity.
     */
    SearchDTO save(SearchDTO searchDTO);

    /**
     * Partially updates a search.
     *
     * @param searchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SearchDTO> partialUpdate(SearchDTO searchDTO);

    /**
     * Get all the searches.
     *
     * @return the list of entities.
     */
    List<SearchDTO> findAll();

    /**
     * Get the "id" search.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SearchDTO> findOne(Long id);

    /**
     * Delete the "id" search.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
