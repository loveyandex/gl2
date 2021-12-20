package com.tachnolife.gamolife.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tachnolife.gamolife.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GamerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gamer.class);
        Gamer gamer1 = new Gamer();
        gamer1.setId(1L);
        Gamer gamer2 = new Gamer();
        gamer2.setId(gamer1.getId());
        assertThat(gamer1).isEqualTo(gamer2);
        gamer2.setId(2L);
        assertThat(gamer1).isNotEqualTo(gamer2);
        gamer1.setId(null);
        assertThat(gamer1).isNotEqualTo(gamer2);
    }
}
