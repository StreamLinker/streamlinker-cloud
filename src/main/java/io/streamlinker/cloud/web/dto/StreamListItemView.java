package io.streamlinker.cloud.web.dto;

public record StreamListItemView(
        String streamId,
        String edgeId,
        String name,
        String accessMode,
        String cloudApp,
        String cloudStream,
        boolean enabled,
        String state,
        boolean localOnline,
        boolean cloudOnline,
        String lastError
) {
}
