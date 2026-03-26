package io.streamlinker.cloud.infra.db.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:cloud_mapper;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.sql.init.mode=always",
        "spring.sql.init.schema-locations=classpath:sql/streamlinker-cloud-schema-h2.sql"
})
class CloudMapperContextTest {

    @Autowired
    private EdgeNodeMapper edgeNodeMapper;

    @Autowired
    private StreamCatalogMapper streamCatalogMapper;

    @Autowired
    private StreamStatusMapper streamStatusMapper;

    @Test
    void shouldLoadCloudMappers() {
        assertNotNull(edgeNodeMapper);
        assertNotNull(streamCatalogMapper);
        assertNotNull(streamStatusMapper);
    }
}