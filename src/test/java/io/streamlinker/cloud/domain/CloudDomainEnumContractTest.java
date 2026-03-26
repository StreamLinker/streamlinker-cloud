package io.streamlinker.cloud.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CloudDomainEnumContractTest {

    @Test
    void shouldExposeExpectedEnumValues() {
        assertEquals(EdgeNodeStatus.ONLINE, EdgeNodeStatus.valueOf("ONLINE"));
        assertEquals(StreamState.RUNNING, StreamState.valueOf("RUNNING"));
    }
}