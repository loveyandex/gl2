package com.tachnolife.gamolife.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The GameCondition entity.\n@author A true Abolfazl
 */
@ApiModel(description = "The GameCondition entity.\n@author A true Abolfazl")
@Entity
@Table(name = "play_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlayHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * maxPlay
     */
    @ApiModelProperty(value = "maxPlay")
    @Column(name = "max_play")
    private Integer maxPlay;

    @NotNull
    @Column(name = "date_plays", nullable = false)
    private Integer datePlays;

    @NotNull
    @Column(name = "play_date", nullable = false, unique = true)
    private LocalDate playDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "user", "playhistories", "gameShares", "inviter", "invtings", "games" }, allowSetters = true)
    private Gamer gamer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayHistory id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getMaxPlay() {
        return this.maxPlay;
    }

    public PlayHistory maxPlay(Integer maxPlay) {
        this.maxPlay = maxPlay;
        return this;
    }

    public void setMaxPlay(Integer maxPlay) {
        this.maxPlay = maxPlay;
    }

    public Integer getDatePlays() {
        return this.datePlays;
    }

    public PlayHistory datePlays(Integer datePlays) {
        this.datePlays = datePlays;
        return this;
    }

    public void setDatePlays(Integer datePlays) {
        this.datePlays = datePlays;
    }

    public LocalDate getPlayDate() {
        return this.playDate;
    }

    public PlayHistory playDate(LocalDate playDate) {
        this.playDate = playDate;
        return this;
    }

    public void setPlayDate(LocalDate playDate) {
        this.playDate = playDate;
    }

    public Gamer getGamer() {
        return this.gamer;
    }

    public PlayHistory gamer(Gamer gamer) {
        this.setGamer(gamer);
        return this;
    }

    public void setGamer(Gamer gamer) {
        this.gamer = gamer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayHistory)) {
            return false;
        }
        return id != null && id.equals(((PlayHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayHistory{" +
            "id=" + getId() +
            ", maxPlay=" + getMaxPlay() +
            ", datePlays=" + getDatePlays() +
            ", playDate='" + getPlayDate() + "'" +
            "}";
    }
}
