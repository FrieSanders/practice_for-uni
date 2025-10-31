// products.js — обычный скрипт, НЕ module
(function () {
  const GRID_ID = 'productsGrid';   // этот id есть в index.html
  const grid = document.getElementById(GRID_ID);

  function cardHtml(p) {
    return `
      <div class="product-card" data-id="${p.id}">
        ${p.image || p.imageUrl ? `<img src="${p.imageUrl || p.image}" alt="${p.name}" style="max-width:100%; border-radius:8px; margin-bottom:10px;" onerror="this.style.display='none'">` : ''}
        <h3>${p.name ?? 'Без названия'}</h3>
        <p>Цена: ${p.price ?? '-'} ₽</p>
        <button class="btn-add" data-id="${p.id}" data-name="${p.name}" data-price="${p.price ?? 0}">Добавить в корзину</button>
      </div>`;
  }

  async function loadFromApi() {
    const r = await fetch('/api/catalog/products'); // nginx проксирует на catalog-service:8082
    if (!r.ok) throw new Error('catalog ' + r.status);
    return await r.json();
  }

  async function init() {
    if (!grid) return;
    try {
      const items = await loadFromApi();
      grid.innerHTML = Array.isArray(items) && items.length
          ? items.map(cardHtml).join('')
          : '<div class="muted">Товаров нет</div>';

      grid.querySelectorAll('.btn-add').forEach(btn => {
        btn.addEventListener('click', () => {
          const product = {
            id: btn.getAttribute('data-id'),
            name: btn.getAttribute('data-name'),
            price: Number(btn.getAttribute('data-price') || 0)
          };
          if (window.addToCart) window.addToCart(product);
          else console.error('window.addToCart недоступна');
        });
      });
    } catch (e) {
      console.error(e);
      grid.innerHTML = `<div class="error">Ошибка загрузки каталога</div>`;
    }
  }

  document.addEventListener('DOMContentLoaded', init);
})();
