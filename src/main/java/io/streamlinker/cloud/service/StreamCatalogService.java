package io.streamlinker.cloud.service;

import io.streamlinker.cloud.infra.db.entity.StreamCatalogEntity;
import io.streamlinker.cloud.infra.db.entity.StreamStatusEntity;
import io.streamlinker.cloud.infra.db.repository.StreamCatalogRepository;
import io.streamlinker.cloud.infra.db.repository.StreamStatusRepository;
import io.streamlinker.cloud.web.dto.StreamListItemView;
import io.streamlinker.cloud.web.dto.StreamStatusReportItem;
import io.streamlinker.cloud.web.dto.StreamStatusReportRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StreamCatalogService {

    private final StreamCatalogRepository streamCatalogRepository;
    private final StreamStatusRepository streamStatusRepository;

    public StreamCatalogService(StreamCatalogRepository streamCatalogRepository,
                                StreamStatusRepository streamStatusRepository) {
        this.streamCatalogRepository = streamCatalogRepository;
        this.streamStatusRepository = streamStatusRepository;
    }

    public int report(String edgeId, StreamStatusReportRequest request) {
        for (StreamStatusReportItem item : request.streams()) {
            StreamCatalogEntity catalog = streamCatalogRepository.findByStreamId(item.streamId());
            if (catalog == null) {
                catalog = new StreamCatalogEntity();
                catalog.setStreamId(item.streamId());
            }
            catalog.setEdgeId(edgeId);
            catalog.setName(item.name());
            catalog.setAccessMode(item.accessMode());
            catalog.setCloudApp(item.cloudApp());
            catalog.setCloudStream(item.cloudStream());
            catalog.setEnabled(item.enabled() == null ? 1 : item.enabled());
            streamCatalogRepository.save(catalog);

            StreamStatusEntity status = streamStatusRepository.findByStreamId(item.streamId());
            if (status == null) {
                status = new StreamStatusEntity();
                status.setStreamId(item.streamId());
            }
            status.setEdgeId(edgeId);
            status.setState(item.state());
            status.setLocalOnline(item.localOnline() == null ? 0 : item.localOnline());
            status.setCloudOnline(item.cloudOnline() == null ? 0 : item.cloudOnline());
            status.setLastError(item.lastError());
            status.setUpdatedAt(LocalDateTime.now());
            streamStatusRepository.save(status);
        }
        return request.streams().size();
    }

    public List<StreamListItemView> listStreams() {
        Map<String, StreamStatusEntity> statusMap = streamStatusRepository.findAll().stream()
                .collect(Collectors.toMap(StreamStatusEntity::getStreamId, Function.identity()));
        return streamCatalogRepository.findAll().stream()
                .sorted(Comparator.comparing(StreamCatalogEntity::getStreamId))
                .map(catalog -> toView(catalog, statusMap.get(catalog.getStreamId())))
                .toList();
    }

    public StreamListItemView getStream(String streamId) {
        StreamCatalogEntity catalog = streamCatalogRepository.findByStreamId(streamId);
        if (catalog == null) {
            throw new IllegalArgumentException("Stream not found: " + streamId);
        }
        return toView(catalog, streamStatusRepository.findByStreamId(streamId));
    }

    private StreamListItemView toView(StreamCatalogEntity catalog, StreamStatusEntity status) {
        return new StreamListItemView(
                catalog.getStreamId(),
                catalog.getEdgeId(),
                catalog.getName(),
                catalog.getAccessMode(),
                catalog.getCloudApp(),
                catalog.getCloudStream(),
                catalog.getEnabled() != null && catalog.getEnabled() == 1,
                status == null ? "UNKNOWN" : status.getState(),
                status != null && status.getLocalOnline() != null && status.getLocalOnline() == 1,
                status != null && status.getCloudOnline() != null && status.getCloudOnline() == 1,
                status == null ? null : status.getLastError()
        );
    }
}
