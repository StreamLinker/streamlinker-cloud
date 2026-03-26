package io.streamlinker.cloud.service;

import io.streamlinker.cloud.web.dto.PlayInfoView;
import io.streamlinker.cloud.web.dto.StreamListItemView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PlayInfoService {

    private final StreamCatalogService streamCatalogService;
    private final String zlmBaseUrl;

    public PlayInfoService(StreamCatalogService streamCatalogService,
                           @Value("${streamlinker.zlm.base-url}") String zlmBaseUrl) {
        this.streamCatalogService = streamCatalogService;
        this.zlmBaseUrl = trimTrailingSlash(zlmBaseUrl);
    }

    public PlayInfoView getPlayInfo(String streamId) {
        StreamListItemView stream = streamCatalogService.getStream(streamId);
        String app = defaultValue(stream.cloudApp(), "live");
        String streamName = defaultValue(stream.cloudStream(), stream.streamId());
        return new PlayInfoView(
                stream.streamId(),
                stream.name(),
                app,
                streamName,
                zlmBaseUrl + "/index/api/webrtc?app=" + app + "&stream=" + streamName + "&type=play",
                zlmBaseUrl + "/" + app + "/" + streamName + ".live.flv",
                zlmBaseUrl + "/" + app + "/" + streamName + "/hls.m3u8",
                stream.localOnline(),
                stream.cloudOnline()
        );
    }

    private String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String defaultValue(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value;
    }
}
