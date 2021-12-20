package com.tachnolife.gamolife.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tachnolife.gamolife.IntegrationTest;
import com.tachnolife.gamolife.domain.GameShare;
import com.tachnolife.gamolife.repository.GameShareRepository;
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
 * Integration tests for the {@link GameShareResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameShareResourceIT {

    private static final Integer DEFAULT_MAX_PLAY = 1;
    private static final Integer UPDATED_MAX_PLAY = 2;

    private static final Instant DEFAULT_SHARE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHARE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/game-shares";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameShareRepository gameShareRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameShareMockMvc;

    private GameShare gameShare;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameShare createEntity(EntityManager em) {
        GameShare gameShare = new GameShare().maxPlay(DEFAULT_MAX_PLAY).shareTime(DEFAULT_SHARE_TIME);
        return gameShare;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameShare createUpdatedEntity(EntityManager em) {
        GameShare gameShare = new GameShare().maxPlay(UPDATED_MAX_PLAY).shareTime(UPDATED_SHARE_TIME);
        return gameShare;
    }

    @BeforeEach
    public void initTest() {
        gameShare = createEntity(em);
    }

    @Test
    @Transactional
    void createGameShare() throws Exception {
        int databaseSizeBeforeCreate = gameShareRepository.findAll().size();
        // Create the GameShare
        restGameShareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameShare)))
            .andExpect(status().isCreated());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeCreate + 1);
        GameShare testGameShare = gameShareList.get(gameShareList.size() - 1);
        assertThat(testGameShare.getMaxPlay()).isEqualTo(DEFAULT_MAX_PLAY);
        assertThat(testGameShare.getShareTime()).isEqualTo(DEFAULT_SHARE_TIME);
    }

    @Test
    @Transactional
    void createGameShareWithExistingId() throws Exception {
        // Create the GameShare with an existing ID
        gameShare.setId(1L);

        int databaseSizeBeforeCreate = gameShareRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameShareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameShare)))
            .andExpect(status().isBadRequest());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkShareTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameShareRepository.findAll().size();
        // set the field null
        gameShare.setShareTime(null);

        // Create the GameShare, which fails.

        restGameShareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameShare)))
            .andExpect(status().isBadRequest());

        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGameShares() throws Exception {
        // Initialize the database
        gameShareRepository.saveAndFlush(gameShare);

        // Get all the gameShareList
        restGameShareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameShare.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxPlay").value(hasItem(DEFAULT_MAX_PLAY)))
            .andExpect(jsonPath("$.[*].shareTime").value(hasItem(DEFAULT_SHARE_TIME.toString())));
    }

    @Test
    @Transactional
    void getGameShare() throws Exception {
        // Initialize the database
        gameShareRepository.saveAndFlush(gameShare);

        // Get the gameShare
        restGameShareMockMvc
            .perform(get(ENTITY_API_URL_ID, gameShare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameShare.getId().intValue()))
            .andExpect(jsonPath("$.maxPlay").value(DEFAULT_MAX_PLAY))
            .andExpect(jsonPath("$.shareTime").value(DEFAULT_SHARE_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGameShare() throws Exception {
        // Get the gameShare
        restGameShareMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGameShare() throws Exception {
        // Initialize the database
        gameShareRepository.saveAndFlush(gameShare);

        int databaseSizeBeforeUpdate = gameShareRepository.findAll().size();

        // Update the gameShare
        GameShare updatedGameShare = gameShareRepository.findById(gameShare.getId()).get();
        // Disconnect from session so that the updates on updatedGameShare are not directly saved in db
        em.detach(updatedGameShare);
        updatedGameShare.maxPlay(UPDATED_MAX_PLAY).shareTime(UPDATED_SHARE_TIME);

        restGameShareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGameShare.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGameShare))
            )
            .andExpect(status().isOk());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeUpdate);
        GameShare testGameShare = gameShareList.get(gameShareList.size() - 1);
        assertThat(testGameShare.getMaxPlay()).isEqualTo(UPDATED_MAX_PLAY);
        assertThat(testGameShare.getShareTime()).isEqualTo(UPDATED_SHARE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingGameShare() throws Exception {
        int databaseSizeBeforeUpdate = gameShareRepository.findAll().size();
        gameShare.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameShareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameShare.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameShare))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameShare() throws Exception {
        int databaseSizeBeforeUpdate = gameShareRepository.findAll().size();
        gameShare.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameShareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameShare))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameShare() throws Exception {
        int databaseSizeBeforeUpdate = gameShareRepository.findAll().size();
        gameShare.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameShareMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameShare)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameShareWithPatch() throws Exception {
        // Initialize the database
        gameShareRepository.saveAndFlush(gameShare);

        int databaseSizeBeforeUpdate = gameShareRepository.findAll().size();

        // Update the gameShare using partial update
        GameShare partialUpdatedGameShare = new GameShare();
        partialUpdatedGameShare.setId(gameShare.getId());

        partialUpdatedGameShare.maxPlay(UPDATED_MAX_PLAY);

        restGameShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameShare.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameShare))
            )
            .andExpect(status().isOk());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeUpdate);
        GameShare testGameShare = gameShareList.get(gameShareList.size() - 1);
        assertThat(testGameShare.getMaxPlay()).isEqualTo(UPDATED_MAX_PLAY);
        assertThat(testGameShare.getShareTime()).isEqualTo(DEFAULT_SHARE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateGameShareWithPatch() throws Exception {
        // Initialize the database
        gameShareRepository.saveAndFlush(gameShare);

        int databaseSizeBeforeUpdate = gameShareRepository.findAll().size();

        // Update the gameShare using partial update
        GameShare partialUpdatedGameShare = new GameShare();
        partialUpdatedGameShare.setId(gameShare.getId());

        partialUpdatedGameShare.maxPlay(UPDATED_MAX_PLAY).shareTime(UPDATED_SHARE_TIME);

        restGameShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameShare.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameShare))
            )
            .andExpect(status().isOk());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeUpdate);
        GameShare testGameShare = gameShareList.get(gameShareList.size() - 1);
        assertThat(testGameShare.getMaxPlay()).isEqualTo(UPDATED_MAX_PLAY);
        assertThat(testGameShare.getShareTime()).isEqualTo(UPDATED_SHARE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingGameShare() throws Exception {
        int databaseSizeBeforeUpdate = gameShareRepository.findAll().size();
        gameShare.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameShare.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameShare))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameShare() throws Exception {
        int databaseSizeBeforeUpdate = gameShareRepository.findAll().size();
        gameShare.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameShare))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameShare() throws Exception {
        int databaseSizeBeforeUpdate = gameShareRepository.findAll().size();
        gameShare.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameShareMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameShare))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameShare in the database
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameShare() throws Exception {
        // Initialize the database
        gameShareRepository.saveAndFlush(gameShare);

        int databaseSizeBeforeDelete = gameShareRepository.findAll().size();

        // Delete the gameShare
        restGameShareMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameShare.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameShare> gameShareList = gameShareRepository.findAll();
        assertThat(gameShareList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
