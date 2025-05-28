document.addEventListener("DOMContentLoaded", () => {
    loadCartItems();

    // Event-Listener für die Buttons hinzufügen
    document.querySelector(".cart-details").addEventListener("click", (event) => {
        if (event.target.classList.contains("increase-quantity")) {
            const itemId = event.target.closest(".cart-item").dataset.itemId;
            updateCartItemQuantity(itemId, 1);
        } else if (event.target.classList.contains("decrease-quantity")) {
            const itemId = event.target.closest(".cart-item").dataset.itemId;
            updateCartItemQuantity(itemId, -1);
        }
    });
});

function loadCartItems() {
    const cartCountElement = document.querySelector(".cart-count");
    const cartDetailsElement = document.querySelector(".cart-details");
    const cartTotalPriceElement = document.getElementById("cart-total-price");
    const cartItemTemplate = document.getElementById("cart-item-template");

    const cartItems = JSON.parse(localStorage.getItem("cartItems")) || [];

    let totalPrice = 0;

    // Entferne alte Einträge (außer der Dummy-Vorlage)
    const existingItems = cartDetailsElement.querySelectorAll(".cart-item:not(#cart-item-template)");
    existingItems.forEach(item => item.remove());

    // Füge jedes Produkt in den Warenkorb ein
    cartItems.forEach(item => {
        const cartItem = cartItemTemplate.cloneNode(true);
        cartItem.classList.remove("d-none");
        cartItem.removeAttribute("id");
        cartItem.dataset.itemId = item.id;

        cartItem.querySelector(".cart-item-title").textContent = item.name;
        cartItem.querySelector(".quantity-value").textContent = item.quantity;
        const itemTotalPrice = item.price * item.quantity;
        cartItem.querySelector(".cart-item-price").textContent = `€${itemTotalPrice.toFixed(2)}`;

        cartDetailsElement.insertBefore(cartItem, cartTotalPriceElement.parentElement);

        totalPrice += itemTotalPrice;
    });

    // Aktualisiere die Gesamtanzahl und den Gesamtpreis
    cartCountElement.textContent = cartItems.reduce((sum, item) => sum + item.quantity, 0);
    cartTotalPriceElement.textContent = `€${totalPrice.toFixed(2)}`;
}

function updateCartItemQuantity(itemId, change) {
    const cartItems = JSON.parse(localStorage.getItem("cartItems")) || [];
    const item = cartItems.find(item => item.id === parseInt(itemId, 10));

    if (item) {
        item.quantity += change;

        // Entferne das Item, wenn die Menge 0 oder kleiner ist
        if (item.quantity <= 0) {
            const index = cartItems.indexOf(item);
            cartItems.splice(index, 1);
        }

        localStorage.setItem("cartItems", JSON.stringify(cartItems));
        loadCartItems();
    }
}

function addToCart() {
    const cartItems = JSON.parse(localStorage.getItem("cartItems")) || [];
    const existingItem = cartItems.find(item => item.id === product.id);

    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cartItems.push({ id: product.id, name: product.name, price: product.price, quantity: 1 });
    }

    localStorage.setItem("cartItems", JSON.stringify(cartItems));
    loadCartItems();
}