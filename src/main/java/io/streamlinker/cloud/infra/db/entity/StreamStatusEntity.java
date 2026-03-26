package io.streamlinker.cloud.infra.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("stream_status")
public class StreamStatusEntity {

    @TableId
    private String streamId;
    private String edgeId;
    private String state;
    private Integer localOnline;
    private Integer cloudOnline;
    private String lastError;
    private LocalDateTime updatedAt;
}