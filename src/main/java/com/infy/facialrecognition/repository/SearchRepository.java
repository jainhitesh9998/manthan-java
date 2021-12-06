package com.infy.facialrecognition.repository;

import com.infy.facialrecognition.domain.Search;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Search entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {}
