const streamListEl = document.getElementById('playerStreamList');
const titleEl = document.getElementById('playerTitle');
const messageEl = document.getElementById('playerMessage');
const videoEl = document.getElementById('videoPlayer');
const playUrlEl = document.getElementById('playUrl');
const refreshButton = document.getElementById('refreshPlayerData');
const tabs = Array.from(document.querySelectorAll('.tab'));

let currentProtocol = 'webrtc';
let currentStreamId = new URLSearchParams(window.location.search).get('streamId');
let currentPlayInfo = null;
let flvPlayer = null;
let hlsPlayer = null;
let rtcPeer = null;

async function fetchJson(url, options = {}) {
  const response = await fetch(url, options);
  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`);
  }
  return response.json();
}

function setMessage(text) {
  messageEl.textContent = text;
  messageEl.style.display = 'grid';
}

function hideMessage() {
  messageEl.style.display = 'none';
}

function resetPlayers() {
  if (flvPlayer) {
    flvPlayer.destroy();
    flvPlayer = null;
  }
  if (hlsPlayer) {
    hlsPlayer.destroy();
    hlsPlayer = null;
  }
  if (rtcPeer) {
    rtcPeer.getSenders().forEach((sender) => sender.track && sender.track.stop());
    rtcPeer.close();
    rtcPeer = null;
  }
  videoEl.pause();
  videoEl.removeAttribute('src');
  videoEl.srcObject = null;
  videoEl.load();
}

async function waitForIceGathering(peer) {
  if (peer.iceGatheringState === 'complete') {
    return;
  }
  await new Promise((resolve) => {
    const timeout = window.setTimeout(resolve, 1500);
    peer.addEventListener('icegatheringstatechange', () => {
      if (peer.iceGatheringState === 'complete') {
        clearTimeout(timeout);
        resolve();
      }
    });
  });
}

async function playWebRtc() {
  resetPlayers();
  setMessage('Negotiating WebRTC session...');
  rtcPeer = new RTCPeerConnection();
  rtcPeer.addTransceiver('audio', { direction: 'recvonly' });
  rtcPeer.addTransceiver('video', { direction: 'recvonly' });
  rtcPeer.ontrack = (event) => {
    videoEl.srcObject = event.streams[0];
    videoEl.muted = false;
    videoEl.play().catch(() => undefined);
    hideMessage();
  };
  const offer = await rtcPeer.createOffer();
  await rtcPeer.setLocalDescription(offer);
  await waitForIceGathering(rtcPeer);

  const response = await fetch(currentPlayInfo.webrtcUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/plain;charset=UTF-8'
    },
    body: rtcPeer.localDescription.sdp
  });
  if (!response.ok) {
    throw new Error(`WebRTC negotiation failed: ${response.status}`);
  }
  const contentType = response.headers.get('content-type') || '';
  let answerSdp;
  if (contentType.includes('application/json')) {
    const payload = await response.json();
    answerSdp = payload.sdp || payload.data?.sdp || payload.result?.sdp;
  } else {
    answerSdp = await response.text();
  }
  if (!answerSdp) {
    throw new Error('WebRTC answer SDP is empty');
  }
  await rtcPeer.setRemoteDescription({ type: 'answer', sdp: answerSdp });
  playUrlEl.textContent = currentPlayInfo.webrtcUrl;
}

async function playFlv() {
  resetPlayers();
  if (!window.flvjs || !window.flvjs.isSupported()) {
    throw new Error('flv.js is not available in this browser');
  }
  flvPlayer = window.flvjs.createPlayer({
    type: 'flv',
    url: currentPlayInfo.httpFlvUrl
  });
  flvPlayer.attachMediaElement(videoEl);
  flvPlayer.load();
  await flvPlayer.play();
  hideMessage();
  playUrlEl.textContent = currentPlayInfo.httpFlvUrl;
}

async function playHls() {
  resetPlayers();
  if (window.Hls && window.Hls.isSupported()) {
    hlsPlayer = new window.Hls();
    hlsPlayer.loadSource(currentPlayInfo.hlsUrl);
    hlsPlayer.attachMedia(videoEl);
    hlsPlayer.on(window.Hls.Events.MANIFEST_PARSED, () => {
      videoEl.play().catch(() => undefined);
    });
  } else if (videoEl.canPlayType('application/vnd.apple.mpegurl')) {
    videoEl.src = currentPlayInfo.hlsUrl;
    await videoEl.play();
  } else {
    throw new Error('HLS is not supported in this browser');
  }
  hideMessage();
  playUrlEl.textContent = currentPlayInfo.hlsUrl;
}

async function selectProtocol(protocol) {
  currentProtocol = protocol;
  tabs.forEach((tab) => tab.classList.toggle('active', tab.dataset.protocol === protocol));
  if (!currentPlayInfo) {
    return;
  }
  try {
    if (protocol === 'webrtc') {
      await playWebRtc();
      return;
    }
    if (protocol === 'flv') {
      await playFlv();
      return;
    }
    await playHls();
  } catch (error) {
    resetPlayers();
    setMessage(error.message);
    playUrlEl.textContent = '-';
  }
}

function renderStreamList(streams) {
  if (!streams.length) {
    streamListEl.innerHTML = '<div class="card"><p>No streams available.</p></div>';
    return;
  }
  streamListEl.innerHTML = streams.map((stream) => `
    <article class="card">
      <h3>${stream.name}</h3>
      <p><strong>ID:</strong> ${stream.streamId}</p>
      <p><strong>State:</strong> ${stream.state}</p>
      <p><strong>Cloud online:</strong> ${stream.cloudOnline ? 'Yes' : 'No'}</p>
      <div class="card-actions">
        <button class="button primary" data-stream-id="${stream.streamId}">Play</button>
      </div>
    </article>
  `).join('');

  streamListEl.querySelectorAll('button[data-stream-id]').forEach((button) => {
    button.addEventListener('click', async () => {
      currentStreamId = button.dataset.streamId;
      window.history.replaceState({}, '', `/player.html?streamId=${encodeURIComponent(currentStreamId)}`);
      await loadPlayInfo();
    });
  });
}

async function loadPlayInfo() {
  if (!currentStreamId) {
    setMessage('Choose a stream from the list.');
    titleEl.textContent = 'Select a stream';
    playUrlEl.textContent = '-';
    return;
  }
  try {
    currentPlayInfo = await fetchJson(`/api/streams/${encodeURIComponent(currentStreamId)}/play-info`);
    titleEl.textContent = `${currentPlayInfo.streamName} (${currentPlayInfo.streamId})`;
    await selectProtocol(currentProtocol);
  } catch (error) {
    resetPlayers();
    setMessage(error.message);
    playUrlEl.textContent = '-';
  }
}

async function bootstrap() {
  try {
    const streams = await fetchJson('/api/streams');
    renderStreamList(streams);
    await loadPlayInfo();
  } catch (error) {
    setMessage(error.message);
    streamListEl.innerHTML = `<div class="card"><p>${error.message}</p></div>`;
  }
}

tabs.forEach((tab) => {
  tab.addEventListener('click', () => selectProtocol(tab.dataset.protocol));
});

refreshButton.addEventListener('click', bootstrap);
bootstrap();