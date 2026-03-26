package io.streamlinker.cloud.web.dto;

public record PlayInfoView(
        String streamId,
        String streamName,
        String app,
        String stream,
        String webrtcUrl,
        String httpFlvUrl,
        String hlsUrl,
        boolean localOnline,
        boolean cloudOnline
) {
}
