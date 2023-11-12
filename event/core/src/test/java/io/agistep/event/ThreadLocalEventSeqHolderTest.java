package io.agistep.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadLocalEventSeqHolderTest {

    @BeforeEach
    void setUp() {
        Events.clearAll();
    }

    @Test
    void name() {
        assertThat(Events.getLatestSeqOf(1L)).isEqualTo(-1);

        ThreadLocalEventSeqHolder.instance().setSeq(1L,0);
        assertThat(Events.getLatestSeqOf(1L)).isEqualTo(0);

        ThreadLocalEventSeqHolder.instance().setSeq(1L,1);
        assertThat(Events.getLatestSeqOf(1L)).isEqualTo(1);

        Events.clearAll();

        assertThat(Events.getLatestSeqOf(1L)).isEqualTo(-1);

    }
}