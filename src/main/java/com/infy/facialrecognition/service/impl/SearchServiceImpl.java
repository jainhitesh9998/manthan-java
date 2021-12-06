package com.infy.facialrecognition.service.impl;

import com.infy.facialrecognition.domain.Search;
import com.infy.facialrecognition.repository.SearchRepository;
import com.infy.facialrecognition.service.SearchService;
import com.infy.facialrecognition.service.dto.SearchDTO;
import com.infy.facialrecognition.service.mapper.SearchMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Search}.
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService {

    private final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final SearchRepository searchRepository;

    private final SearchMapper searchMapper;

    public SearchServiceImpl(SearchRepository searchRepository, SearchMapper searchMapper) {
        this.searchRepository = searchRepository;
        this.searchMapper = searchMapper;
    }

    @Override
    public SearchDTO save(SearchDTO searchDTO) {
        log.debug("Request to save Search : {}", searchDTO);
        Search search = searchMapper.toEntity(searchDTO);
        search = searchRepository.save(search);
        return searchMapper.toDto(search);
    }

    @Override
    public Optional<SearchDTO> partialUpdate(SearchDTO searchDTO) {
        log.debug("Request to partially update Search : {}", searchDTO);

        return searchRepository
            .findById(searchDTO.getId())
            .map(existingSearch -> {
                searchMapper.partialUpdate(existingSearch, searchDTO);

                return existingSearch;
            })
            .map(searchRepository::save)
            .map(searchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchDTO> findAll() {
        log.debug("Request to get all Searches");
        return searchRepository.findAll().stream().map(searchMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SearchDTO> findOne(Long id) {
        log.debug("Request to get Search : {}", id);
        return searchRepository.findById(id).map(searchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Search : {}", id);
        searchRepository.deleteById(id);
    }
}
