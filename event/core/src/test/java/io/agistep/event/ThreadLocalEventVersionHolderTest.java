package io.agistep.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadLocalEventVersionHolderTest {

    @BeforeEach
    void setUp() {
        Events.clearAll();
    }

    @Test
    void name() {
        assertThat(Events.getLatestVersionOf(1L)).isEqualTo(-1);

        Events.updateVersion(1L,0);
        assertThat(Events.getLatestVersionOf(1L)).isEqualTo(0);

        Events.updateVersion(1L,1);
        assertThat(Events.getLatestVersionOf(1L)).isEqualTo(1);

        Events.clearAll();

        assertThat(Events.getLatestVersionOf(1L)).isEqualTo(-1);

    }
}