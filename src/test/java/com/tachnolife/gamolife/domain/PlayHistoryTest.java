package com.tachnolife.gamolife.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tachnolife.gamolife.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayHistory.class);
        PlayHistory playHistory1 = new PlayHistory();
        playHistory1.setId(1L);
        PlayHistory playHistory2 = new PlayHistory();
        playHistory2.setId(playHistory1.getId());
        assertThat(playHistory1).isEqualTo(playHistory2);
        playHistory2.setId(2L);
        assertThat(playHistory1).isNotEqualTo(playHistory2);
        playHistory1.setId(null);
        assertThat(playHistory1).isNotEqualTo(playHistory2);
    }
}
