package com.tachnolife.gamolife.web.rest;

import com.tachnolife.gamolife.cunsome.SMSClient;
import com.tachnolife.gamolife.domain.Authority;
import com.tachnolife.gamolife.domain.Gamer;
import com.tachnolife.gamolife.domain.User;
import com.tachnolife.gamolife.repository.AuthorityRepository;
import com.tachnolife.gamolife.repository.GamerRepository;
import com.tachnolife.gamolife.repository.UserRepository;
import com.tachnolife.gamolife.security.AuthoritiesConstants;
import com.tachnolife.gamolife.security.jwt.JWTFilter;
import com.tachnolife.gamolife.security.jwt.TokenProvider;
import com.tachnolife.gamolife.web.rest.errors.BadRequestAlertException;
import com.tachnolife.gamolife.web.rest.payload.ApiResponse;
import com.tachnolife.gamolife.web.rest.payload.GSup;
import com.tachnolife.gamolife.web.rest.payload.RestResponse;
import com.tachnolife.gamolife.web.rest.vm.LoginVM;

import net.bytebuddy.utility.RandomString;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import tech.jhipster.security.RandomUtil;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.*;

/**
 * REST controller for managing {@link com.tachnolife.gamolife.domain.Gamer}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@CrossOrigin(origins = "http://localhost:9090")
public class GamerResource {

    private final Logger log = LoggerFactory.getLogger(GamerResource.class);

    private static final String ENTITY_NAME = "gamer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private GamerRepository gamerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;


    /**
     * {@code POST  /gamers} : Create a new gamer.
     *
     * @param gamer the gamer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gamer, or with status {@code 400 (Bad Request)} if the gamer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gamers")
    public ResponseEntity<Gamer> createGamer(@Valid @RequestBody Gamer gamer) throws URISyntaxException {
        log.debug("REST request to save Gamer : {}", gamer);
        if (gamer.getId() != null) {
            throw new BadRequestAlertException("A new gamer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = userRepository.findById(gamer.getUser().getId()).orElse(null);

        gamer.setUser(user);

        Gamer result = gamerRepository.save(gamer);
        return ResponseEntity
            .created(new URI("/api/gamers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gamers/:id} : Updates an existing gamer.
     *
     * @param id    the id of the gamer to save.
     * @param gamer the gamer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gamer,
     * or with status {@code 400 (Bad Request)} if the gamer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gamer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gamers/{id}")
    public ResponseEntity<Gamer> updateGamer(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Gamer gamer)
        throws URISyntaxException {
        log.debug("REST request to update Gamer : {}, {}", id, gamer);
        if (gamer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gamer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gamerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Gamer result = gamerRepository.save(gamer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gamer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gamers/:id} : Partial updates given fields of an existing gamer, field will ignore if it is null
     *
     * @param id    the id of the gamer to save.
     * @param gamer the gamer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gamer,
     * or with status {@code 400 (Bad Request)} if the gamer is not valid,
     * or with status {@code 404 (Not Found)} if the gamer is not found,
     * or with status {@code 500 (Internal Server Error)} if the gamer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gamers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Gamer> partialUpdateGamer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Gamer gamer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gamer partially : {}, {}", id, gamer);
        if (gamer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gamer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gamerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Gamer> result = gamerRepository
            .findById(gamer.getId())
            .map(
                existingGamer -> {
                    if (gamer.getName() != null) {
                        existingGamer.setName(gamer.getName());
                    }
                    if (gamer.getPhonenumber() != null) {
                        existingGamer.setPhonenumber(gamer.getPhonenumber());
                    }
                    if (gamer.getVerifyCode() != null) {
                        existingGamer.setVerifyCode(gamer.getVerifyCode());
                    }
                    if (gamer.getReferalCode() != null) {
                        existingGamer.setReferalCode(gamer.getReferalCode());
                    }
                    if (gamer.getScore() != null) {
                        existingGamer.setScore(gamer.getScore());
                    }
                    if (gamer.getCanplayGameToday() != null) {
                        existingGamer.setCanplayGameToday(gamer.getCanplayGameToday());
                    }

                    return existingGamer;
                }
            )
            .map(gamerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gamer.getId().toString())
        );
    }

    /**
     * {@code GET  /gamers} : get all the gamers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gamers in body.
     */
    @GetMapping("/gamers")
    public List<Gamer> getAllGamers() {
        log.debug("REST request to get all Gamers");
        return gamerRepository.findAll();
    }

    /**
     * {@code GET  /gamers/:id} : get the "id" gamer.
     *
     * @param id the id of the gamer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gamer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gamers/{id}")
    public ResponseEntity<Gamer> getGamer(@PathVariable Long id) {
        log.debug("REST request to get Gamer : {}", id);
        Optional<Gamer> gamer = gamerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gamer);
    }

    /**
     * {@code DELETE  /gamers/:id} : delete the "id" gamer.
     *
     * @param id the id of the gamer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gamers/{id}")
    public ResponseEntity<Void> deleteGamer(@PathVariable Long id) {
        log.debug("REST request to delete Gamer : {}", id);
        gamerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


    @PostMapping("/gamers/authenticate")
    public ResponseEntity<UserJWTController.JWTToken> authoddrize(@Valid @RequestBody LoginVM loginVM) {

        Gamer user = gamerRepository.findByPhonenumber(loginVM.getUsername()).orElseThrow(() -> {
            throw new RuntimeException("there is no user such login");
        });


        if (user.getVerifyCode() == null)
            return new ResponseEntity(new RestResponse<>((false), (101), ("there is no code sent as sms otp try to get one"), null), HttpStatus.OK);


        if (user.getVerifyCode().equals(loginVM.getPassword()) && loginVM.getPassword() != null && !loginVM.getPassword().isEmpty()) {
            String jwt = tokenProvider.createTokenForGamer(user.getPhonenumber(), loginVM.isRememberMe());
            user.setVerifyCode(null);

            HttpHeaders httpHeaders = new HttpHeaders();


            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new UserJWTController.JWTToken(jwt), httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity(new RestResponse<>((false), (101), ("wrong code"), null), HttpStatus.OK);

        }


    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private SMSClient smsClient;


    @Autowired
    private OkHttpClient client;


    @PostMapping("/gamers/register")
    public ResponseEntity<?> authoddr(@RequestBody GSup gamerSignUp) throws IOException {


//        try {
//            HashMap<String, Object> params = new HashMap<>();
//            params.put("username", "09122131691");
//            params.put("password", "Yazdan990");
//            params.put("message", gamerSignUp.phoneNumber);
//            params.put("numbers", gamerSignUp.phoneNumber);
//            params.put("sendernumber", 50004307L);
//            params.put("sendtype", 1);
//            smsClient.sendPtoPsms(params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        Optional<Gamer> byPhonenumber = gamerRepository
            .findByPhonenumber(gamerSignUp.phoneNumber);

        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        String make = String.valueOf(n);

        String refcode = RandomString.make(6);
        if (byPhonenumber.isPresent()) {

            log.debug("ispresent ");
            Gamer gamer = byPhonenumber.get();
            gamer.verifyCode(make);

            sendSms(gamerSignUp.phoneNumber, make);

            ApiResponse ok = new ApiResponse("ok");
            return new ResponseEntity<ApiResponse>(ok, HttpStatus.OK);
        } else {
            User s = new User();
            s.setActivated(true);
            s.setLogin(gamerSignUp.phoneNumber);
            s.setEmail(gamerSignUp.phoneNumber);
            // new user is not active
            s.setActivated(false);
            // new user gets registration key
            s.setActivationKey(RandomUtil.generateActivationKey());
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById(AuthoritiesConstants.GAMER).ifPresent(authorities::add);
            s.setAuthorities(authorities);


            User save = userRepository.save(s);
            Gamer save1 = gamerRepository
                .save(new Gamer()
                    .referalCode(refcode)
                    .user(save)
                    .canplayGameToday(true)
                    .maxCanPlay(1)
                    .phonenumber(gamerSignUp.phoneNumber)
                );

            sendSmsAndWait(gamerSignUp.phoneNumber, make);
            ApiResponse ok = new ApiResponse(make);
            return new ResponseEntity<ApiResponse>(ok, HttpStatus.OK);
        }
    }


    private void sendSms(String phoneNumber, String code) {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String msg = "Your code is " + code;

        okhttp3.RequestBody body = okhttp3.RequestBody
            .create(mediaType, String.format("username=09122131691&password=Yazdan990&message=%s&numbers=%s&sendernumber=50004307&sendtype=1",
                msg, phoneNumber));

        Request request = new Request.Builder()
            .url("https://niksms.com/fa/publicapi/groupsms")
//            .url("https://example.com")
            .method("POST", body)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("Cookie", "Localization=fa-IR")
            .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                log.debug(e.toString());
                // Error
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String res = response.body().string();
                log.debug(res);

                // Do something with the response
            }
        });
    }

    private void sendSmsAndWait(String phoneNumber, String code) throws IOException {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String msg = "Your code is " + code;

        okhttp3.RequestBody body = okhttp3.RequestBody
            .create(mediaType, String.format("username=09122131691&password=Yazdan990&message=%s&numbers=%s&sendernumber=50004307&sendtype=1",
                msg, phoneNumber));

        Request request = new Request.Builder()
            .url("https://niksms.com/fa/publicapi/groupsms")
            .method("POST", body)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("Cookie", "Localization=fa-IR")
            .build();

        Response execute = client.newCall(request).execute();


    }

    @GetMapping("/gamers/me")
//    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity getUser(Principal principal) {
        String phone = principal.getName();
        Gamer gamer = gamerRepository.findByPhonenumber(phone).orElse(null);

        return new ResponseEntity(gamer, HttpStatus.OK);


    }


    @PatchMapping("/gamers/reduceMaxPlay")
//    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<?> reduceMaxPlay(Principal principal) {
        String phone = principal.getName();
        Gamer gamer = gamerRepository.findByPhonenumber(phone).orElse(null);
        if (gamer == null) {
            return null;
        }
        gamer.maxCanPlay(gamer.getMaxCanPlay() - 1);

        return new ResponseEntity(gamerRepository.save(gamer), HttpStatus.OK);

    }


    @PatchMapping("/gamers/increaseMaxPlay")
//    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<?> increaseMaxPlay(Principal principal) {
        String phone = principal.getName();
        Gamer gamer = gamerRepository.findByPhonenumber(phone).orElse(null);
        if (gamer == null) {
            return null;
        }
        gamer.maxCanPlay(gamer.getMaxCanPlay() + 1);

        return new ResponseEntity(gamerRepository.save(gamer), HttpStatus.OK);

    }


}
