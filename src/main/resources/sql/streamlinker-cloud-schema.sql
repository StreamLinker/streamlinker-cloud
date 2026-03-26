CREATE TABLE IF NOT EXISTS edge_node (
    edge_id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    version VARCHAR(64) NULL,
    last_heartbeat_at DATETIME NULL,
    remark VARCHAR(255) NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS stream_catalog (
    stream_id VARCHAR(64) PRIMARY KEY,
    edge_id VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL,
    access_mode VARCHAR(32) NULL,
    cloud_app VARCHAR(64) NULL,
    cloud_stream VARCHAR(128) NULL,
    enabled TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255) NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_stream_catalog_edge FOREIGN KEY (edge_id) REFERENCES edge_node(edge_id)
);

CREATE TABLE IF NOT EXISTS stream_status (
    stream_id VARCHAR(64) PRIMARY KEY,
    edge_id VARCHAR(64) NOT NULL,
    state VARCHAR(32) NOT NULL,
    local_online TINYINT NOT NULL DEFAULT 0,
    cloud_online TINYINT NOT NULL DEFAULT 0,
    last_error VARCHAR(255) NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_stream_status_edge FOREIGN KEY (edge_id) REFERENCES edge_node(edge_id)
);