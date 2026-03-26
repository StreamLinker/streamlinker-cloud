# StreamLinker Cloud

[English](README.md) | 简体中文

StreamLinker 的企业级云端流媒体平台。

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![ZLMediaKit](https://img.shields.io/badge/ZLMediaKit-integrated-orange.svg)](https://github.com/ZLMediaKit/ZLMediaKit)
[![Playback](https://img.shields.io/badge/playback-WebRTC%20first-1565C0.svg)](#内置页面)

## 产品定位

StreamLinker Cloud 是 StreamLinker 平台的云端组件。
它负责接收来自 StreamLinker Edge 的流状态与目录信息，聚合云端目录，提供浏览器播放入口，并逐步演进为完整的云端流媒体管理平台。

典型链路：

```text
摄像头 / 无人机
      ↓
StreamLinker Edge（Windows 主机）
  拉流 -> 处理 -> 推流
      ↓
StreamLinker Cloud（服务器）
  接收 -> 编目 -> 播放 -> 运维
      ↓
浏览器控制台 / 第三方系统
```

## 典型场景

| 场景 | 说明 |
| --- | --- |
| 安防运营中心 | 将多个现场视频统一汇聚到云端播放中心 |
| 无人机实时观看 | 为低延迟无人机视频提供浏览器入口 |
| 工地与巡检 | 将分散现场的视频统一接入云端中心 |
| 通用流媒体网关 | 在一个平台里接收、管理、分发直播流 |

## 开源范围

当前开源方向包括：
- Edge 注册、心跳、流状态上报
- 云端流目录持久化
- 为 WebRTC、HTTP-FLV、HLS 生成播放信息
- 内置轻量级主面板和播放器页面
- 通过共享仓库 `streamlinker-zlm-sdk` 与 ZLMediaKit 集成

商业版或后续阶段可能包括：
- 登录鉴权与基于角色的权限控制
- 录像、回放和留存策略
- 更完整的运营后台
- GB28181 和行业协议集成
- 多租户隔离与企业级支持

## 当前实现状态

仓库当前已经包含一套可运行的 V1 骨架，基于：
- Spring Boot 3
- MyBatis-Plus
- MySQL
- `streamlinker-zlm-sdk`
- 内置静态 HTML 页面

当前仓库已实现：
- `edge_node`、`stream_catalog`、`stream_status` 持久化
- Edge 注册、心跳、流状态上报接口
- 流列表、流详情、播放信息接口
- 默认 `WebRTC`、备用 `HTTP-FLV` / `HLS` 的播放器页面
- 用于查看边缘节点和已发布流的主面板
- 基于 H2 的持久化、接口、静态页测试

超出当前快照、计划继续演进的能力包括：
- 录像与回放归档
- 更丰富的管理后台
- 运维诊断与告警视图
- 部署打包与安装指南

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8+
- 可访问的云端 ZLMediaKit
- Maven 3.9+

### 源码启动

```bash
git clone https://github.com/StreamLinker/streamlinker-cloud.git
cd streamlinker-cloud
mvn test
mvn spring-boot:run
```

主配置文件：
- `src/main/resources/application.yml`

数据库建表脚本：
- `src/main/resources/sql/streamlinker-cloud-schema.sql`

### 计划中的交付形态

下面这些交付形式属于目标产品形态，但当前开源快照还没有完全打包好：
- 可直接部署的 release 包
- 容器化部署资产
- 更完整的云端安装和运维指南

## 内置页面

应用启动后可直接访问：
- `/`：Cloud 主面板
- `/player.html`：多协议播放器页面

播放策略：
- 默认协议：`WebRTC`
- 备用协议：`HTTP-FLV`、`HLS`

## 主要接口

面向 Edge 的接口：
- `POST /api/edge/register`
- `POST /api/edge/heartbeat`
- `POST /api/edge/streams/status`
- `GET /api/edge`

面向播放侧的接口：
- `GET /api/streams`
- `GET /api/streams/{streamId}`
- `GET /api/streams/{streamId}/play-info`

## 仓库结构

```text
streamlinker-cloud/
|- src/main/java/io/streamlinker/cloud
|  |- domain
|  |- infra/db
|  |- service
|  `- web
|- src/main/resources
|  |- sql
|  `- static
`- src/test
```

## 与 Edge 的协作关系

这个仓库和 StreamLinker 的边缘侧仓库配套工作。

职责划分：
- `streamlinker-edge`：现场接入、编排、恢复、推流
- `streamlinker-cloud`：中心侧接收、聚合、播放

## 依赖

本项目依赖共享 SDK 仓库：
- [streamlinker-zlm-sdk](https://github.com/StreamLinker/streamlinker-zlm-sdk)

## 路线图

下一步计划：
- 更深入地通过 SDK 查询云端 ZLMediaKit 在线状态
- 提升和 ZLMediaKit 官方 WebRTC JS 方案的兼容性
- 增加更丰富的流诊断和运维视图
- 完成仓库发布和版本流程