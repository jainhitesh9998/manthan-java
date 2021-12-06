package com.infy.facialrecognition.service.impl;

import com.infy.facialrecognition.domain.Embeddings;
import com.infy.facialrecognition.repository.EmbeddingsRepository;
import com.infy.facialrecognition.service.EmbeddingsService;
import com.infy.facialrecognition.service.dto.EmbeddingsDTO;
import com.infy.facialrecognition.service.mapper.EmbeddingsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Embeddings}.
 */
@Service
@Transactional
public class EmbeddingsServiceImpl implements EmbeddingsService {

    private final Logger log = LoggerFactory.getLogger(EmbeddingsServiceImpl.class);

    private final EmbeddingsRepository embeddingsRepository;

    private final EmbeddingsMapper embeddingsMapper;

    public EmbeddingsServiceImpl(EmbeddingsRepository embeddingsRepository, EmbeddingsMapper embeddingsMapper) {
        this.embeddingsRepository = embeddingsRepository;
        this.embeddingsMapper = embeddingsMapper;
    }

    @Override
    public EmbeddingsDTO save(EmbeddingsDTO embeddingsDTO) {
        log.debug("Request to save Embeddings : {}", embeddingsDTO);
        Embeddings embeddings = embeddingsMapper.toEntity(embeddingsDTO);
        embeddings = embeddingsRepository.save(embeddings);
        return embeddingsMapper.toDto(embeddings);
    }

    @Override
    public Optional<EmbeddingsDTO> partialUpdate(EmbeddingsDTO embeddingsDTO) {
        log.debug("Request to partially update Embeddings : {}", embeddingsDTO);

        return embeddingsRepository
            .findById(embeddingsDTO.getId())
            .map(existingEmbeddings -> {
                embeddingsMapper.partialUpdate(existingEmbeddings, embeddingsDTO);

                return existingEmbeddings;
            })
            .map(embeddingsRepository::save)
            .map(embeddingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmbeddingsDTO> findAll() {
        log.debug("Request to get all Embeddings");
        return embeddingsRepository.findAll().stream().map(embeddingsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmbeddingsDTO> findOne(Long id) {
        log.debug("Request to get Embeddings : {}", id);
        return embeddingsRepository.findById(id).map(embeddingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Embeddings : {}", id);
        embeddingsRepository.deleteById(id);
    }
}
