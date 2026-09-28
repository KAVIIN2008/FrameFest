const API = "http://localhost:8080/api";

const $ = (id) => document.getElementById(id);

async function apiRequest(path, options = {}) {
  const response = await fetch(`${API}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {})
    }
  });

  if (!response.ok) {
    const body = await response.text();
    throw new Error(body || `Request failed (${response.status})`);
  }

  if (response.status === 204) return null;
  return response.json();
}

function showMessage(message, type) {
  const el = $("formMessage");
  el.textContent = message;
  el.className = `message ${type || ""}`;
}

function safeText(value) {
  return String(value ?? "");
}

function renderFilms(entries) {
  const list = $("filmList");

  if (!Array.isArray(entries) || entries.length === 0) {
    list.innerHTML = '<p class="empty-state">No films submitted yet. Be the first!</p>';
    return;
  }

  list.replaceChildren();

  [...entries].reverse().forEach((entry) => {
    const item = document.createElement("article");
    item.className = "film-item";

    const title = document.createElement("h3");
    title.textContent = entry.title || "Untitled film";

    const director = document.createElement("p");
    director.textContent = `Directed by ${entry.directorName || "Unknown"}`;

    const description = document.createElement("p");
    description.textContent = entry.description || "No description provided.";

    const meta = document.createElement("div");
    meta.className = "film-meta";
    meta.textContent = `Entry #${entry.id}`;

    item.append(title, director, description, meta);

    if (entry.filmUrl) {
      try {
        const url = new URL(entry.filmUrl);
        if (url.protocol === "https:" || url.protocol === "http:") {
          const link = document.createElement("a");
          link.href = url.href;
          link.target = "_blank";
          link.rel = "noopener noreferrer";
          link.textContent = "Watch film ↗";
          item.append(link);
        }
      } catch {
        // Ignore invalid URLs.
      }
    }

    list.append(item);
  });
}

function renderLeaderboard(rows) {
  const body = $("leaderboardBody");
  body.replaceChildren();

  if (!Array.isArray(rows) || rows.length === 0) {
    body.innerHTML = '<tr><td colspan="6" class="empty-state">No scores yet. Rankings will appear once films are evaluated.</td></tr>';
    $("topFilm").textContent = "—";
    $("topScore").textContent = "Waiting for results";
    return;
  }

  rows.forEach((row, index) => {
    const tr = document.createElement("tr");

    const values = [
      index + 1,
      row.title || "Untitled film",
      row.directorName || "—",
      row.totalScore ?? 0,
      row.averageScore ?? 0,
      row.numberOfJudges ?? 0
    ];

    values.forEach((value, column) => {
      const td = document.createElement("td");

      if (column === 0) {
        const badge = document.createElement("span");
        badge.className = "rank-badge";
        badge.textContent = value;
        td.append(badge);
      } else {
        td.textContent = column === 4 && typeof value === "number"
          ? value.toFixed(1)
          : value;
      }

      if (column === 3) td.className = "score-cell";
      tr.append(td);
    });

    body.append(tr);
  });

  $("topFilm").textContent = rows[0].title || "—";
  $("topScore").textContent = `${rows[0].totalScore ?? 0} total points`;
}

async function loadEntries() {
  const entries = await apiRequest("/entries");
  $("totalFilms").textContent = entries.length;
  renderFilms(entries);
}

async function loadJudges() {
  const judges = await apiRequest("/judges");
  $("totalJudges").textContent = judges.length;
}

async function loadScores() {
  const scores = await apiRequest("/scores");
  $("totalScores").textContent = scores.length;
}

async function loadLeaderboard() {
  const rows = await apiRequest("/results/leaderboard");
  renderLeaderboard(rows);
}

async function loadDashboard() {
  const tasks = await Promise.allSettled([
    loadEntries(),
    loadJudges(),
    loadScores(),
    loadLeaderboard()
  ]);

  const labels = ["film submissions", "judges", "scores", "leaderboard"];
  const failures = tasks
    .map((task, index) => task.status === "rejected"
      ? `${labels[index]}: ${task.reason?.message || "request failed"}`
      : null)
    .filter(Boolean);

  if (failures.length) {
    console.error("Some dashboard data could not be loaded:", failures);
    const list = $("filmList");
    if (tasks[0].status === "rejected") {
      list.innerHTML = '<p class="empty-state">Could not load submissions. Make sure the backend is running at http://localhost:8080 and CORS is configured.</p>';
    }
    const body = $("leaderboardBody");
    if (tasks[3].status === "rejected") {
      body.innerHTML = '<tr><td colspan="6" class="empty-state">Could not load leaderboard. Check the backend and browser console.</td></tr>';
    }
    const message = $("formMessage");
    if (message) {
      message.textContent = `Some dashboard data could not load: ${failures.join(" | ")}`;
      message.className = "message error";
    }
  }
}

$("entryForm").addEventListener("submit", async (event) => {
  event.preventDefault();

  const button = $("entryForm").querySelector("button[type='submit']");
  button.disabled = true;
  showMessage("Submitting your film...", "");

  const entry = {
    title: $("title").value.trim(),
    directorName: $("directorName").value.trim(),
    description: $("description").value.trim(),
    filmUrl: $("filmUrl").value.trim()
  };

  try {
    const created = await apiRequest("/entries", {
      method: "POST",
      body: JSON.stringify(entry)
    });

    showMessage(`Film submitted successfully! Entry ID: ${created.id}`, "success");
    $("entryForm").reset();
    await loadDashboard();
  } catch (error) {
    console.error(error);
    showMessage(
      "Could not submit the film. Check that the backend is running and try again.",
      "error"
    );
  } finally {
    button.disabled = false;
  }
});

$("refreshBtn").addEventListener("click", async () => {
  try {
    await loadEntries();
  } catch (error) {
    console.error(error);
    $("filmList").innerHTML = '<p class="empty-state">Could not load submissions. Check the backend.</p>';
  }
});

$("refreshLeaderboard").addEventListener("click", async () => {
  try {
    await loadLeaderboard();
  } catch (error) {
    console.error(error);
    $("leaderboardBody").innerHTML = '<tr><td colspan="6" class="empty-state">Could not load leaderboard.</td></tr>';
  }
});

loadDashboard();