package com.tachnolife.gamolife.web.rest;

import com.tachnolife.gamolife.domain.PlayHistory;
import com.tachnolife.gamolife.repository.PlayHistoryRepository;
import com.tachnolife.gamolife.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tachnolife.gamolife.domain.PlayHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlayHistoryResource {

    private final Logger log = LoggerFactory.getLogger(PlayHistoryResource.class);

    private static final String ENTITY_NAME = "playHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayHistoryRepository playHistoryRepository;

    public PlayHistoryResource(PlayHistoryRepository playHistoryRepository) {
        this.playHistoryRepository = playHistoryRepository;
    }

    /**
     * {@code POST  /play-histories} : Create a new playHistory.
     *
     * @param playHistory the playHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playHistory, or with status {@code 400 (Bad Request)} if the playHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/play-histories")
    public ResponseEntity<PlayHistory> createPlayHistory(@Valid @RequestBody PlayHistory playHistory) throws URISyntaxException {
        log.debug("REST request to save PlayHistory : {}", playHistory);
        if (playHistory.getId() != null) {
            throw new BadRequestAlertException("A new playHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayHistory result = playHistoryRepository.save(playHistory);
        return ResponseEntity
            .created(new URI("/api/play-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /play-histories/:id} : Updates an existing playHistory.
     *
     * @param id the id of the playHistory to save.
     * @param playHistory the playHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playHistory,
     * or with status {@code 400 (Bad Request)} if the playHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/play-histories/{id}")
    public ResponseEntity<PlayHistory> updatePlayHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlayHistory playHistory
    ) throws URISyntaxException {
        log.debug("REST request to update PlayHistory : {}, {}", id, playHistory);
        if (playHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlayHistory result = playHistoryRepository.save(playHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /play-histories/:id} : Partial updates given fields of an existing playHistory, field will ignore if it is null
     *
     * @param id the id of the playHistory to save.
     * @param playHistory the playHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playHistory,
     * or with status {@code 400 (Bad Request)} if the playHistory is not valid,
     * or with status {@code 404 (Not Found)} if the playHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the playHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/play-histories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PlayHistory> partialUpdatePlayHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlayHistory playHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlayHistory partially : {}, {}", id, playHistory);
        if (playHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayHistory> result = playHistoryRepository
            .findById(playHistory.getId())
            .map(
                existingPlayHistory -> {
                    if (playHistory.getMaxPlay() != null) {
                        existingPlayHistory.setMaxPlay(playHistory.getMaxPlay());
                    }
                    if (playHistory.getDatePlays() != null) {
                        existingPlayHistory.setDatePlays(playHistory.getDatePlays());
                    }
                    if (playHistory.getPlayDate() != null) {
                        existingPlayHistory.setPlayDate(playHistory.getPlayDate());
                    }

                    return existingPlayHistory;
                }
            )
            .map(playHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /play-histories} : get all the playHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playHistories in body.
     */
    @GetMapping("/play-histories")
    public List<PlayHistory> getAllPlayHistories() {
        log.debug("REST request to get all PlayHistories");
        return playHistoryRepository.findAll();
    }

    /**
     * {@code GET  /play-histories/:id} : get the "id" playHistory.
     *
     * @param id the id of the playHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/play-histories/{id}")
    public ResponseEntity<PlayHistory> getPlayHistory(@PathVariable Long id) {
        log.debug("REST request to get PlayHistory : {}", id);
        Optional<PlayHistory> playHistory = playHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(playHistory);
    }

    /**
     * {@code DELETE  /play-histories/:id} : delete the "id" playHistory.
     *
     * @param id the id of the playHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/play-histories/{id}")
    public ResponseEntity<Void> deletePlayHistory(@PathVariable Long id) {
        log.debug("REST request to delete PlayHistory : {}", id);
        playHistoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
