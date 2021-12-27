package com.tachnolife.gamolife.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * The TodayShare entity.\n@author A true Abolfazl
 */
@ApiModel(description = "The TodayShare entity.\n@author A true Abolfazl")
@Entity
@Table(name = "game_share",
    uniqueConstraints = {@UniqueConstraint(name = "gamer_user_idshare_time", columnNames = {"share_time", "gamer_user_id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GameShare implements Serializable {

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
    @Column(name = "share_time", nullable = false)
    private LocalDate shareTime;

    @ManyToOne
    @JsonIgnoreProperties(value = {"user", "playhistories", "gameShares", "inviter", "invtings", "games"}, allowSetters = true)
    private Gamer gamer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameShare id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getMaxPlay() {
        return this.maxPlay;
    }

    public GameShare maxPlay(Integer maxPlay) {
        this.maxPlay = maxPlay;
        return this;
    }

    public void setMaxPlay(Integer maxPlay) {
        this.maxPlay = maxPlay;
    }

    public LocalDate getShareTime() {
        return this.shareTime;
    }

    public GameShare shareTime(LocalDate shareTime) {
        this.shareTime = shareTime;
        return this;
    }


    public void setShareTime(LocalDate shareTime) {
        this.shareTime = shareTime;
    }

    public Gamer getGamer() {
        return this.gamer;
    }

    public GameShare gamer(Gamer gamer) {
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
        if (!(o instanceof GameShare)) {
            return false;
        }
        return id != null && id.equals(((GameShare) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameShare{" +
            "id=" + getId() +
            ", maxPlay=" + getMaxPlay() +
            ", shareTime='" + getShareTime() + "'" +
            "}";
    }
}
