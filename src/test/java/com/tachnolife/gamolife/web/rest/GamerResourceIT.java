package com.tachnolife.gamolife.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tachnolife.gamolife.IntegrationTest;
import com.tachnolife.gamolife.domain.Gamer;
import com.tachnolife.gamolife.repository.GamerRepository;
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
 * Integration tests for the {@link GamerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GamerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONENUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_VERIFY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_VERIFY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REFERAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REFERAL_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_SCORE = 1L;
    private static final Long UPDATED_SCORE = 2L;

    private static final Boolean DEFAULT_CANPLAY_GAME_TODAY = false;
    private static final Boolean UPDATED_CANPLAY_GAME_TODAY = true;

    private static final String ENTITY_API_URL = "/api/gamers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GamerRepository gamerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGamerMockMvc;

    private Gamer gamer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gamer createEntity(EntityManager em) {
        Gamer gamer = new Gamer()
            .name(DEFAULT_NAME)
            .phonenumber(DEFAULT_PHONENUMBER)
            .verifyCode(DEFAULT_VERIFY_CODE)
            .referalCode(DEFAULT_REFERAL_CODE)
            .score(DEFAULT_SCORE)
            .canplayGameToday(DEFAULT_CANPLAY_GAME_TODAY);
        return gamer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gamer createUpdatedEntity(EntityManager em) {
        Gamer gamer = new Gamer()
            .name(UPDATED_NAME)
            .phonenumber(UPDATED_PHONENUMBER)
            .verifyCode(UPDATED_VERIFY_CODE)
            .referalCode(UPDATED_REFERAL_CODE)
            .score(UPDATED_SCORE)
            .canplayGameToday(UPDATED_CANPLAY_GAME_TODAY);
        return gamer;
    }

    @BeforeEach
    public void initTest() {
        gamer = createEntity(em);
    }

    @Test
    @Transactional
    void createGamer() throws Exception {
        int databaseSizeBeforeCreate = gamerRepository.findAll().size();
        // Create the Gamer
        restGamerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gamer)))
            .andExpect(status().isCreated());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeCreate + 1);
        Gamer testGamer = gamerList.get(gamerList.size() - 1);
        assertThat(testGamer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGamer.getPhonenumber()).isEqualTo(DEFAULT_PHONENUMBER);
        assertThat(testGamer.getVerifyCode()).isEqualTo(DEFAULT_VERIFY_CODE);
        assertThat(testGamer.getReferalCode()).isEqualTo(DEFAULT_REFERAL_CODE);
        assertThat(testGamer.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testGamer.getCanplayGameToday()).isEqualTo(DEFAULT_CANPLAY_GAME_TODAY);
    }

    @Test
    @Transactional
    void createGamerWithExistingId() throws Exception {
        // Create the Gamer with an existing ID
        gamer.setId(1L);

        int databaseSizeBeforeCreate = gamerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGamerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gamer)))
            .andExpect(status().isBadRequest());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhonenumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = gamerRepository.findAll().size();
        // set the field null
        gamer.setPhonenumber(null);

        // Create the Gamer, which fails.

        restGamerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gamer)))
            .andExpect(status().isBadRequest());

        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGamers() throws Exception {
        // Initialize the database
        gamerRepository.saveAndFlush(gamer);

        // Get all the gamerList
        restGamerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gamer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phonenumber").value(hasItem(DEFAULT_PHONENUMBER)))
            .andExpect(jsonPath("$.[*].verifyCode").value(hasItem(DEFAULT_VERIFY_CODE)))
            .andExpect(jsonPath("$.[*].referalCode").value(hasItem(DEFAULT_REFERAL_CODE)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())))
            .andExpect(jsonPath("$.[*].canplayGameToday").value(hasItem(DEFAULT_CANPLAY_GAME_TODAY.booleanValue())));
    }

    @Test
    @Transactional
    void getGamer() throws Exception {
        // Initialize the database
        gamerRepository.saveAndFlush(gamer);

        // Get the gamer
        restGamerMockMvc
            .perform(get(ENTITY_API_URL_ID, gamer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gamer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phonenumber").value(DEFAULT_PHONENUMBER))
            .andExpect(jsonPath("$.verifyCode").value(DEFAULT_VERIFY_CODE))
            .andExpect(jsonPath("$.referalCode").value(DEFAULT_REFERAL_CODE))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.intValue()))
            .andExpect(jsonPath("$.canplayGameToday").value(DEFAULT_CANPLAY_GAME_TODAY.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingGamer() throws Exception {
        // Get the gamer
        restGamerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGamer() throws Exception {
        // Initialize the database
        gamerRepository.saveAndFlush(gamer);

        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();

        // Update the gamer
        Gamer updatedGamer = gamerRepository.findById(gamer.getId()).get();
        // Disconnect from session so that the updates on updatedGamer are not directly saved in db
        em.detach(updatedGamer);
        updatedGamer
            .name(UPDATED_NAME)
            .phonenumber(UPDATED_PHONENUMBER)
            .verifyCode(UPDATED_VERIFY_CODE)
            .referalCode(UPDATED_REFERAL_CODE)
            .score(UPDATED_SCORE)
            .canplayGameToday(UPDATED_CANPLAY_GAME_TODAY);

        restGamerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGamer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGamer))
            )
            .andExpect(status().isOk());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
        Gamer testGamer = gamerList.get(gamerList.size() - 1);
        assertThat(testGamer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGamer.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testGamer.getVerifyCode()).isEqualTo(UPDATED_VERIFY_CODE);
        assertThat(testGamer.getReferalCode()).isEqualTo(UPDATED_REFERAL_CODE);
        assertThat(testGamer.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testGamer.getCanplayGameToday()).isEqualTo(UPDATED_CANPLAY_GAME_TODAY);
    }

    @Test
    @Transactional
    void putNonExistingGamer() throws Exception {
        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();
        gamer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGamerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gamer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gamer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGamer() throws Exception {
        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();
        gamer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGamerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gamer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGamer() throws Exception {
        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();
        gamer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGamerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gamer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGamerWithPatch() throws Exception {
        // Initialize the database
        gamerRepository.saveAndFlush(gamer);

        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();

        // Update the gamer using partial update
        Gamer partialUpdatedGamer = new Gamer();
        partialUpdatedGamer.setId(gamer.getId());

        partialUpdatedGamer
            .phonenumber(UPDATED_PHONENUMBER)
            .verifyCode(UPDATED_VERIFY_CODE)
            .referalCode(UPDATED_REFERAL_CODE)
            .canplayGameToday(UPDATED_CANPLAY_GAME_TODAY);

        restGamerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGamer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGamer))
            )
            .andExpect(status().isOk());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
        Gamer testGamer = gamerList.get(gamerList.size() - 1);
        assertThat(testGamer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGamer.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testGamer.getVerifyCode()).isEqualTo(UPDATED_VERIFY_CODE);
        assertThat(testGamer.getReferalCode()).isEqualTo(UPDATED_REFERAL_CODE);
        assertThat(testGamer.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testGamer.getCanplayGameToday()).isEqualTo(UPDATED_CANPLAY_GAME_TODAY);
    }

    @Test
    @Transactional
    void fullUpdateGamerWithPatch() throws Exception {
        // Initialize the database
        gamerRepository.saveAndFlush(gamer);

        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();

        // Update the gamer using partial update
        Gamer partialUpdatedGamer = new Gamer();
        partialUpdatedGamer.setId(gamer.getId());

        partialUpdatedGamer
            .name(UPDATED_NAME)
            .phonenumber(UPDATED_PHONENUMBER)
            .verifyCode(UPDATED_VERIFY_CODE)
            .referalCode(UPDATED_REFERAL_CODE)
            .score(UPDATED_SCORE)
            .canplayGameToday(UPDATED_CANPLAY_GAME_TODAY);

        restGamerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGamer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGamer))
            )
            .andExpect(status().isOk());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
        Gamer testGamer = gamerList.get(gamerList.size() - 1);
        assertThat(testGamer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGamer.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testGamer.getVerifyCode()).isEqualTo(UPDATED_VERIFY_CODE);
        assertThat(testGamer.getReferalCode()).isEqualTo(UPDATED_REFERAL_CODE);
        assertThat(testGamer.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testGamer.getCanplayGameToday()).isEqualTo(UPDATED_CANPLAY_GAME_TODAY);
    }

    @Test
    @Transactional
    void patchNonExistingGamer() throws Exception {
        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();
        gamer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGamerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gamer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gamer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGamer() throws Exception {
        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();
        gamer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGamerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gamer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGamer() throws Exception {
        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();
        gamer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGamerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gamer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGamer() throws Exception {
        // Initialize the database
        gamerRepository.saveAndFlush(gamer);

        int databaseSizeBeforeDelete = gamerRepository.findAll().size();

        // Delete the gamer
        restGamerMockMvc
            .perform(delete(ENTITY_API_URL_ID, gamer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
