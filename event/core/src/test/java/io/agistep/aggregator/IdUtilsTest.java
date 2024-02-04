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
    final static C C = new C();
    final static D D = new D();
    final static E E = new E();
    final static F F = new F();
    final static G G = new G();
    final static H H = new H();
    final static I I = new I();

    static class A { @AggregateId long orderId = 1L; }
    static class B { long id = 1L;}

    static class C { Long id = 2L;}
    static class D { }
    static class E { BigDecimal id;}

    static class F { long id;}
    static class G { @AggregateId long orderId; }

    static class H {
        @AggregateId long orderId = 1L;
        long id = 3L;
    }

    static class I extends H { }

    @Test
    @DisplayName("Exception: Illegal AggregateId")
    void idOf0() {
        assertThatThrownBy(()->idOf(C))
                .isInstanceOf(IllegalAggregateIdException.class)
                .hasMessage("An aggregate id should be primitive long type.");

        assertThatThrownBy(()->idOf(D))
                .isInstanceOf(IllegalAggregateIdException.class)
                .hasMessage("Aggregate Must Have 'id' field or have @AggregateId annotation.");

        assertThatThrownBy(()->idOf(E))
                .isInstanceOf(IllegalAggregateIdException.class)
                .hasMessage("An aggregate id should be primitive long type.");

        assertThatThrownBy(()->idOf(F))
                .isInstanceOf(IllegalAggregateIdException.class)
                .hasMessage("An Aggregate id must not be 0(zero). :%s", F.getClass().getName());

        assertThatThrownBy(()->idOf(G))
                .isInstanceOf(IllegalAggregateIdException.class)
                .hasMessage("An Aggregate id must not be 0(zero). :%s", G.getClass().getName());
    }

    @Test
    @DisplayName("Convention assign of Id Field: should be name of 'id' or have @AggregateId annotation")
    void idOf1() {
        assertThat(idOf(A)).isEqualTo(A.orderId);
        assertThat(idOf(B)).isEqualTo(B.id);
        assertThat(idOf(I)).isEqualTo(H.orderId);
    }

    @Test
    @DisplayName("If id field and @AggregateId annotation both exist, @AggregateId annotation is used")
    void idOf2() {
        assertThat(idOf(H)).isEqualTo(H.orderId);
    }

    @Test
    void assertThatNotAssignIdOf() {
        assertThat(IdUtils.notAssignedIdOf(A)).isFalse();
        assertThat(IdUtils.notAssignedIdOf(B)).isFalse();

        assertThat(IdUtils.notAssignedIdOf(F)).isTrue();
        assertThat(IdUtils.notAssignedIdOf(G)).isTrue();
    }
}
