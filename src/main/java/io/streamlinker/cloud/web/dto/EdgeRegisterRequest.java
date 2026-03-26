package io.streamlinker.cloud.web.dto;

import jakarta.validation.constraints.NotBlank;

public record EdgeRegisterRequest(
        @NotBlank String edgeId,
        @NotBlank String name,
        String version,
        String remark
) {
}
