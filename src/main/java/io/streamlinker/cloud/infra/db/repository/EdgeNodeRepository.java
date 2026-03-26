package io.streamlinker.cloud.infra.db.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.streamlinker.cloud.infra.db.entity.EdgeNodeEntity;
import io.streamlinker.cloud.infra.db.mapper.EdgeNodeMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EdgeNodeRepository {

    private final EdgeNodeMapper edgeNodeMapper;

    public EdgeNodeRepository(EdgeNodeMapper edgeNodeMapper) {
        this.edgeNodeMapper = edgeNodeMapper;
    }

    public EdgeNodeEntity findByEdgeId(String edgeId) {
        return edgeNodeMapper.selectById(edgeId);
    }

    public EdgeNodeEntity save(EdgeNodeEntity entity) {
        if (findByEdgeId(entity.getEdgeId()) == null) {
            edgeNodeMapper.insert(entity);
            return entity;
        }
        edgeNodeMapper.updateById(entity);
        return entity;
    }

    public List<EdgeNodeEntity> findAll() {
        return edgeNodeMapper.selectList(new LambdaQueryWrapper<>());
    }

    public void deleteAll() {
        edgeNodeMapper.delete(new LambdaQueryWrapper<>());
    }
}