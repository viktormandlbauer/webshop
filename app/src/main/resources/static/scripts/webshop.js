const state = {
  user: null,
  categories: [],
  products: [],
  account: null,
};

const api = (url, options = {}) => $.ajax({
  url,
  method: options.method || 'GET',
  contentType: options.contentType === false ? false : 'application/json',
  processData: options.processData === false ? false : true,
  data: options.data,
});

const jsonApi = (url, method, data) => api(url, {
  method,
  data: data ? JSON.stringify(data) : undefined,
});

function formObject(form) {
  return Object.fromEntries(new FormData(form).entries());
}

function flash(message, type = 'success') {
  $('#flash-message')
    .removeClass()
    .addClass(`alert alert-${type} alert-dismissible fade show`)
    .text(message)
    .show();
  setTimeout(() => $('#flash-message').fadeOut(180), 3200);
}

function price(value) {
  return Number(value || 0).toFixed(2);
}

function showView(name) {
  $('.view').addClass('d-none');
  $(`#view-${name}`).removeClass('d-none');
  if (name === 'products') loadProducts();
  if (name === 'cart') renderCart();
  if (name === 'account') loadAccount();
  if (name === 'admin') loadAdmin();
}

function renderMenu() {
  const user = state.user;
  const isAdmin = user && user.role === 'Admin';
  const items = isAdmin
    ? [['home', 'Home'], ['admin', 'Produkte bearbeiten'], ['admin', 'Kunden bearbeiten'], ['admin', 'Gutscheine verwalten']]
    : user && user.id
      ? [['home', 'Home'], ['products', 'Produkte'], ['account', 'Mein Konto'], ['cart', 'Warenkorb']]
      : [['home', 'Home'], ['products', 'Produkte'], ['cart', 'Warenkorb'], ['auth', 'Login / Registrierung']];

  $('#menu').html(items.map(([view, label]) =>
    `<li class="nav-item"><a class="nav-link" href="#" data-view="${view}">${label}</a></li>`).join(''));

  if (user && user.id) {
    $('#menu').append('<li class="nav-item"><a class="nav-link" href="#" id="logout">Logout</a></li>');
    $('#login-status').html(`<span class="status-indicator bg-success"></span>${user.username} (${user.role})`);
  } else {
    $('#login-status').html('<span class="status-indicator bg-danger"></span>Gast');
  }
}

function updateCartBadge(cart) {
  $('#cart-count').text(cart ? cart.count : 0);
}

function renderCategories() {
  const options = state.categories.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
  $('#category-filter').html(options);
  $('[name="categoryId"]').html(options);
}

function productImage(product) {
  return product.imageURL || '/images/ProductPlaceholder.jpeg';
}

function renderProducts(products) {
  $('#products').html(products.map(product => `
    <article class="product-card" draggable="true" data-product-id="${product.id}">
      <img src="${productImage(product)}" alt="${product.name}" onerror="this.src='/images/ProductPlaceholder.jpeg'">
      <div class="body">
        <div class="d-flex justify-content-between gap-2">
          <h3 class="h5">${product.name}</h3>
          <span class="badge-soft">${product.avgRating || '0.0'} ★</span>
        </div>
        <p>${product.description}</p>
        <div class="d-flex justify-content-between align-items-center">
          <span class="price">${price(product.price)} EUR</span>
          <button class="btn btn-shop btn-sm add-cart" data-id="${product.id}">In den Warenkorb</button>
        </div>
      </div>
    </article>`).join(''));
}

function loadProducts() {
  const q = $('#search').val();
  const categoryId = $('#category-filter').val();
  const query = q ? `?q=${encodeURIComponent(q)}` : categoryId ? `?categoryId=${categoryId}` : '';
  api(`/api/shop/products${query}`).done(products => {
    state.products = products;
    renderProducts(products);
  });
}

