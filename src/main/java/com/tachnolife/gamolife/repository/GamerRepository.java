package com.tachnolife.gamolife.repository;

import com.tachnolife.gamolife.domain.Gamer;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Gamer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GamerRepository extends JpaRepository<Gamer, Long> {


     Optional<Gamer> findByPhonenumber(String phone);

    @Modifying
    @Query("update Gamer u set u.score = :score where u.phonenumber = :phone")
    void updatePhone(@Param(value = "phone") String phone, @Param(value = "score") Long score);


    @Modifying
    @Transactional
    @Query("update Gamer u set u.score = u.score +:score where u.phonenumber = :phone")
    void addScore(@Param(value = "phone") String phone, @Param(value = "score") Long score);




}
