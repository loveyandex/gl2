package com.tachnolife.gamolife.repository;

import com.tachnolife.gamolife.domain.GameShare;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data SQL repository for the GameShare entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameShareRepository extends JpaRepository<GameShare, Long> {

    Optional<GameShare> findGameShareByGamer_PhonenumberAndShareTime(String phone, Instant instant);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO game_share(max_play, share_time, gamer_user_id) " +
        "SELECT 1, :date, user_id FROM gamer WHERE phonenumber =:phone ON DUPLICATE KEY UPDATE max_play =max_play+1", nativeQuery = true)
    void addAndupdateGameShareMaxPLayer(@Param(value = "phone") String phone, @Param(value = "date") LocalDate date);


}
