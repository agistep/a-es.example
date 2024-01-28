package io.agistep.aggregator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.agistep.aggregator.IdUtils.idOf;
import static org.assertj.core.api.Assertions.assertThat;


class IdUtilsTest {

    final static A A = new A();
    final static B B = new B();

    static class A implements Aggregate {
        long id = 1L;
        @Override
        public long getId() {
            return id;
        }
    }
    static class B implements Aggregate {
        long id;

        @Override
        public long getId() {
            return id;
        }
    }

    @Test
    @DisplayName("Convention Naming of Id Field")
    void idOf1() {
        assertThat(idOf(A)).isEqualTo(A.id);
        assertThat(idOf(B)).isEqualTo(B.id);
    }

    @Test
    void assertThatNotAssignIdOf() {
        assertThat(IdUtils.notAssignedIdOf(A)).isFalse();
        assertThat(IdUtils.notAssignedIdOf(IdUtilsTest.B)).isTrue();
    }
}
