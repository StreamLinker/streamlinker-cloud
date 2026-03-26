# StreamLinker Cloud

English | [简体中文](README.zh-CN.md)

Enterprise-grade cloud streaming platform for StreamLinker.

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![ZLMediaKit](https://img.shields.io/badge/ZLMediaKit-integrated-orange.svg)](https://github.com/ZLMediaKit/ZLMediaKit)
[![Playback](https://img.shields.io/badge/playback-WebRTC%20first-1565C0.svg)](#built-in-web-pages)

## Product positioning

StreamLinker Cloud is the cloud-side component of the StreamLinker platform.
It is responsible for receiving stream metadata from StreamLinker Edge, aggregating catalogs, exposing browser playback, and evolving toward a full cloud streaming management platform.

Typical pipeline:

```text
Camera / Drone
      ↓
StreamLinker Edge (Windows host)
  pull -> process -> push
      ↓
StreamLinker Cloud (server)
  receive -> catalog -> play -> operate
      ↓
Browser console / third-party systems
```

## Typical scenarios

| Scenario | Description |
| --- | --- |
| Security operations | Aggregate multiple site streams into one cloud playback center |
| Drone live viewing | Offer a browser-side entry for low-latency drone video |
| Construction and inspection | Centralize distributed site streams into one cloud hub |
| Generic streaming gateway | Receive, manage, and distribute live streams in one platform |

## Open-source scope

Current open-source direction includes:
- edge registration, heartbeat, and stream status reporting
- cloud-side stream catalog persistence
- playback info generation for WebRTC, HTTP-FLV, and HLS
- built-in lightweight dashboard and player pages
- integration with the shared `streamlinker-zlm-sdk`

Commercial or later-stage capabilities may include:
- authentication and role-based access control
- recording, playback archive, and retention policies
- richer operational dashboards
- GB28181 and industry protocol integration
- multi-tenant isolation and enterprise support

## Current implementation status

The repository already contains a working V1 skeleton based on:
- Spring Boot 3
- MyBatis-Plus
- MySQL
- `streamlinker-zlm-sdk`
- built-in static HTML pages

Already implemented in this repository:
- `edge_node`, `stream_catalog`, and `stream_status` persistence
- Edge-side APIs for register, heartbeat, and stream status reporting
- cloud-side APIs for stream list, stream detail, and play info
- WebRTC-first player page with HTTP-FLV and HLS fallback tabs
- static dashboard page for edge nodes and published streams
- H2-backed test suite for persistence, APIs, and static pages

Planned platform capabilities beyond the current snapshot:
- recording and playback archive
- richer management console
- operator diagnostics and alert views
- deployment packaging and installation guides

## Quick start

### Environment requirements

- JDK 17+
- MySQL 8+
- reachable cloud-side ZLMediaKit
- Maven 3.9+

### Source run

```bash
git clone https://github.com/StreamLinker/streamlinker-cloud.git
cd streamlinker-cloud
mvn test
mvn spring-boot:run
```

Main configuration file:
- `src/main/resources/application.yml`

Database schema:
- `src/main/resources/sql/streamlinker-cloud-schema.sql`

### Planned packaging modes

The following delivery modes are part of the target product shape, but are not fully packaged in the current open-source snapshot yet:
- release package for direct deployment
- containerized deployment assets
- richer cloud installation and operations guides

## Built-in web pages

After the application starts, these pages are available:
- `/` : cloud dashboard
- `/player.html` : multi-protocol player page

Playback strategy:
- default protocol: `WebRTC`
- fallback protocols: `HTTP-FLV`, `HLS`

## Main APIs

Edge-facing APIs:
- `POST /api/edge/register`
- `POST /api/edge/heartbeat`
- `POST /api/edge/streams/status`
- `GET /api/edge`

Playback APIs:
- `GET /api/streams`
- `GET /api/streams/{streamId}`
- `GET /api/streams/{streamId}/play-info`

## Repository layout

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

## Working with Edge

This repository is designed to work together with the StreamLinker edge side.

Role split:
- `streamlinker-edge`: on-site intake, orchestration, recovery, and push
- `streamlinker-cloud`: centralized receiving, aggregation, and playback

## Dependency

This project depends on the shared SDK repository:
- [streamlinker-zlm-sdk](https://github.com/StreamLinker/streamlinker-zlm-sdk)

## Roadmap

Planned next steps:
- query cloud-side ZLMediaKit online state through the SDK in more depth
- improve compatibility with the official ZLMediaKit WebRTC JS stack
- add richer stream diagnostics and operator views
- finish repository publishing and release flow