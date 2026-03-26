package io.streamlinker.cloud.web.dto;

import jakarta.validation.constraints.NotBlank;

public record EdgeHeartbeatRequest(
        @NotBlank String edgeId,
        String version
) {
}
