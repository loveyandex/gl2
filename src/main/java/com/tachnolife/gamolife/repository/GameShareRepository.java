package com.tachnolife.gamolife.repository;

import com.tachnolife.gamolife.domain.GameShare;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GameShare entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameShareRepository extends JpaRepository<GameShare, Long> {}
