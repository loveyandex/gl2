package com.tachnolife.gamolife.web.rest;

import com.tachnolife.gamolife.domain.Gamer;
import com.tachnolife.gamolife.repository.GameShareRepository;
import com.tachnolife.gamolife.repository.GamerRepository;
import com.tachnolife.gamolife.repository.UserRepository;
import com.tachnolife.gamolife.security.jwt.TokenProvider;
import com.tachnolife.gamolife.web.rest.payload.GSup;
import com.tachnolife.gamolife.web.rest.payload.UpdateScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

/**
 * @author Abolfazl
 */
@RestController
@RequestMapping("/api")
@Transactional
@CrossOrigin(origins = "http://localhost:9090")
public class GamePolicyController {


    private final Logger log = LoggerFactory.getLogger(GamePolicyController.class);

    @Autowired
    private GamerRepository gamerRepository;
    @Autowired
    private GameShareRepository gameShareRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;


    @PatchMapping("/gamers/score/add")
//    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<?> score(@RequestBody UpdateScore updateScore, Principal principal) {
        String phone = principal.getName();
        gamerRepository.updatePhone(phone, Long.valueOf(updateScore.score));
        return new ResponseEntity<>(null, null, HttpStatus.OK);

    }


    @PatchMapping("/gamers/canplay/add")
//    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<?> canplay(@RequestBody UpdateScore updateScore, Principal principal) {
        String phone = principal.getName();
        gamerRepository.addScore(phone, Long.valueOf(updateScore.score));

        return new ResponseEntity<>(null, null, HttpStatus.OK);

    }


    @PutMapping("/gamers/gameshares")
//    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<?> gameshares(@RequestBody UpdateScore updateScore, Principal principal) {
        String phone = principal.getName();

//        gameShareRepository.findGameShareByGamer_PhonenumberAndShareTime()
        gameShareRepository.addAndupdateGameShareMaxPLayer(phone, LocalDate.now());

        return new ResponseEntity<>(null, null, HttpStatus.OK);

    }







}
