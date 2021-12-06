package com.infy.facialrecognition.web.rest;

import com.infy.facialrecognition.repository.SearchRepository;
import com.infy.facialrecognition.service.SearchService;
import com.infy.facialrecognition.service.dto.SearchDTO;
import com.infy.facialrecognition.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.infy.facialrecognition.domain.Search}.
 */
@RestController
@RequestMapping("/api")
public class SearchResource {

    private final Logger log = LoggerFactory.getLogger(SearchResource.class);

    private static final String ENTITY_NAME = "facialRecognitionSearch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchService searchService;

    private final SearchRepository searchRepository;

    public SearchResource(SearchService searchService, SearchRepository searchRepository) {
        this.searchService = searchService;
        this.searchRepository = searchRepository;
    }

    /**
     * {@code POST  /searches} : Create a new search.
     *
     * @param searchDTO the searchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchDTO, or with status {@code 400 (Bad Request)} if the search has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/searches")
    public ResponseEntity<SearchDTO> createSearch(@RequestBody SearchDTO searchDTO) throws URISyntaxException {
        log.debug("REST request to save Search : {}", searchDTO);
        if (searchDTO.getId() != null) {
            throw new BadRequestAlertException("A new search cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SearchDTO result = searchService.save(searchDTO);
        return ResponseEntity
            .created(new URI("/api/searches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /searches/:id} : Updates an existing search.
     *
     * @param id the id of the searchDTO to save.
     * @param searchDTO the searchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchDTO,
     * or with status {@code 400 (Bad Request)} if the searchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/searches/{id}")
    public ResponseEntity<SearchDTO> updateSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SearchDTO searchDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Search : {}, {}", id, searchDTO);
        if (searchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SearchDTO result = searchService.save(searchDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, searchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /searches/:id} : Partial updates given fields of an existing search, field will ignore if it is null
     *
     * @param id the id of the searchDTO to save.
     * @param searchDTO the searchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchDTO,
     * or with status {@code 400 (Bad Request)} if the searchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the searchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/searches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchDTO> partialUpdateSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SearchDTO searchDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Search partially : {}, {}", id, searchDTO);
        if (searchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchDTO> result = searchService.partialUpdate(searchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, searchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /searches} : get all the searches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searches in body.
     */
    @GetMapping("/searches")
    public List<SearchDTO> getAllSearches() {
        log.debug("REST request to get all Searches");
        return searchService.findAll();
    }

    /**
     * {@code GET  /searches/:id} : get the "id" search.
     *
     * @param id the id of the searchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/searches/{id}")
    public ResponseEntity<SearchDTO> getSearch(@PathVariable Long id) {
        log.debug("REST request to get Search : {}", id);
        Optional<SearchDTO> searchDTO = searchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchDTO);
    }

    /**
     * {@code DELETE  /searches/:id} : delete the "id" search.
     *
     * @param id the id of the searchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/searches/{id}")
    public ResponseEntity<Void> deleteSearch(@PathVariable Long id) {
        log.debug("REST request to delete Search : {}", id);
        searchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
