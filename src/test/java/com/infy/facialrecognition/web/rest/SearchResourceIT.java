package com.infy.facialrecognition.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infy.facialrecognition.IntegrationTest;
import com.infy.facialrecognition.domain.Search;
import com.infy.facialrecognition.repository.SearchRepository;
import com.infy.facialrecognition.service.dto.SearchDTO;
import com.infy.facialrecognition.service.mapper.SearchMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SearchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchResourceIT {

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_INITIATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_INITIATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/searches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private SearchMapper searchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchMockMvc;

    private Search search;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Search createEntity(EntityManager em) {
        Search search = new Search();//.result(DEFAULT_RESULT).time(DEFAULT_TIME).initiatedBy(DEFAULT_INITIATED_BY);
        return search;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Search createUpdatedEntity(EntityManager em) {
        Search search = new Search();//.result(UPDATED_RESULT).time(UPDATED_TIME).initiatedBy(UPDATED_INITIATED_BY);
        return search;
    }

    @BeforeEach
    public void initTest() {
        search = createEntity(em);
    }

    @Test
    @Transactional
    void createSearch() throws Exception {
        int databaseSizeBeforeCreate = searchRepository.findAll().size();
        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);
        restSearchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeCreate + 1);
        Search testSearch = searchList.get(searchList.size() - 1);
        assertThat(testSearch.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testSearch.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testSearch.getInitiatedBy()).isEqualTo(DEFAULT_INITIATED_BY);
    }

    @Test
    @Transactional
    void createSearchWithExistingId() throws Exception {
        // Create the Search with an existing ID
        search.setId(1L);
        SearchDTO searchDTO = searchMapper.toDto(search);

        int databaseSizeBeforeCreate = searchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSearches() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        // Get all the searchList
        restSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(search.getId().intValue())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].initiatedBy").value(hasItem(DEFAULT_INITIATED_BY)));
    }

    @Test
    @Transactional
    void getSearch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        // Get the search
        restSearchMockMvc
            .perform(get(ENTITY_API_URL_ID, search.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(search.getId().intValue()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.initiatedBy").value(DEFAULT_INITIATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingSearch() throws Exception {
        // Get the search
        restSearchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSearch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        int databaseSizeBeforeUpdate = searchRepository.findAll().size();

        // Update the search
        Search updatedSearch = searchRepository.findById(search.getId()).get();
        // Disconnect from session so that the updates on updatedSearch are not directly saved in db
        em.detach(updatedSearch);
//        updatedSearch.result(UPDATED_RESULT).time(UPDATED_TIME).initiatedBy(UPDATED_INITIATED_BY);
        SearchDTO searchDTO = searchMapper.toDto(updatedSearch);

        restSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isOk());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
        Search testSearch = searchList.get(searchList.size() - 1);
        assertThat(testSearch.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testSearch.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testSearch.getInitiatedBy()).isEqualTo(UPDATED_INITIATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchWithPatch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        int databaseSizeBeforeUpdate = searchRepository.findAll().size();

        // Update the search using partial update
        Search partialUpdatedSearch = new Search();
        partialUpdatedSearch.setId(search.getId());

        partialUpdatedSearch.time(UPDATED_TIME).initiatedBy(UPDATED_INITIATED_BY);

        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearch.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearch))
            )
            .andExpect(status().isOk());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
        Search testSearch = searchList.get(searchList.size() - 1);
        assertThat(testSearch.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testSearch.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testSearch.getInitiatedBy()).isEqualTo(UPDATED_INITIATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateSearchWithPatch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        int databaseSizeBeforeUpdate = searchRepository.findAll().size();

        // Update the search using partial update
        Search partialUpdatedSearch = new Search();
        partialUpdatedSearch.setId(search.getId());

//        partialUpdatedSearch.result(UPDATED_RESULT).time(UPDATED_TIME).initiatedBy(UPDATED_INITIATED_BY);

        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearch.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearch))
            )
            .andExpect(status().isOk());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
        Search testSearch = searchList.get(searchList.size() - 1);
        assertThat(testSearch.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testSearch.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testSearch.getInitiatedBy()).isEqualTo(UPDATED_INITIATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        int databaseSizeBeforeDelete = searchRepository.findAll().size();

        // Delete the search
        restSearchMockMvc
            .perform(delete(ENTITY_API_URL_ID, search.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
