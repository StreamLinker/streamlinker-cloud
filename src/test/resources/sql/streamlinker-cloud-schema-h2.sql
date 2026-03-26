CREATE TABLE edge_node (
    edge_id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    version VARCHAR(64),
    last_heartbeat_at TIMESTAMP,
    remark VARCHAR(255),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stream_catalog (
    stream_id VARCHAR(64) PRIMARY KEY,
    edge_id VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL,
    access_mode VARCHAR(32),
    cloud_app VARCHAR(64),
    cloud_stream VARCHAR(128),
    enabled TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stream_status (
    stream_id VARCHAR(64) PRIMARY KEY,
    edge_id VARCHAR(64) NOT NULL,
    state VARCHAR(32) NOT NULL,
    local_online TINYINT NOT NULL DEFAULT 0,
    cloud_online TINYINT NOT NULL DEFAULT 0,
    last_error VARCHAR(255),
    updated_at TIMESTAMP NOT NULL
);