package io.agistep.event.serialization;

import io.agistep.event.Serializer;
import io.agistep.event.storages.MapEventStorage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NoOpSerializerTest {

    MapEventStorage sut;

    @Test
    void name2() {
        // TODO 다른 시리얼라이져 넣기
        // Object > Json > byte[]
        // 저장하는 형식과 변환(json, protobuf) 형식은 다른
        // 설정하는거


    }

}