package io.agistep.aggregator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.agistep.aggregator.IdUtils.idOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class IdUtilsTest {

    final static A A = new A();
    final static B B = new B();
    final static F F = new F();
    final static G G = new G();
    final static H H = new H();
    final static I I = new I();

    static class A { long id = 1L;}
    static class B { Long id = 2L;}
    static class F { }
    static class G { BigDecimal id;}
    static class H { long id;}
    static class I { Long id;}

    @Test
    @DisplayName("Exception: Illegal AggregateId")
    void idOf0() {
        assertThatThrownBy(()->idOf(F))
                .isInstanceOf(IllegalAggregateIdException.class)
                .hasMessage("Aggregate Must Have 'id' field. :%s", F.getClass().getName());

        assertThatThrownBy(()->idOf(G))
                .isInstanceOf(IllegalAggregateIdException.class)
                .hasMessage(
                        "An ID field applied should be one of the following types: " +
                        "long, Long. :%s", G.getClass().getName());

        assertThatThrownBy(()->idOf(H))
                .isInstanceOf(IllegalAggregateIdException.class)
                .hasMessage("Primitive Type Int and long must not have 0(zero). :%s", H.getClass().getName());

        assertThatThrownBy(()->idOf(I))
                .isInstanceOf(IllegalAggregateIdException.class)
                .hasMessage("An Id must not be null. :%s", I.getClass().getName());
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
        assertThat(IdUtils.notAssignedIdOf(H)).isTrue();
        assertThat(IdUtils.notAssignedIdOf(I)).isTrue();
    }
}
