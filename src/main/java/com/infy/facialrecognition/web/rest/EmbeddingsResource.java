package com.infy.facialrecognition.web.rest;

import com.infy.facialrecognition.repository.EmbeddingsRepository;
import com.infy.facialrecognition.service.EmbeddingsService;
import com.infy.facialrecognition.service.dto.EmbeddingsDTO;
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
 * REST controller for managing {@link com.infy.facialrecognition.domain.Embeddings}.
 */
@RestController
@RequestMapping("/api")
public class EmbeddingsResource {

    private final Logger log = LoggerFactory.getLogger(EmbeddingsResource.class);

    private static final String ENTITY_NAME = "facialRecognitionEmbeddings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmbeddingsService embeddingsService;

    private final EmbeddingsRepository embeddingsRepository;

    public EmbeddingsResource(EmbeddingsService embeddingsService, EmbeddingsRepository embeddingsRepository) {
        this.embeddingsService = embeddingsService;
        this.embeddingsRepository = embeddingsRepository;
    }

    /**
     * {@code POST  /embeddings} : Create a new embeddings.
     *
     * @param embeddingsDTO the embeddingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new embeddingsDTO, or with status {@code 400 (Bad Request)} if the embeddings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/embeddings")
    public ResponseEntity<EmbeddingsDTO> createEmbeddings(@RequestBody EmbeddingsDTO embeddingsDTO) throws URISyntaxException {
        log.debug("REST request to save Embeddings : {}", embeddingsDTO);
        if (embeddingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new embeddings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmbeddingsDTO result = embeddingsService.save(embeddingsDTO);
        return ResponseEntity
            .created(new URI("/api/embeddings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /embeddings/:id} : Updates an existing embeddings.
     *
     * @param id the id of the embeddingsDTO to save.
     * @param embeddingsDTO the embeddingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated embeddingsDTO,
     * or with status {@code 400 (Bad Request)} if the embeddingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the embeddingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/embeddings/{id}")
    public ResponseEntity<EmbeddingsDTO> updateEmbeddings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmbeddingsDTO embeddingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Embeddings : {}, {}", id, embeddingsDTO);
        if (embeddingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, embeddingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!embeddingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmbeddingsDTO result = embeddingsService.save(embeddingsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, embeddingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /embeddings/:id} : Partial updates given fields of an existing embeddings, field will ignore if it is null
     *
     * @param id the id of the embeddingsDTO to save.
     * @param embeddingsDTO the embeddingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated embeddingsDTO,
     * or with status {@code 400 (Bad Request)} if the embeddingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the embeddingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the embeddingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/embeddings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmbeddingsDTO> partialUpdateEmbeddings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmbeddingsDTO embeddingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Embeddings partially : {}, {}", id, embeddingsDTO);
        if (embeddingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, embeddingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!embeddingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmbeddingsDTO> result = embeddingsService.partialUpdate(embeddingsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, embeddingsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /embeddings} : get all the embeddings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of embeddings in body.
     */
    @GetMapping("/embeddings")
    public List<EmbeddingsDTO> getAllEmbeddings() {
        log.debug("REST request to get all Embeddings");
        return embeddingsService.findAll();
    }

    /**
     * {@code GET  /embeddings/:id} : get the "id" embeddings.
     *
     * @param id the id of the embeddingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the embeddingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/embeddings/{id}")
    public ResponseEntity<EmbeddingsDTO> getEmbeddings(@PathVariable Long id) {
        log.debug("REST request to get Embeddings : {}", id);
        Optional<EmbeddingsDTO> embeddingsDTO = embeddingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(embeddingsDTO);
    }

    /**
     * {@code DELETE  /embeddings/:id} : delete the "id" embeddings.
     *
     * @param id the id of the embeddingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/embeddings/{id}")
    public ResponseEntity<Void> deleteEmbeddings(@PathVariable Long id) {
        log.debug("REST request to delete Embeddings : {}", id);
        embeddingsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
