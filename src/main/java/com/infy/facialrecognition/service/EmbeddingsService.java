package com.infy.facialrecognition.service;

import com.infy.facialrecognition.service.dto.EmbeddingsDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.infy.facialrecognition.domain.Embeddings}.
 */
public interface EmbeddingsService {
    /**
     * Save a embeddings.
     *
     * @param embeddingsDTO the entity to save.
     * @return the persisted entity.
     */
    EmbeddingsDTO save(EmbeddingsDTO embeddingsDTO);

    /**
     * Partially updates a embeddings.
     *
     * @param embeddingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmbeddingsDTO> partialUpdate(EmbeddingsDTO embeddingsDTO);

    /**
     * Get all the embeddings.
     *
     * @return the list of entities.
     */
    List<EmbeddingsDTO> findAll();

    /**
     * Get the "id" embeddings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmbeddingsDTO> findOne(Long id);

    /**
     * Delete the "id" embeddings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
