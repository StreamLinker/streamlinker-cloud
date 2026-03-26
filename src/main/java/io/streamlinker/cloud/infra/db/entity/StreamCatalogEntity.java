package io.streamlinker.cloud.infra.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("stream_catalog")
public class StreamCatalogEntity {

    @TableId
    private String streamId;
    private String edgeId;
    private String name;
    private String accessMode;
    private String cloudApp;
    private String cloudStream;
    private Integer enabled;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}