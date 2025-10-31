// js/auth.js — обычный скрипт (НЕ module), Firebase compat v9
(function () {
  // firebase compat уже подключён в index.html и инициализирован в js/firebase-init.js
  const auth = firebase.auth();

  // Глобальная функция для OrderService
  window.getAuthToken = async function getAuthToken() {
    const u = auth.currentUser;
    if (!u) return null;
    return await u.getIdToken(true);
  };

  // UI
  const btnAuth      = document.getElementById('btn-auth');
  const authModal    = document.getElementById('auth-modal');
  const btnLogin     = document.getElementById('btn-login');
  const btnSignup    = document.getElementById('btn-signup');
  const inputEmail   = document.getElementById('email');
  const inputPass    = document.getElementById('password');
  const userDisplay  = document.getElementById('user-display');

  function openAuth()  { if (authModal) authModal.style.display = 'block'; }
  function closeAuth() { if (authModal) authModal.style.display = 'none'; }

  function updateUserLabel(user) {
    if (!userDisplay) return;
    userDisplay.textContent = user ? user.email : '';
    if (btnAuth) btnAuth.textContent = user ? 'Выйти' : 'Войти';
  }

  // Кнопка в шапке: Войти/Выйти
  if (btnAuth) {
    btnAuth.addEventListener('click', async () => {
      const u = auth.currentUser;
      if (u) {
        await auth.signOut();
        updateUserLabel(null);
      } else {
        openAuth();
      }
    });
  }

  // Логин
  if (btnLogin) {
    btnLogin.addEventListener('click', async () => {
      try {
        const email = inputEmail.value.trim();
        const pass  = inputPass.value;
        await auth.signInWithEmailAndPassword(email, pass);
        updateUserLabel(auth.currentUser);
        closeAuth();
      } catch (e) {
        alert('Ошибка входа: ' + (e?.message || e));
      }
    });
  }

  // Регистрация
  if (btnSignup) {
    btnSignup.addEventListener('click', async () => {
      try {
        const email = inputEmail.value.trim();
        const pass  = inputPass.value;
        await auth.createUserWithEmailAndPassword(email, pass);
        updateUserLabel(auth.currentUser);
        closeAuth();
      } catch (e) {
        alert('Ошибка регистрации: ' + (e?.message || e));
      }
    });
  }

  // Слежение за сессией
  auth.onAuthStateChanged(user => updateUserLabel(user));
})();
