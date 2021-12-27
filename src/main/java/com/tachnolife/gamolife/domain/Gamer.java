package com.tachnolife.gamolife.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Gamer entity.\n@author A true Abolfazl
 */
@ApiModel(description = "The Gamer entity.\n@author A true Abolfazl")
@Entity
@Table(name = "gamer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Gamer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "phonenumber", nullable = false, unique = true)
    private String phonenumber;

    @Column(name = "verify_code")
    private String verifyCode;

    @Column(name = "referal_code")
    private String referalCode;

    @Column(name = "score")
    private Long score;

    @Column(name = "canplay_game_today")
    private Boolean canplayGameToday;


    @Column(name = "max_can_play")
    private long maxCanPlay;


    @OneToOne
    @MapsId
    private User user;

    @OneToMany(mappedBy = "gamer",fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"gamer"} )
    private Set<PlayHistory> playhistories = new HashSet<>();

    @OneToMany(mappedBy = "gamer",fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"gamer"}, allowSetters = true)
    private Set<GameShare> gameShares = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"user", "playhistories", "gameShares", "inviter", "invtings", "games"}, allowSetters = true)
    private Gamer inviter;

    @OneToMany(mappedBy = "inviter",fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"user", "playhistories", "gameShares", "inviter", "invtings", "games"}, allowSetters = true)
    private Set<Gamer> invtings = new HashSet<>();

    @ManyToMany(mappedBy = "gamers",fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"gamers"}, allowSetters = true)
    private Set<Game> games = new HashSet<>();




    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gamer id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Gamer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public Gamer phonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getVerifyCode() {
        return this.verifyCode;
    }

    public Gamer verifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
        return this;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getReferalCode() {
        return this.referalCode;
    }

    public Gamer referalCode(String referalCode) {
        this.referalCode = referalCode;
        return this;
    }

    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }

    public Long getScore() {
        return this.score;
    }

    public Gamer score(Long score) {
        this.score = score;
        return this;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Boolean getCanplayGameToday() {
        return this.canplayGameToday;
    }

    public Gamer canplayGameToday(Boolean canplayGameToday) {
        this.canplayGameToday = canplayGameToday;
        return this;
    }
    public Gamer maxCanPlay(long maxCanPlay) {
        this.maxCanPlay = maxCanPlay;
        return this;
    }

    public long getMaxCanPlay() {
        return this.maxCanPlay;
    }

    public void setCanplayGameToday(Boolean canplayGameToday) {
        this.canplayGameToday = canplayGameToday;
    }

    public User getUser() {
        return this.user;
    }

    public Gamer user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<PlayHistory> getPlayhistories() {
        return this.playhistories;
    }

    public Gamer playhistories(Set<PlayHistory> playHistories) {
        this.setPlayhistories(playHistories);
        return this;
    }

    public Gamer addPlayhistory(PlayHistory playHistory) {
        this.playhistories.add(playHistory);
        playHistory.setGamer(this);
        return this;
    }

    public Gamer removePlayhistory(PlayHistory playHistory) {
        this.playhistories.remove(playHistory);
        playHistory.setGamer(null);
        return this;
    }

    public void setPlayhistories(Set<PlayHistory> playHistories) {
        if (this.playhistories != null) {
            this.playhistories.forEach(i -> i.setGamer(null));
        }
        if (playHistories != null) {
            playHistories.forEach(i -> i.setGamer(this));
        }
        this.playhistories = playHistories;
    }

    public Set<GameShare> getGameShares() {
        return this.gameShares;
    }

    public Gamer gameShares(Set<GameShare> gameShares) {
        this.setGameShares(gameShares);
        return this;
    }

    public Gamer addGameShare(GameShare gameShare) {
        this.gameShares.add(gameShare);
        gameShare.setGamer(this);
        return this;
    }

    public Gamer removeGameShare(GameShare gameShare) {
        this.gameShares.remove(gameShare);
        gameShare.setGamer(null);
        return this;
    }

    public void setGameShares(Set<GameShare> gameShares) {
        if (this.gameShares != null) {
            this.gameShares.forEach(i -> i.setGamer(null));
        }
        if (gameShares != null) {
            gameShares.forEach(i -> i.setGamer(this));
        }
        this.gameShares = gameShares;
    }

    public Gamer getInviter() {
        return this.inviter;
    }

    public Gamer inviter(Gamer gamer) {
        this.setInviter(gamer);
        return this;
    }

    public void setInviter(Gamer gamer) {
        this.inviter = gamer;
    }

    public Set<Gamer> getInvtings() {
        return this.invtings;
    }

    public Gamer invtings(Set<Gamer> gamers) {
        this.setInvtings(gamers);
        return this;
    }

    public Gamer addInvting(Gamer gamer) {
        this.invtings.add(gamer);
        gamer.setInviter(this);
        return this;
    }

    public Gamer removeInvting(Gamer gamer) {
        this.invtings.remove(gamer);
        gamer.setInviter(null);
        return this;
    }

    public void setInvtings(Set<Gamer> gamers) {
        if (this.invtings != null) {
            this.invtings.forEach(i -> i.setInviter(null));
        }
        if (gamers != null) {
            gamers.forEach(i -> i.setInviter(this));
        }
        this.invtings = gamers;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public Gamer games(Set<Game> games) {
        this.setGames(games);
        return this;
    }

    public Gamer addGame(Game game) {
        this.games.add(game);
        game.getGamers().add(this);
        return this;
    }

    public Gamer removeGame(Game game) {
        this.games.remove(game);
        game.getGamers().remove(this);
        return this;
    }

    public void setGames(Set<Game> games) {
        if (this.games != null) {
            this.games.forEach(i -> i.removeGamer(this));
        }
        if (games != null) {
            games.forEach(i -> i.addGamer(this));
        }
        this.games = games;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gamer)) {
            return false;
        }
        return id != null && id.equals(((Gamer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gamer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phonenumber='" + getPhonenumber() + "'" +
            ", verifyCode='" + getVerifyCode() + "'" +
            ", referalCode='" + getReferalCode() + "'" +
            ", score=" + getScore() +
            ", canplayGameToday='" + getCanplayGameToday() + "'" +
            "}";
    }
}
