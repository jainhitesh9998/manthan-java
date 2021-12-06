package com.infy.facialrecognition.repository;

import com.infy.facialrecognition.domain.Embeddings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Embeddings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmbeddingsRepository extends JpaRepository<Embeddings, Long> {}
