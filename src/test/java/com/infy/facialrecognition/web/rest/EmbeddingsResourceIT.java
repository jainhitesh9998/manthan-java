package com.infy.facialrecognition.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infy.facialrecognition.IntegrationTest;
import com.infy.facialrecognition.domain.Embeddings;
import com.infy.facialrecognition.repository.EmbeddingsRepository;
import com.infy.facialrecognition.service.dto.EmbeddingsDTO;
import com.infy.facialrecognition.service.mapper.EmbeddingsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmbeddingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmbeddingsResourceIT {

    private static final String DEFAULT_EMBEDDING = "AAAAAAAAAA";
    private static final String UPDATED_EMBEDDING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/embeddings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmbeddingsRepository embeddingsRepository;

    @Autowired
    private EmbeddingsMapper embeddingsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmbeddingsMockMvc;

    private Embeddings embeddings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Embeddings createEntity(EntityManager em) {
        Embeddings embeddings = new Embeddings();//.embedding(DEFAULT_EMBEDDING);
        return embeddings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Embeddings createUpdatedEntity(EntityManager em) {
        Embeddings embeddings = new Embeddings();//.embedding(UPDATED_EMBEDDING);
        return embeddings;
    }

    @BeforeEach
    public void initTest() {
        embeddings = createEntity(em);
    }

    @Test
    @Transactional
    void createEmbeddings() throws Exception {
        int databaseSizeBeforeCreate = embeddingsRepository.findAll().size();
        // Create the Embeddings
        EmbeddingsDTO embeddingsDTO = embeddingsMapper.toDto(embeddings);
        restEmbeddingsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(embeddingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeCreate + 1);
        Embeddings testEmbeddings = embeddingsList.get(embeddingsList.size() - 1);
        assertThat(testEmbeddings.getEmbedding()).isEqualTo(DEFAULT_EMBEDDING);
    }

    @Test
    @Transactional
    void createEmbeddingsWithExistingId() throws Exception {
        // Create the Embeddings with an existing ID
        embeddings.setId(1L);
        EmbeddingsDTO embeddingsDTO = embeddingsMapper.toDto(embeddings);

        int databaseSizeBeforeCreate = embeddingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmbeddingsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(embeddingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmbeddings() throws Exception {
        // Initialize the database
        embeddingsRepository.saveAndFlush(embeddings);

        // Get all the embeddingsList
        restEmbeddingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(embeddings.getId().intValue())))
            .andExpect(jsonPath("$.[*].embedding").value(hasItem(DEFAULT_EMBEDDING)));
    }

    @Test
    @Transactional
    void getEmbeddings() throws Exception {
        // Initialize the database
        embeddingsRepository.saveAndFlush(embeddings);

        // Get the embeddings
        restEmbeddingsMockMvc
            .perform(get(ENTITY_API_URL_ID, embeddings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(embeddings.getId().intValue()))
            .andExpect(jsonPath("$.embedding").value(DEFAULT_EMBEDDING));
    }

    @Test
    @Transactional
    void getNonExistingEmbeddings() throws Exception {
        // Get the embeddings
        restEmbeddingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmbeddings() throws Exception {
        // Initialize the database
        embeddingsRepository.saveAndFlush(embeddings);

        int databaseSizeBeforeUpdate = embeddingsRepository.findAll().size();

        // Update the embeddings
        Embeddings updatedEmbeddings = embeddingsRepository.findById(embeddings.getId()).get();
        // Disconnect from session so that the updates on updatedEmbeddings are not directly saved in db
        em.detach(updatedEmbeddings);
//        updatedEmbeddings.embedding(UPDATED_EMBEDDING);
        EmbeddingsDTO embeddingsDTO = embeddingsMapper.toDto(updatedEmbeddings);

        restEmbeddingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, embeddingsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(embeddingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeUpdate);
        Embeddings testEmbeddings = embeddingsList.get(embeddingsList.size() - 1);
        assertThat(testEmbeddings.getEmbedding()).isEqualTo(UPDATED_EMBEDDING);
    }

    @Test
    @Transactional
    void putNonExistingEmbeddings() throws Exception {
        int databaseSizeBeforeUpdate = embeddingsRepository.findAll().size();
        embeddings.setId(count.incrementAndGet());

        // Create the Embeddings
        EmbeddingsDTO embeddingsDTO = embeddingsMapper.toDto(embeddings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmbeddingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, embeddingsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(embeddingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmbeddings() throws Exception {
        int databaseSizeBeforeUpdate = embeddingsRepository.findAll().size();
        embeddings.setId(count.incrementAndGet());

        // Create the Embeddings
        EmbeddingsDTO embeddingsDTO = embeddingsMapper.toDto(embeddings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmbeddingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(embeddingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmbeddings() throws Exception {
        int databaseSizeBeforeUpdate = embeddingsRepository.findAll().size();
        embeddings.setId(count.incrementAndGet());

        // Create the Embeddings
        EmbeddingsDTO embeddingsDTO = embeddingsMapper.toDto(embeddings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmbeddingsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(embeddingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmbeddingsWithPatch() throws Exception {
        // Initialize the database
        embeddingsRepository.saveAndFlush(embeddings);

        int databaseSizeBeforeUpdate = embeddingsRepository.findAll().size();

        // Update the embeddings using partial update
        Embeddings partialUpdatedEmbeddings = new Embeddings();
        partialUpdatedEmbeddings.setId(embeddings.getId());

//        partialUpdatedEmbeddings.embedding(UPDATED_EMBEDDING);

        restEmbeddingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmbeddings.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmbeddings))
            )
            .andExpect(status().isOk());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeUpdate);
        Embeddings testEmbeddings = embeddingsList.get(embeddingsList.size() - 1);
        assertThat(testEmbeddings.getEmbedding()).isEqualTo(UPDATED_EMBEDDING);
    }

    @Test
    @Transactional
    void fullUpdateEmbeddingsWithPatch() throws Exception {
        // Initialize the database
        embeddingsRepository.saveAndFlush(embeddings);

        int databaseSizeBeforeUpdate = embeddingsRepository.findAll().size();

        // Update the embeddings using partial update
        Embeddings partialUpdatedEmbeddings = new Embeddings();
        partialUpdatedEmbeddings.setId(embeddings.getId());

//        partialUpdatedEmbeddings.embedding(UPDATED_EMBEDDING);

        restEmbeddingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmbeddings.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmbeddings))
            )
            .andExpect(status().isOk());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeUpdate);
        Embeddings testEmbeddings = embeddingsList.get(embeddingsList.size() - 1);
        assertThat(testEmbeddings.getEmbedding()).isEqualTo(UPDATED_EMBEDDING);
    }

    @Test
    @Transactional
    void patchNonExistingEmbeddings() throws Exception {
        int databaseSizeBeforeUpdate = embeddingsRepository.findAll().size();
        embeddings.setId(count.incrementAndGet());

        // Create the Embeddings
        EmbeddingsDTO embeddingsDTO = embeddingsMapper.toDto(embeddings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmbeddingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, embeddingsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(embeddingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmbeddings() throws Exception {
        int databaseSizeBeforeUpdate = embeddingsRepository.findAll().size();
        embeddings.setId(count.incrementAndGet());

        // Create the Embeddings
        EmbeddingsDTO embeddingsDTO = embeddingsMapper.toDto(embeddings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmbeddingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(embeddingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmbeddings() throws Exception {
        int databaseSizeBeforeUpdate = embeddingsRepository.findAll().size();
        embeddings.setId(count.incrementAndGet());

        // Create the Embeddings
        EmbeddingsDTO embeddingsDTO = embeddingsMapper.toDto(embeddings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmbeddingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(embeddingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Embeddings in the database
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmbeddings() throws Exception {
        // Initialize the database
        embeddingsRepository.saveAndFlush(embeddings);

        int databaseSizeBeforeDelete = embeddingsRepository.findAll().size();

        // Delete the embeddings
        restEmbeddingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, embeddings.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Embeddings> embeddingsList = embeddingsRepository.findAll();
        assertThat(embeddingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