function renderCart(cart = null) {
  const done = data => {
    updateCartBadge(data);
    if (!data.items.length) {
      $('#cart-list').html('<p>Der Warenkorb ist leer.</p>');
      $('#checkout-box').hide();
    } else {
      $('#cart-list').html(data.items.map(item => `
        <div class="cart-row">
          <div><strong>${item.product.name}</strong><br><span>${price(item.lineTotal)} EUR</span></div>
          <input class="form-control cart-qty" type="number" min="0" value="${item.quantity}" data-id="${item.product.id}" style="width:90px">
          <button class="btn btn-outline-light btn-sm remove-cart" data-id="${item.product.id}">Entfernen</button>
        </div>`).join(''));
      $('#checkout-box').show();
    }
    $('#cart-total').text(price(data.total));
  };
  cart ? done(cart) : api('/api/shop/cart').done(done);
}

function loadAccount() {
  api('/api/shop/account').done(account => {
    state.account = account;
    Object.entries(account.user).forEach(([key, value]) => $(`#account-form [name="${key}"]`).val(value));
    $('#payment-list').html(account.paymentMethods.map(p => `<div>${p.label}: ${p.maskedDetails}</div>`).join('') || '<small>Keine Zahlungsarten angelegt.</small>');
    $('#checkout-payment').html(account.paymentMethods.map(p => `<option value="${p.id}">${p.label} (${p.maskedDetails})</option>`).join(''));
    $('#order-list').html(account.orders.map(order => `
      <div class="order-row">
        <div><strong>Bestellung #${order.id}</strong><br>${new Date(order.orderDate).toLocaleString()} · ${price(order.finalTotal)} EUR</div>
        <span class="badge-soft">${order.items.length} Positionen</span>
        <button class="btn btn-outline-light btn-sm invoice" data-id="${order.id}">Rechnung drucken</button>
      </div>`).join('') || '<p>Noch keine Bestellungen vorhanden.</p>');
  }).fail(() => {
    flash('Bitte zuerst einloggen.', 'warning');
    showView('auth');
  });
}

function loadAdmin() {
  if (!state.user || state.user.role !== 'Admin') {
    flash('Adminrechte erforderlich.', 'warning');
    showView('home');
    return;
  }
  api('/api/shop/products').done(products => {
    state.products = products;
    $('#admin-products').html(products.map(p => `
      <div class="admin-row">
        <div><strong>${p.name}</strong><br>${price(p.price)} EUR · ${p.category.name}</div>
        <button class="btn btn-outline-light btn-sm edit-product" data-id="${p.id}">Bearbeiten</button>
        <button class="btn btn-danger btn-sm delete-product" data-id="${p.id}">Loeschen</button>
      </div>`).join(''));
  });
  api('/api/shop/admin/customers').done(customers => {
    $('#admin-customers').html(customers.map(c => `
      <div class="admin-row">
        <div><strong>${c.username}</strong><br>${c.email} · ${c.role}</div>
        <button class="btn btn-outline-light btn-sm customer-orders" data-id="${c.id}">Bestellungen</button>
        <button class="btn btn-${c.active ? 'warning' : 'success'} btn-sm toggle-customer" data-id="${c.id}" data-active="${!c.active}">
          ${c.active ? 'Deaktivieren' : 'Aktivieren'}
        </button>
      </div>`).join(''));
  });
  api('/api/shop/admin/vouchers').done(vouchers => {
    $('#admin-vouchers').html(vouchers.map(v => `
      <div class="admin-row">
        <div><strong>${v.code}</strong><br>Wert ${price(v.value)} EUR · Rest ${price(v.remainingValue)} EUR · bis ${v.expiresAt}</div>
        <span class="badge-soft">${v.expired ? 'abgelaufen' : v.redeemed ? 'eingeloest' : 'aktiv'}</span>
      </div>`).join(''));
  });
}

function addCart(productId, quantity = 1) {
  jsonApi('/api/shop/cart/items', 'POST', { productId, quantity }).done(cart => {
    renderCart(cart);
    updateCartBadge(cart);
    flash('Produkt wurde in den Warenkorb gelegt.');
  });
}

