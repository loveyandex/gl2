package com.tachnolife.gamolife.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tachnolife.gamolife.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameShareTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameShare.class);
        GameShare gameShare1 = new GameShare();
        gameShare1.setId(1L);
        GameShare gameShare2 = new GameShare();
        gameShare2.setId(gameShare1.getId());
        assertThat(gameShare1).isEqualTo(gameShare2);
        gameShare2.setId(2L);
        assertThat(gameShare1).isNotEqualTo(gameShare2);
        gameShare1.setId(null);
        assertThat(gameShare1).isNotEqualTo(gameShare2);
    }
}
