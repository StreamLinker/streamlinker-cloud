package io.streamlinker.cloud.service;

import io.streamlinker.cloud.domain.EdgeNodeStatus;
import io.streamlinker.cloud.infra.db.entity.EdgeNodeEntity;
import io.streamlinker.cloud.infra.db.repository.EdgeNodeRepository;
import io.streamlinker.cloud.web.dto.EdgeHeartbeatRequest;
import io.streamlinker.cloud.web.dto.EdgeNodeView;
import io.streamlinker.cloud.web.dto.EdgeRegisterRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class EdgeRegistryService {

    private final EdgeNodeRepository edgeNodeRepository;

    public EdgeRegistryService(EdgeNodeRepository edgeNodeRepository) {
        this.edgeNodeRepository = edgeNodeRepository;
    }

    public EdgeNodeView register(EdgeRegisterRequest request) {
        EdgeNodeEntity entity = edgeNodeRepository.findByEdgeId(request.edgeId());
        if (entity == null) {
            entity = new EdgeNodeEntity();
            entity.setEdgeId(request.edgeId());
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setName(request.name());
        entity.setVersion(request.version());
        entity.setRemark(request.remark());
        entity.setStatus(EdgeNodeStatus.ONLINE.name());
        entity.setLastHeartbeatAt(now);
        edgeNodeRepository.save(entity);
        return toView(entity);
    }

    public EdgeNodeView heartbeat(EdgeHeartbeatRequest request) {
        EdgeNodeEntity entity = edgeNodeRepository.findByEdgeId(request.edgeId());
        if (entity == null) {
            throw new IllegalArgumentException("Edge node not registered: " + request.edgeId());
        }
        entity.setStatus(EdgeNodeStatus.ONLINE.name());
        entity.setVersion(request.version());
        entity.setLastHeartbeatAt(LocalDateTime.now());
        edgeNodeRepository.save(entity);
        return toView(entity);
    }

    public List<EdgeNodeView> list() {
        return edgeNodeRepository.findAll().stream()
                .sorted(Comparator.comparing(EdgeNodeEntity::getEdgeId))
                .map(this::toView)
                .toList();
    }

    private EdgeNodeView toView(EdgeNodeEntity entity) {
        return new EdgeNodeView(
                entity.getEdgeId(),
                entity.getName(),
                entity.getStatus(),
                entity.getVersion(),
                entity.getRemark(),
                entity.getLastHeartbeatAt()
        );
    }
}
