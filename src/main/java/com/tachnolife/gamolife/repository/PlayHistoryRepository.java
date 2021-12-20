package com.tachnolife.gamolife.repository;

import com.tachnolife.gamolife.domain.PlayHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlayHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {}
