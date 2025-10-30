// ui/firebase-auth.js
console.log('[firebase-auth] start');

import { initializeApp } from "https://www.gstatic.com/firebasejs/10.14.1/firebase-app.js";
import { getAuth, onAuthStateChanged, signInWithEmailAndPassword, signOut, getIdToken } from "https://www.gstatic.com/firebasejs/10.14.1/firebase-auth.js";

const firebaseConfig = {
    apiKey: "AIzaSyC7Z-MYD1zuReRP7gH5YX3va-Kv94GJctk",
    authDomain: "eltech-25eb8.firebaseapp.com",
    projectId: "eltech-25eb8",
    storageBucket: "eltech-25eb8.firebasestorage.app",
    messagingSenderId: "133670605676",
    appId: "1:133670605676:web:30dbd61a28421c0b1f5949",
    measurementId: "G-Z0X669LCKE"
};

let app, auth;
try {
    app = initializeApp(firebaseConfig);
    auth = getAuth(app);
    console.log('[firebase-auth] SDK initialized');
} catch (e) {
    console.error('[firebase-auth] init error:', e);
}

async function authFetch(url, opts = {}) {
    try {
        const user = auth?.currentUser || null;
        const token = user ? await getIdToken(user) : null;
        const h = new Headers(opts.headers || {});
        if (token) h.set("Authorization", "Bearer " + token);
        return fetch(url, { ...opts, headers: h });
    } catch (e) {
        console.error('[firebase-auth] fetch error:', e);
        throw e;
    }
}

onAuthStateChanged(getAuth(), (user) => {
    console.log('[firebase-auth] state:', user ? user.email || user.uid : 'guest');
    const badge = document.getElementById("authBadge");
    const loginBox = document.getElementById("loginBox");
    if (badge) badge.textContent = user ? (user.email || user.uid) : "Гость";
    if (loginBox) loginBox.classList.toggle("hidden", !!user);
});

window.auth = {
    auth,
    async signIn(email, pass) {
        console.log('[firebase-auth] signIn', email);
        await signInWithEmailAndPassword(getAuth(), email, pass);
    },
    async signOut() {
        console.log('[firebase-auth] signOut');
        await signOut(getAuth());
    },
    fetch: authFetch,
};

console.log('[firebase-auth] ready');
export {}; // фикс для строгого module-режима
