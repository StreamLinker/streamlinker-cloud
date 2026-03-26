package io.streamlinker.cloud.service;

import io.streamlinker.cloud.infra.db.entity.EdgeNodeEntity;
import io.streamlinker.cloud.infra.db.entity.StreamCatalogEntity;
import io.streamlinker.cloud.infra.db.entity.StreamStatusEntity;
import io.streamlinker.cloud.infra.db.repository.EdgeNodeRepository;
import io.streamlinker.cloud.infra.db.repository.StreamCatalogRepository;
import io.streamlinker.cloud.infra.db.repository.StreamStatusRepository;
import io.streamlinker.cloud.web.dto.PlayInfoView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "streamlinker.zlm.base-url=http://127.0.0.1:8080")
class PlayInfoServiceTest {

    @Autowired
    private PlayInfoService playInfoService;
    @Autowired
    private EdgeNodeRepository edgeNodeRepository;
    @Autowired
    private StreamCatalogRepository streamCatalogRepository;
    @Autowired
    private StreamStatusRepository streamStatusRepository;

    @BeforeEach
    void setUp() {
        streamStatusRepository.deleteAll();
        streamCatalogRepository.deleteAll();
        edgeNodeRepository.deleteAll();

        EdgeNodeEntity edgeNode = new EdgeNodeEntity();
        edgeNode.setEdgeId("edge-1");
        edgeNode.setName("Edge 1");
        edgeNode.setStatus("ONLINE");
        edgeNode.setLastHeartbeatAt(LocalDateTime.now());
        edgeNodeRepository.save(edgeNode);

        StreamCatalogEntity catalog = new StreamCatalogEntity();
        catalog.setStreamId("stream-1");
        catalog.setEdgeId("edge-1");
        catalog.setName("Demo Stream");
        catalog.setCloudApp("live");
        catalog.setCloudStream("demo-1");
        catalog.setEnabled(1);
        streamCatalogRepository.save(catalog);

        StreamStatusEntity status = new StreamStatusEntity();
        status.setStreamId("stream-1");
        status.setEdgeId("edge-1");
        status.setState("RUNNING");
        status.setLocalOnline(1);
        status.setCloudOnline(1);
        status.setUpdatedAt(LocalDateTime.now());
        streamStatusRepository.save(status);
    }

    @Test
    void shouldBuildPlayUrlsForStream() {
        PlayInfoView view = playInfoService.getPlayInfo("stream-1");
        assertThat(view.streamId()).isEqualTo("stream-1");
        assertThat(view.webrtcUrl()).contains("/index/api/webrtc");
        assertThat(view.httpFlvUrl()).isEqualTo("http://127.0.0.1:8080/live/demo-1.live.flv");
        assertThat(view.hlsUrl()).isEqualTo("http://127.0.0.1:8080/live/demo-1/hls.m3u8");
        assertThat(view.cloudOnline()).isTrue();
    }
}
