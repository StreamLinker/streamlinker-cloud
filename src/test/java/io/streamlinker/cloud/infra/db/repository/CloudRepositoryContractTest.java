package io.streamlinker.cloud.infra.db.repository;

import io.streamlinker.cloud.infra.db.entity.EdgeNodeEntity;
import io.streamlinker.cloud.infra.db.entity.StreamCatalogEntity;
import io.streamlinker.cloud.infra.db.entity.StreamStatusEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:cloud_repo;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.sql.init.mode=always",
        "spring.sql.init.schema-locations=classpath:sql/streamlinker-cloud-schema-h2.sql"
})
class CloudRepositoryContractTest {

    @Autowired
    private EdgeNodeRepository edgeNodeRepository;

    @Autowired
    private StreamCatalogRepository streamCatalogRepository;

    @Autowired
    private StreamStatusRepository streamStatusRepository;

    @AfterEach
    void tearDown() {
        streamStatusRepository.deleteAll();
        streamCatalogRepository.deleteAll();
        edgeNodeRepository.deleteAll();
    }

    @Test
    void shouldSaveAndQueryCloudEntities() {
        EdgeNodeEntity edgeNode = new EdgeNodeEntity();
        edgeNode.setEdgeId("edge-001");
        edgeNode.setName("Edge 001");
        edgeNode.setStatus("ONLINE");
        edgeNode.setCreateTime(LocalDateTime.now());
        edgeNode.setUpdateTime(LocalDateTime.now());
        edgeNodeRepository.save(edgeNode);

        StreamCatalogEntity catalog = new StreamCatalogEntity();
        catalog.setStreamId("stream-001");
        catalog.setEdgeId("edge-001");
        catalog.setName("Camera 001");
        catalog.setAccessMode("PROXY");
        catalog.setCloudApp("live");
        catalog.setCloudStream("camera-001");
        catalog.setEnabled(1);
        catalog.setCreateTime(LocalDateTime.now());
        catalog.setUpdateTime(LocalDateTime.now());
        streamCatalogRepository.save(catalog);

        StreamStatusEntity status = new StreamStatusEntity();
        status.setStreamId("stream-001");
        status.setEdgeId("edge-001");
        status.setState("RUNNING");
        status.setLocalOnline(1);
        status.setCloudOnline(1);
        status.setUpdatedAt(LocalDateTime.now());
        streamStatusRepository.save(status);

        assertNotNull(edgeNodeRepository.findByEdgeId("edge-001"));
        assertEquals("Camera 001", streamCatalogRepository.findByStreamId("stream-001").getName());
        assertEquals("RUNNING", streamStatusRepository.findByStreamId("stream-001").getState());
    }
}