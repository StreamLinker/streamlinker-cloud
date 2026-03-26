package io.streamlinker.cloud.infra.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("edge_node")
public class EdgeNodeEntity {

    @TableId
    private String edgeId;
    private String name;
    private String status;
    private String version;
    private LocalDateTime lastHeartbeatAt;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}