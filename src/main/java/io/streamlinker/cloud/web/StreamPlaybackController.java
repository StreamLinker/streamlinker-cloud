package io.streamlinker.cloud.web;

import io.streamlinker.cloud.service.PlayInfoService;
import io.streamlinker.cloud.service.StreamCatalogService;
import io.streamlinker.cloud.web.dto.PlayInfoView;
import io.streamlinker.cloud.web.dto.StreamListItemView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/streams")
public class StreamPlaybackController {

    private final StreamCatalogService streamCatalogService;
    private final PlayInfoService playInfoService;

    public StreamPlaybackController(StreamCatalogService streamCatalogService,
                                    PlayInfoService playInfoService) {
        this.streamCatalogService = streamCatalogService;
        this.playInfoService = playInfoService;
    }

    @GetMapping
    public List<StreamListItemView> list() {
        return streamCatalogService.listStreams();
    }

    @GetMapping("/{streamId}")
    public StreamListItemView get(@PathVariable String streamId) {
        return streamCatalogService.getStream(streamId);
    }

    @GetMapping("/{streamId}/play-info")
    public PlayInfoView getPlayInfo(@PathVariable String streamId) {
        return playInfoService.getPlayInfo(streamId);
    }
}
