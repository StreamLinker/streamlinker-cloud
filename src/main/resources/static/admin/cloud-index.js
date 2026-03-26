const edgeListEl = document.getElementById('edgeList');
const streamListEl = document.getElementById('streamList');
const edgeCountEl = document.getElementById('edgeCount');
const streamCountEl = document.getElementById('streamCount');
const refreshButton = document.getElementById('refreshDashboard');

async function fetchJson(url) {
  const response = await fetch(url);
  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`);
  }
  return response.json();
}

function statusBadge(value) {
  return value === 'ONLINE' || value === 'RUNNING'
    ? '<span class="status-online">Online</span>'
    : '<span class="status-offline">Offline</span>';
}

function renderEdges(edges) {
  edgeCountEl.textContent = String(edges.length);
  if (!edges.length) {
    edgeListEl.innerHTML = '<div class="card"><p>No edge nodes have registered yet.</p></div>';
    return;
  }
  edgeListEl.innerHTML = edges.map((edge) => `
    <article class="card">
      <h3>${edge.name}</h3>
      <p><strong>ID:</strong> ${edge.edgeId}</p>
      <p><strong>Status:</strong> ${statusBadge(edge.status)}</p>
      <p><strong>Version:</strong> ${edge.version || '-'}</p>
      <p><strong>Last heartbeat:</strong> ${edge.lastHeartbeatAt || '-'}</p>
    </article>
  `).join('');
}

function renderStreams(streams) {
  streamCountEl.textContent = String(streams.length);
  if (!streams.length) {
    streamListEl.innerHTML = '<div class="card"><p>No streams have been reported yet.</p></div>';
    return;
  }
  streamListEl.innerHTML = streams.map((stream) => `
    <article class="card">
      <h3>${stream.name}</h3>
      <p><strong>Stream ID:</strong> ${stream.streamId}</p>
      <p><strong>Edge:</strong> ${stream.edgeId}</p>
      <p><strong>Mode:</strong> ${stream.accessMode || '-'}</p>
      <p><strong>Cloud stream:</strong> ${stream.cloudApp || '-'} / ${stream.cloudStream || '-'}</p>
      <p><strong>State:</strong> ${statusBadge(stream.state)}</p>
      <div class="card-actions">
        <a class="button primary" href="/player.html?streamId=${encodeURIComponent(stream.streamId)}">Play</a>
      </div>
    </article>
  `).join('');
}

async function loadDashboard() {
  try {
    const [edges, streams] = await Promise.all([
      fetchJson('/api/edge'),
      fetchJson('/api/streams')
    ]);
    renderEdges(edges);
    renderStreams(streams);
  } catch (error) {
    edgeListEl.innerHTML = `<div class="card"><p>${error.message}</p></div>`;
    streamListEl.innerHTML = `<div class="card"><p>${error.message}</p></div>`;
  }
}

refreshButton.addEventListener('click', loadDashboard);
loadDashboard();