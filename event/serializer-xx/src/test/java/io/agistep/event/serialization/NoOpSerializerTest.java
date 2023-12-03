package io.agistep.event.serialization;

import io.agistep.event.Serializer;
import io.agistep.event.storages.MapEventStorage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoOpSerializerTest {

    MapEventStorage sut;

    @Test
    void name() {
        sut = new MapEventStorage();
        Serializer serializer = sut.getSerializer();
        assertThat(serializer).isInstanceOf(NoOpSerializer.class);
        //NoOp 은 무엇인가? 기존의 것을 그대로 넘기는 것은 무엇을 의미하는가?
        // 아무것도 하지 않는다. Serialization 을 하지 않는다.
        // 저장/불러오기를 어떻게 테스트하지??


    }

    @Test
    void name2() {
        // TODO 다른 시리얼라이져 넣기
        // Object > Json > byte[]
        // 저장하는 형식과 변환(json, protobuf) 형식은 다른
        // 설정하는거


    }

}