function initDragDrop() {
  $(document).on('dragstart', '.product-card', function (event) {
    event.originalEvent.dataTransfer.setData('text/plain', $(this).data('product-id'));
  });
  $('#cart-drop')
    .on('dragover', event => {
      event.preventDefault();
      $('#cart-drop').addClass('drag-over');
    })
    .on('dragleave drop', () => $('#cart-drop').removeClass('drag-over'))
    .on('drop', event => {
      event.preventDefault();
      addCart(Number(event.originalEvent.dataTransfer.getData('text/plain')));
    });
}

$(function () {
  api('/api/shop/session').done(data => {
    state.user = data.user && data.user.id ? data.user : null;
    updateCartBadge(data.cart);
    renderMenu();
  });
  api('/api/shop/categories').done(categories => {
    state.categories = categories;
    renderCategories();
    loadProducts();
  });
  initDragDrop();

  $(document).on('click', '[data-view]', function (event) {
    event.preventDefault();
    showView($(this).data('view'));
  });

  $(document).on('click', '#logout', function (event) {
    event.preventDefault();
    jsonApi('/api/shop/logout', 'POST').done(() => {
      state.user = null;
      renderMenu();
      flash('Du bist ausgeloggt.');
      showView('home');
    });
  });

  $('#login-form').on('submit', function (event) {
    event.preventDefault();
    const data = formObject(this);
    data.rememberMe = Boolean(data.rememberMe);
    jsonApi('/api/shop/login', 'POST', data).done(result => {
      state.user = result.user;
      renderMenu();
      flash('Login erfolgreich.');
      showView(state.user.role === 'Admin' ? 'admin' : 'products');
    }).fail(xhr => flash(xhr.responseJSON?.message || 'Login fehlgeschlagen.', 'danger'));
  });

  $('#register-form').on('submit', function (event) {
    event.preventDefault();
    const data = formObject(this);
    if (data.password !== data.confirmPassword) {
      flash('Passwoerter stimmen nicht ueberein.', 'danger');
      return;
    }
    jsonApi('/api/shop/register', 'POST', data).done(() => {
      this.reset();
      flash('Registrierung erfolgreich. Du kannst dich jetzt einloggen.');
    }).fail(xhr => flash(xhr.responseJSON?.message || 'Registrierung fehlgeschlagen.', 'danger'));
  });

  $('#category-filter').on('change', loadProducts);
  $('#search').on('input', () => window.clearTimeout(window.searchTimer) || (window.searchTimer = setTimeout(loadProducts, 180)));
  $(document).on('click', '.add-cart', function () { addCart(Number($(this).data('id'))); });
  $(document).on('click', '.remove-cart', function () {
    jsonApi(`/api/shop/cart/items/${$(this).data('id')}`, 'DELETE').done(renderCart);
  });
  $(document).on('change', '.cart-qty', function () {
    const productId = Number($(this).data('id'));
    jsonApi(`/api/shop/cart/items/${productId}`, 'PATCH', { productId, quantity: Number($(this).val()) }).done(renderCart);
  });

  $('#checkout-btn').on('click', function () {
    const paymentMethodId = Number($('#checkout-payment').val()) || null;
    const voucherCode = $('#checkout-voucher').val();
    jsonApi('/api/shop/checkout', 'POST', { paymentMethodId, voucherCode }).done(order => {
      flash(`Bestellung #${order.id} wurde gespeichert.`);
      renderCart({ items: [], total: 0, count: 0 });
    }).fail(xhr => flash(xhr.responseJSON?.message || 'Bestellung nicht moeglich.', 'danger'));
  });

  $('#account-form').on('submit', function (event) {
    event.preventDefault();
    jsonApi('/api/shop/account', 'PUT', formObject(this)).done(() => {
      flash('Konto gespeichert.');
      loadAccount();
    }).fail(xhr => flash(xhr.responseJSON?.message || 'Speichern fehlgeschlagen.', 'danger'));
  });

  $('#payment-form').on('submit', function (event) {
    event.preventDefault();
    jsonApi('/api/shop/account/payments', 'POST', formObject(this)).done(() => {
      this.reset();
      flash('Zahlungsart hinzugefuegt.');
      loadAccount();
    });
  });

  $(document).on('click', '.invoice', function () {
    window.open(`/api/shop/orders/${$(this).data('id')}/invoice`, '_blank');
  });

  $(document).on('click', '.edit-product', function () {
    const product = state.products.find(p => p.id === Number($(this).data('id')));
    if (!product) return;
    Object.entries({
      id: product.id,
      name: product.name,
      description: product.description,
      price: product.price,
      avgRating: product.avgRating,
      stock: product.stock,
      categoryId: product.category.id,
      imageURL: product.imageURL,
    }).forEach(([key, value]) => $(`#admin-product-form [name="${key}"]`).val(value));
  });

  $('#admin-product-form').on('submit', function (event) {
    event.preventDefault();
    const data = formObject(this);
    const category = state.categories.find(c => c.id === Number(data.categoryId));
    if (data.id) {
      jsonApi(`/api/shop/admin/products/${data.id}`, 'PUT', {
        name: data.name,
        description: data.description,
        price: Number(data.price),
        avgRating: Number(data.avgRating),
        stock: Number(data.stock),
        categoryId: Number(data.categoryId),
        imageURL: data.imageURL,
      }).done(() => {
        this.reset();
        flash('Produkt gespeichert.');
        loadProducts();
        loadAdmin();
      });
    } else {
      const fd = new FormData(this);
      fd.set('categoryName', category.name);
      api('/api/products', { method: 'POST', data: fd, contentType: false, processData: false }).done(() => {
        this.reset();
        flash('Produkt angelegt.');
        loadProducts();
        loadAdmin();
      });
    }
  });

  $(document).on('click', '.delete-product', function () {
    jsonApi(`/api/shop/admin/products/${$(this).data('id')}`, 'DELETE').done(() => {
      flash('Produkt geloescht.');
      loadAdmin();
    });
  });

  $(document).on('click', '.toggle-customer', function () {
    api(`/api/shop/admin/customers/${$(this).data('id')}/active?active=${$(this).data('active')}`, { method: 'PATCH' })
      .done(loadAdmin);
  });

  $(document).on('click', '.customer-orders', function () {
    api(`/api/shop/admin/customers/${$(this).data('id')}/orders`).done(orders => {
      $('#admin-customers').append(`
        <div class="mt-3">
          <h3 class="h5">Bestelldetails</h3>
          ${orders.map(order => `
            <div class="mb-3">
              <strong>Bestellung #${order.id}</strong> · ${price(order.finalTotal)} EUR
              ${order.items.map(item => `
                <div class="admin-row">
                  <div>${item.product.name}<br>${item.quantity} x ${price(item.unitPrice)} EUR</div>
                  <button class="btn btn-danger btn-sm remove-order-item" data-order="${order.id}" data-item="${item.id}">Position entfernen</button>
                </div>`).join('')}
            </div>`).join('') || '<p>Keine Bestellungen.</p>'}
        </div>`);
    });
  });

  $(document).on('click', '.remove-order-item', function () {
    jsonApi(`/api/shop/admin/orders/${$(this).data('order')}/items/${$(this).data('item')}`, 'DELETE').done(() => {
      flash('Position aus Bestellung entfernt.');
      loadAdmin();
    });
  });

  $('#voucher-form').on('submit', function (event) {
    event.preventDefault();
    const data = formObject(this);
    jsonApi('/api/shop/admin/vouchers', 'POST', { value: Number(data.value), expiresAt: data.expiresAt }).done(voucher => {
      this.reset();
      flash(`Gutschein ${voucher.code} erstellt.`);
      loadAdmin();
    });
  });
});
