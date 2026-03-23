<div align="center">


# 🌐 StreamLinker Cloud

**企业级流媒体管理平台 · 服务端**

基于 ZLMediaKit 二次开发，提供流媒体接入、管理、播放的完整云端解决方案

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue3](https://img.shields.io/badge/Vue-3.x-42b883.svg)](https://vuejs.org/)
[![ZLMediaKit](https://img.shields.io/badge/ZLMediaKit-latest-orange.svg)](https://github.com/ZLMediaKit/ZLMediaKit)
[![Docker](https://img.shields.io/badge/Docker-supported-2496ED.svg)](https://www.docker.com/)

[English](README_EN.md) · 简体中文 · [在线 Demo](#) · [定制开发](#联系我们)

</div>

---

## ✨ 项目简介

StreamLinker Cloud 是 [StreamLinker](https://github.com/StreamLinker) 的**服务端组件**，负责接收来自 Edge 端推送的视频流，提供统一的流媒体管理、实时播放、录像回放和设备监控能力。

与 [StreamLinker Edge](https://github.com/StreamLinker/streamlinker-edge) 配合使用，构成完整的边缘→云端流媒体链路。

```
摄像头 / 无人机
      ↓
StreamLinker Edge（Windows 工控机）
  拉流 → 转码 → 推流
      ↓
StreamLinker Cloud（服务器）
  接收 → 管理 → 播放 → 录像
      ↓
Web 管理界面 / 第三方系统
```

---

## 🎯 适用场景

| 场景         | 说明                           |
| ------------ | ------------------------------ |
| 🔒 安防监控   | 多路摄像头统一接入、管理、回放 |
| 🚁 无人机图传 | 实时图传流接收与显示           |
| 🏗️ 工地巡检   | 工地多点位视频汇聚与管理       |
| 📡 通用直播   | 直播推流接入与分发             |

---

## 🖥️ 功能特性

### 开源版

- ✅ 流媒体接入管理（RTSP / RTMP / HLS / WebRTC）
- ✅ 实时播放（Web 端 H5 播放器）
- ✅ 基础录像与回放
- ✅ 设备列表管理
- ✅ 流状态监控
- ✅ 用户权限管理

### 商业版 *(即将推出)*

- 🔐 多租户隔离
- 📡 GB28181 完整对接
- 🤖 AI 分析接口集成
- 📊 运维监控面板
- 🔔 告警通知（邮件/钉钉/企微）
- 🛡️ 商业技术支持 SLA

---

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- Docker & Docker Compose（推荐）
- ZLMediaKit（已内置配置）

### 方式一：Docker Compose 一键启动（推荐）

```bash
git clone https://github.com/StreamLinker/streamlinker-cloud.git
cd streamlinker-cloud
docker-compose up -d
```

访问 `http://localhost:8080`，默认账号 `admin / admin123`

### 方式二：本地开发启动

```bash
# 后端
cd backend
cp src/main/resources/application.yml.example src/main/resources/application.yml
mvn spring-boot:run

# 前端
cd frontend
npm install
npm run dev
```

---

## 📁 项目结构

```
streamlinker-cloud/
├── backend/                  # Spring Boot 后端
│   ├── src/main/java/
│   │   └── com/streamlinker/
│   │       ├── controller/   # API 接口
│   │       ├── service/      # 业务逻辑
│   │       ├── zlmedia/      # ZLMediaKit 集成
│   │       └── config/       # 配置类
│   └── pom.xml
├── frontend/                 # Vue3 前端
│   ├── src/
│   │   ├── views/            # 页面
│   │   ├── components/       # 组件
│   │   └── api/              # 接口
│   └── package.json
├── docker-compose.yml
└── README.md
```

---

## 📡 与 Edge 端配合

本项目需要配合 [StreamLinker Edge](https://github.com/StreamLinker/streamlinker-edge) 使用：

- **Edge 端**：部署在 Windows 工控机，负责拉取摄像头/无人机流并推送到 Cloud
- **Cloud 端**：部署在服务器，统一接收、管理、分发视频流

---

## 🤝 定制开发 & 商业合作

如果您有以下需求，欢迎联系：

- 🔧 私有化部署定制
- 🔌 硬件设备协议对接（摄像头 / 无人机 / GB28181）
- 🏗️ 行业解决方案定制（安防 / 工地 / 巡检）

📧 邮箱：`your@email.com`  
💬 微信：`your_wechat`

---

## 📄 开源协议

本项目采用 [MIT License](LICENSE) 开源，商业使用请遵守协议。

---

<div align="center">


如果这个项目对你有帮助，请点一个 ⭐ Star，这是对我最大的支持！

</div>
