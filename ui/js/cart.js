// cart.js
(() => {
  const CART_KEY = 'cart';
  const modal = document.getElementById('cartModal');
  const body  = document.getElementById('cartBody');
  const openBtn = document.getElementById('cartOpen');
  const closeBtn = document.getElementById('cartClose');
  const totalEl = document.getElementById('cartTotal');
  const payBtn = document.getElementById('cartPay');
  const historyBtn = document.getElementById('cartHistory');

  let cart = load();

  function load() {
    try { return JSON.parse(localStorage.getItem(CART_KEY) || '[]'); } catch { return []; }
  }
  function save() {
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    updateCount();
  }
  function updateCount() {
    const c = cart.reduce((s, i) => s + i.qty, 0);
    const b = document.getElementById('cartBadge');
    if (b) b.textContent = c;
  }

  function render() {
    body.innerHTML = '';
    let total = 0;
    cart.forEach((item, idx) => {
      total += item.price * item.qty;
      const row = document.createElement('div');
      row.className = 'cart-row';
      row.innerHTML = `
        <div class="cart-line">
          <div class="cart-name">${item.name}</div>
          <div class="cart-meta">${item.price} ₽ × ${item.qty}</div>
          <button class="btn btn-sm" data-remove="${idx}">×</button>
        </div>`;
      body.appendChild(row);
    });
    totalEl.textContent = total.toFixed(2) + ' ₽';
  }

  function open() {
    render();
    modal.style.display = 'block';
    document.body.classList.add('modal-open');
  }
  function close() {
    modal.style.display = 'none';
    document.body.classList.remove('modal-open');
  }

  function add(product) {
    const i = cart.findIndex(x => x.id === product.id);
    if (i >= 0) cart[i].qty += 1;
    else cart.push({ id: product.id, name: product.name, price: product.price, qty: 1 });
    save(); render();
  }
  function removeIndex(i) {
    cart.splice(i, 1);
    save(); render();
  }

  body.addEventListener('click', (e) => {
    const i = e.target.getAttribute('data-remove');
    if (i != null) removeIndex(Number(i));
  });

  openBtn?.addEventListener('click', open);
  closeBtn?.addEventListener('click', close);
  window.addEventListener('keydown', (e) => { if (e.key === 'Escape') close(); });

  async function getIdToken() {
    // Если используется Firebase compat:
    // вернет токен авторизованного пользователя, иначе null
    try {
      if (window.firebase?.auth) {
        const u = window.firebase.auth().currentUser;
        if (u) return await u.getIdToken();
      }
    } catch {}
    return null;
  }

  async function checkoutOne(productId, quantity = 1) {
    const token = await getIdToken();
    const url = `/api/orders?productId=${encodeURIComponent(productId)}&quantity=${encodeURIComponent(quantity)}`;
    const res = await fetch(url, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {})
      }
    });
    if (!res.ok) {
      const txt = await res.text().catch(()=> '');
      throw new Error(`order ${res.status} ${txt}`);
    }
    return res.json();
  }

  payBtn?.addEventListener('click', async () => {
    try {
      if (cart.length === 0) { alert('Корзина пуста'); return; }
      // Для демо оформим один заказ по первому товару
      const first = cart[0];
      const data = await checkoutOne(first.id, first.qty);
      alert(`Заказ #${data.id || ''} оформлен`);
      cart = [];
      save(); render(); close();
    } catch (e) {
      console.error(e);
      alert('Ошибка оформления заказа');
    }
  });

  historyBtn?.addEventListener('click', () => {
    // Место для запроса истории, если нужно
    alert('История: пока локально не хранится');
  });

  // Экспорт для каталога
  window.Cart = { add, open };
  updateCount();
})();
