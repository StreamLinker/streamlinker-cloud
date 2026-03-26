package io.streamlinker.cloud.infra.db.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.streamlinker.cloud.infra.db.entity.StreamCatalogEntity;
import io.streamlinker.cloud.infra.db.mapper.StreamCatalogMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StreamCatalogRepository {

    private final StreamCatalogMapper streamCatalogMapper;

    public StreamCatalogRepository(StreamCatalogMapper streamCatalogMapper) {
        this.streamCatalogMapper = streamCatalogMapper;
    }

    public StreamCatalogEntity findByStreamId(String streamId) {
        return streamCatalogMapper.selectById(streamId);
    }

    public StreamCatalogEntity save(StreamCatalogEntity entity) {
        if (findByStreamId(entity.getStreamId()) == null) {
            streamCatalogMapper.insert(entity);
            return entity;
        }
        streamCatalogMapper.updateById(entity);
        return entity;
    }

    public List<StreamCatalogEntity> findAll() {
        return streamCatalogMapper.selectList(new LambdaQueryWrapper<>());
    }

    public void deleteAll() {
        streamCatalogMapper.delete(new LambdaQueryWrapper<>());
    }
}