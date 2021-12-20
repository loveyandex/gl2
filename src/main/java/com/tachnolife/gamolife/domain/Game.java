package com.tachnolife.gamolife.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Game entity.\n@author A true Abolfazl
 */
@ApiModel(description = "The Game entity.\n@author A true Abolfazl")
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * name
     */
    @ApiModelProperty(value = "name")
    @Column(name = "name")
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "rel_game__gamer", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "gamer_id"))
    @JsonIgnoreProperties(value = { "user", "playhistories", "gameShares", "inviter", "invtings", "games" }, allowSetters = true)
    private Set<Gamer> gamers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Game name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Gamer> getGamers() {
        return this.gamers;
    }

    public Game gamers(Set<Gamer> gamers) {
        this.setGamers(gamers);
        return this;
    }

    public Game addGamer(Gamer gamer) {
        this.gamers.add(gamer);
        gamer.getGames().add(this);
        return this;
    }

    public Game removeGamer(Gamer gamer) {
        this.gamers.remove(gamer);
        gamer.getGames().remove(this);
        return this;
    }

    public void setGamers(Set<Gamer> gamers) {
        this.gamers = gamers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
