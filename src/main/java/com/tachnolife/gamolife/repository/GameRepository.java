package com.tachnolife.gamolife.repository;

import com.tachnolife.gamolife.domain.Game;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Game entity.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query(
        value = "select distinct game from Game game left join fetch game.gamers",
        countQuery = "select count(distinct game) from Game game"
    )
    Page<Game> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct game from Game game left join fetch game.gamers")
    List<Game> findAllWithEagerRelationships();

    @Query("select game from Game game left join fetch game.gamers where game.id =:id")
    Optional<Game> findOneWithEagerRelationships(@Param("id") Long id);






}
