package io.agistep.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadLocalEventSeqHolderTest {

    @BeforeEach
    void setUp() {
        EventSource.clearAll();
    }

    @Test
    void name() {
        assertThat(EventSource.getLatestSeqOf(1L)).isEqualTo(-1);

        ThreadLocalEventSeqHolder.instance().setSeq(1L,0);
        assertThat(EventSource.getLatestSeqOf(1L)).isEqualTo(0);

        ThreadLocalEventSeqHolder.instance().setSeq(1L,1);
        assertThat(EventSource.getLatestSeqOf(1L)).isEqualTo(1);

        EventSource.clearAll();

        assertThat(EventSource.getLatestSeqOf(1L)).isEqualTo(-1);

    }
}