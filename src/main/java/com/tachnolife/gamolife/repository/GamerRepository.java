package com.tachnolife.gamolife.repository;

import com.tachnolife.gamolife.domain.Gamer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Gamer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GamerRepository extends JpaRepository<Gamer, Long> {


     Optional<Gamer> findByPhonenumber(String aLong);


}
