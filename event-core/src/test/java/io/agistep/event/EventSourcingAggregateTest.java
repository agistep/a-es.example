package io.agistep.event;

import io.agistep.identity.Identity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class EventSourcingAggregateTest {

    static public class Bar {

        Identity<Long> id;


        public Bar() {
            BarCreated payload = new BarCreated();
            EventApplier.instance().apply(this, payload);
        }

        @EventHandler(payload = BarCreated.class)
        void onCreated(Event anEvent) {
            this.id = anEvent::getAggregateIdValue;
        }

        private static class BarCreated {
        }
    }

    @Test
    void name() {
        Bar aggregate = new Bar();
        assertThat(aggregate.id.getValue()).isEqualTo(0L);
    }

}
