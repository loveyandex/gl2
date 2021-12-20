package com.tachnolife.gamolife.web.rest;

import com.tachnolife.gamolife.domain.GameShare;
import com.tachnolife.gamolife.repository.GameShareRepository;
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
 * REST controller for managing {@link com.tachnolife.gamolife.domain.GameShare}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GameShareResource {

    private final Logger log = LoggerFactory.getLogger(GameShareResource.class);

    private static final String ENTITY_NAME = "gameShare";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameShareRepository gameShareRepository;

    public GameShareResource(GameShareRepository gameShareRepository) {
        this.gameShareRepository = gameShareRepository;
    }

    /**
     * {@code POST  /game-shares} : Create a new gameShare.
     *
     * @param gameShare the gameShare to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameShare, or with status {@code 400 (Bad Request)} if the gameShare has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-shares")
    public ResponseEntity<GameShare> createGameShare(@Valid @RequestBody GameShare gameShare) throws URISyntaxException {
        log.debug("REST request to save GameShare : {}", gameShare);
        if (gameShare.getId() != null) {
            throw new BadRequestAlertException("A new gameShare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameShare result = gameShareRepository.save(gameShare);
        return ResponseEntity
            .created(new URI("/api/game-shares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-shares/:id} : Updates an existing gameShare.
     *
     * @param id the id of the gameShare to save.
     * @param gameShare the gameShare to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameShare,
     * or with status {@code 400 (Bad Request)} if the gameShare is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameShare couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-shares/{id}")
    public ResponseEntity<GameShare> updateGameShare(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GameShare gameShare
    ) throws URISyntaxException {
        log.debug("REST request to update GameShare : {}, {}", id, gameShare);
        if (gameShare.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameShare.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameShareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameShare result = gameShareRepository.save(gameShare);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameShare.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-shares/:id} : Partial updates given fields of an existing gameShare, field will ignore if it is null
     *
     * @param id the id of the gameShare to save.
     * @param gameShare the gameShare to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameShare,
     * or with status {@code 400 (Bad Request)} if the gameShare is not valid,
     * or with status {@code 404 (Not Found)} if the gameShare is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameShare couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-shares/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GameShare> partialUpdateGameShare(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GameShare gameShare
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameShare partially : {}, {}", id, gameShare);
        if (gameShare.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameShare.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameShareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameShare> result = gameShareRepository
            .findById(gameShare.getId())
            .map(
                existingGameShare -> {
                    if (gameShare.getMaxPlay() != null) {
                        existingGameShare.setMaxPlay(gameShare.getMaxPlay());
                    }
                    if (gameShare.getShareTime() != null) {
                        existingGameShare.setShareTime(gameShare.getShareTime());
                    }

                    return existingGameShare;
                }
            )
            .map(gameShareRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameShare.getId().toString())
        );
    }

    /**
     * {@code GET  /game-shares} : get all the gameShares.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameShares in body.
     */
    @GetMapping("/game-shares")
    public List<GameShare> getAllGameShares() {
        log.debug("REST request to get all GameShares");
        return gameShareRepository.findAll();
    }

    /**
     * {@code GET  /game-shares/:id} : get the "id" gameShare.
     *
     * @param id the id of the gameShare to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameShare, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-shares/{id}")
    public ResponseEntity<GameShare> getGameShare(@PathVariable Long id) {
        log.debug("REST request to get GameShare : {}", id);
        Optional<GameShare> gameShare = gameShareRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gameShare);
    }

    /**
     * {@code DELETE  /game-shares/:id} : delete the "id" gameShare.
     *
     * @param id the id of the gameShare to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-shares/{id}")
    public ResponseEntity<Void> deleteGameShare(@PathVariable Long id) {
        log.debug("REST request to delete GameShare : {}", id);
        gameShareRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
