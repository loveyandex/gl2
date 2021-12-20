package com.tachnolife.gamolife.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tachnolife.gamolife.IntegrationTest;
import com.tachnolife.gamolife.domain.PlayHistory;
import com.tachnolife.gamolife.repository.PlayHistoryRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PlayHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayHistoryResourceIT {

    private static final Integer DEFAULT_MAX_PLAY = 1;
    private static final Integer UPDATED_MAX_PLAY = 2;

    private static final Integer DEFAULT_DATE_PLAYS = 1;
    private static final Integer UPDATED_DATE_PLAYS = 2;

    private static final LocalDate DEFAULT_PLAY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PLAY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/play-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayHistoryRepository playHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayHistoryMockMvc;

    private PlayHistory playHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayHistory createEntity(EntityManager em) {
        PlayHistory playHistory = new PlayHistory().maxPlay(DEFAULT_MAX_PLAY).datePlays(DEFAULT_DATE_PLAYS).playDate(DEFAULT_PLAY_DATE);
        return playHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayHistory createUpdatedEntity(EntityManager em) {
        PlayHistory playHistory = new PlayHistory().maxPlay(UPDATED_MAX_PLAY).datePlays(UPDATED_DATE_PLAYS).playDate(UPDATED_PLAY_DATE);
        return playHistory;
    }

    @BeforeEach
    public void initTest() {
        playHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayHistory() throws Exception {
        int databaseSizeBeforeCreate = playHistoryRepository.findAll().size();
        // Create the PlayHistory
        restPlayHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playHistory)))
            .andExpect(status().isCreated());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        PlayHistory testPlayHistory = playHistoryList.get(playHistoryList.size() - 1);
        assertThat(testPlayHistory.getMaxPlay()).isEqualTo(DEFAULT_MAX_PLAY);
        assertThat(testPlayHistory.getDatePlays()).isEqualTo(DEFAULT_DATE_PLAYS);
        assertThat(testPlayHistory.getPlayDate()).isEqualTo(DEFAULT_PLAY_DATE);
    }

    @Test
    @Transactional
    void createPlayHistoryWithExistingId() throws Exception {
        // Create the PlayHistory with an existing ID
        playHistory.setId(1L);

        int databaseSizeBeforeCreate = playHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playHistory)))
            .andExpect(status().isBadRequest());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDatePlaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = playHistoryRepository.findAll().size();
        // set the field null
        playHistory.setDatePlays(null);

        // Create the PlayHistory, which fails.

        restPlayHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playHistory)))
            .andExpect(status().isBadRequest());

        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlayDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = playHistoryRepository.findAll().size();
        // set the field null
        playHistory.setPlayDate(null);

        // Create the PlayHistory, which fails.

        restPlayHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playHistory)))
            .andExpect(status().isBadRequest());

        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlayHistories() throws Exception {
        // Initialize the database
        playHistoryRepository.saveAndFlush(playHistory);

        // Get all the playHistoryList
        restPlayHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxPlay").value(hasItem(DEFAULT_MAX_PLAY)))
            .andExpect(jsonPath("$.[*].datePlays").value(hasItem(DEFAULT_DATE_PLAYS)))
            .andExpect(jsonPath("$.[*].playDate").value(hasItem(DEFAULT_PLAY_DATE.toString())));
    }

    @Test
    @Transactional
    void getPlayHistory() throws Exception {
        // Initialize the database
        playHistoryRepository.saveAndFlush(playHistory);

        // Get the playHistory
        restPlayHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, playHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playHistory.getId().intValue()))
            .andExpect(jsonPath("$.maxPlay").value(DEFAULT_MAX_PLAY))
            .andExpect(jsonPath("$.datePlays").value(DEFAULT_DATE_PLAYS))
            .andExpect(jsonPath("$.playDate").value(DEFAULT_PLAY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlayHistory() throws Exception {
        // Get the playHistory
        restPlayHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlayHistory() throws Exception {
        // Initialize the database
        playHistoryRepository.saveAndFlush(playHistory);

        int databaseSizeBeforeUpdate = playHistoryRepository.findAll().size();

        // Update the playHistory
        PlayHistory updatedPlayHistory = playHistoryRepository.findById(playHistory.getId()).get();
        // Disconnect from session so that the updates on updatedPlayHistory are not directly saved in db
        em.detach(updatedPlayHistory);
        updatedPlayHistory.maxPlay(UPDATED_MAX_PLAY).datePlays(UPDATED_DATE_PLAYS).playDate(UPDATED_PLAY_DATE);

        restPlayHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlayHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlayHistory))
            )
            .andExpect(status().isOk());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeUpdate);
        PlayHistory testPlayHistory = playHistoryList.get(playHistoryList.size() - 1);
        assertThat(testPlayHistory.getMaxPlay()).isEqualTo(UPDATED_MAX_PLAY);
        assertThat(testPlayHistory.getDatePlays()).isEqualTo(UPDATED_DATE_PLAYS);
        assertThat(testPlayHistory.getPlayDate()).isEqualTo(UPDATED_PLAY_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPlayHistory() throws Exception {
        int databaseSizeBeforeUpdate = playHistoryRepository.findAll().size();
        playHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayHistory() throws Exception {
        int databaseSizeBeforeUpdate = playHistoryRepository.findAll().size();
        playHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayHistory() throws Exception {
        int databaseSizeBeforeUpdate = playHistoryRepository.findAll().size();
        playHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayHistoryWithPatch() throws Exception {
        // Initialize the database
        playHistoryRepository.saveAndFlush(playHistory);

        int databaseSizeBeforeUpdate = playHistoryRepository.findAll().size();

        // Update the playHistory using partial update
        PlayHistory partialUpdatedPlayHistory = new PlayHistory();
        partialUpdatedPlayHistory.setId(playHistory.getId());

        restPlayHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayHistory))
            )
            .andExpect(status().isOk());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeUpdate);
        PlayHistory testPlayHistory = playHistoryList.get(playHistoryList.size() - 1);
        assertThat(testPlayHistory.getMaxPlay()).isEqualTo(DEFAULT_MAX_PLAY);
        assertThat(testPlayHistory.getDatePlays()).isEqualTo(DEFAULT_DATE_PLAYS);
        assertThat(testPlayHistory.getPlayDate()).isEqualTo(DEFAULT_PLAY_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePlayHistoryWithPatch() throws Exception {
        // Initialize the database
        playHistoryRepository.saveAndFlush(playHistory);

        int databaseSizeBeforeUpdate = playHistoryRepository.findAll().size();

        // Update the playHistory using partial update
        PlayHistory partialUpdatedPlayHistory = new PlayHistory();
        partialUpdatedPlayHistory.setId(playHistory.getId());

        partialUpdatedPlayHistory.maxPlay(UPDATED_MAX_PLAY).datePlays(UPDATED_DATE_PLAYS).playDate(UPDATED_PLAY_DATE);

        restPlayHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayHistory))
            )
            .andExpect(status().isOk());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeUpdate);
        PlayHistory testPlayHistory = playHistoryList.get(playHistoryList.size() - 1);
        assertThat(testPlayHistory.getMaxPlay()).isEqualTo(UPDATED_MAX_PLAY);
        assertThat(testPlayHistory.getDatePlays()).isEqualTo(UPDATED_DATE_PLAYS);
        assertThat(testPlayHistory.getPlayDate()).isEqualTo(UPDATED_PLAY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPlayHistory() throws Exception {
        int databaseSizeBeforeUpdate = playHistoryRepository.findAll().size();
        playHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayHistory() throws Exception {
        int databaseSizeBeforeUpdate = playHistoryRepository.findAll().size();
        playHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayHistory() throws Exception {
        int databaseSizeBeforeUpdate = playHistoryRepository.findAll().size();
        playHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(playHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayHistory in the database
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayHistory() throws Exception {
        // Initialize the database
        playHistoryRepository.saveAndFlush(playHistory);

        int databaseSizeBeforeDelete = playHistoryRepository.findAll().size();

        // Delete the playHistory
        restPlayHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, playHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayHistory> playHistoryList = playHistoryRepository.findAll();
        assertThat(playHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
