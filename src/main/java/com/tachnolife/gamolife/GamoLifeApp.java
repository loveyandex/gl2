package com.tachnolife.gamolife;

import com.tachnolife.gamolife.config.ApplicationProperties;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.annotation.PostConstruct;

import com.tachnolife.gamolife.domain.Authority;
import com.tachnolife.gamolife.domain.User;
import com.tachnolife.gamolife.repository.AuthorityRepository;
import com.tachnolife.gamolife.repository.UserRepository;
import com.tachnolife.gamolife.security.AuthoritiesConstants;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.security.RandomUtil;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
@EnableFeignClients
public class GamoLifeApp {
    @Bean
     OkHttpClient client(){
        return new OkHttpClient().newBuilder().build();
    }

    private static final Logger log = LoggerFactory.getLogger(GamoLifeApp.class);

    private final Environment env;

    public GamoLifeApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes GamoLife.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;


    @PostConstruct
    public void initUser() {

        if (userRepository.findOneByLogin("admin").isPresent())
            return;

        User newUser = new User();

        String encryptedPassword = passwordEncoder.encode("admin");
        newUser.setLogin("admin");

        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);


        // new user is not active
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();

        authorityRepository
            .findById(AuthoritiesConstants.ADMIN)
            .ifPresentOrElse(
                authorities::add,
                () -> {
                    Authority s = new Authority(AuthoritiesConstants.ADMIN);
                    authorities.add(s);
                    authorityRepository.save(s);
                }
            );

        authorityRepository
            .findById(AuthoritiesConstants.USER)
            .ifPresentOrElse(
                authorities::add,
                () -> {
                    Authority s = new Authority(AuthoritiesConstants.USER);
                    authorities.add(s);
                    authorityRepository.save(s);
                }
            );        newUser.setAuthorities(authorities);
        userRepository.save(newUser);

    }



    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GamoLifeApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
        log.info("god is here forever 17 feb");
    }

    @RequestMapping("/")
    public String index(){
        System.out.println("Looking in the index controller.........");
        return "index";
    }



    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            "\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles()
        );
    }



}
