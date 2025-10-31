import { initializeApp } from "https://www.gstatic.com/firebasejs/10.14.1/firebase-app.js";
import { getAuth, onAuthStateChanged, signInWithEmailAndPassword, signOut, getIdToken } from "https://www.gstatic.com/firebasejs/10.14.1/firebase-auth.js";

const firebaseConfig = {
  apiKey: "ВАШ_API_KEY",
  authDomain: "eltech-25eb8.firebaseapp.com",
  projectId: "eltech-25eb8"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

async function authFetch(url, opts = {}) {
  const user = auth.currentUser;
  const token = user ? await getIdToken(user) : null;
  const h = new Headers(opts.headers || {});
  if (token) h.set("Authorization", "Bearer " + token);
  return fetch(url, { ...opts, headers: h });
}

onAuthStateChanged(auth, (user) => {
  const badge = document.getElementById("authBadge");
  if (badge) badge.textContent = user ? (user.email || user.uid) : "Гость";
});

window.auth = {
  async signIn(email, pass) { await signInWithEmailAndPassword(auth, email, pass); },
  async signOut() { await signOut(auth); },
  fetch: authFetch,
};
