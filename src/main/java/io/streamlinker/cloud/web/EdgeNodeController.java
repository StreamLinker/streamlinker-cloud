package io.streamlinker.cloud.web;

import io.streamlinker.cloud.service.EdgeRegistryService;
import io.streamlinker.cloud.service.StreamCatalogService;
import io.streamlinker.cloud.web.dto.EdgeHeartbeatRequest;
import io.streamlinker.cloud.web.dto.EdgeNodeView;
import io.streamlinker.cloud.web.dto.EdgeRegisterRequest;
import io.streamlinker.cloud.web.dto.StreamStatusReportRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/edge")
public class EdgeNodeController {

    private final EdgeRegistryService edgeRegistryService;
    private final StreamCatalogService streamCatalogService;

    public EdgeNodeController(EdgeRegistryService edgeRegistryService,
                              StreamCatalogService streamCatalogService) {
        this.edgeRegistryService = edgeRegistryService;
        this.streamCatalogService = streamCatalogService;
    }

    @PostMapping("/register")
    public EdgeNodeView register(@Valid @RequestBody EdgeRegisterRequest request) {
        return edgeRegistryService.register(request);
    }

    @PostMapping("/heartbeat")
    public EdgeNodeView heartbeat(@Valid @RequestBody EdgeHeartbeatRequest request) {
        return edgeRegistryService.heartbeat(request);
    }

    @PostMapping("/streams/status")
    public Map<String, Object> reportStatus(@Valid @RequestBody StreamStatusReportRequest request) {
        int updated = streamCatalogService.report(request.edgeId(), request);
        return Map.of("edgeId", request.edgeId(), "updatedCount", updated);
    }

    @GetMapping
    public List<EdgeNodeView> list() {
        return edgeRegistryService.list();
    }
}
