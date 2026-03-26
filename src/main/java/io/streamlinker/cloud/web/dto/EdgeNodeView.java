package io.streamlinker.cloud.web.dto;

import java.time.LocalDateTime;

public record EdgeNodeView(
        String edgeId,
        String name,
        String status,
        String version,
        String remark,
        LocalDateTime lastHeartbeatAt
) {
}
