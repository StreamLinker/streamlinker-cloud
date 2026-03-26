package io.streamlinker.cloud.infra.db.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.streamlinker.cloud.infra.db.entity.StreamStatusEntity;
import io.streamlinker.cloud.infra.db.mapper.StreamStatusMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StreamStatusRepository {

    private final StreamStatusMapper streamStatusMapper;

    public StreamStatusRepository(StreamStatusMapper streamStatusMapper) {
        this.streamStatusMapper = streamStatusMapper;
    }

    public StreamStatusEntity findByStreamId(String streamId) {
        return streamStatusMapper.selectById(streamId);
    }

    public StreamStatusEntity save(StreamStatusEntity entity) {
        if (findByStreamId(entity.getStreamId()) == null) {
            streamStatusMapper.insert(entity);
            return entity;
        }
        streamStatusMapper.updateById(entity);
        return entity;
    }

    public List<StreamStatusEntity> findAll() {
        return streamStatusMapper.selectList(new LambdaQueryWrapper<>());
    }

    public void deleteAll() {
        streamStatusMapper.delete(new LambdaQueryWrapper<>());
    }
}