package io.streamlinker.cloud.web.dto;

import jakarta.validation.constraints.NotBlank;

public record StreamStatusReportItem(
        @NotBlank String streamId,
        @NotBlank String name,
        String accessMode,
        String cloudApp,
        String cloudStream,
        Integer enabled,
        @NotBlank String state,
        Integer localOnline,
        Integer cloudOnline,
        String lastError
) {
}
