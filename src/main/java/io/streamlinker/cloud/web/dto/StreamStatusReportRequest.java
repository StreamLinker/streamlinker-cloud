package io.streamlinker.cloud.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record StreamStatusReportRequest(
        @NotBlank String edgeId,
        @Valid @NotEmpty List<StreamStatusReportItem> streams
) {
}
