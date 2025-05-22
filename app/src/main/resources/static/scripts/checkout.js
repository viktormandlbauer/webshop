document.addEventListener("DOMContentLoaded", () => {
    const cartItemsContainer = document.getElementById("cart-items");
    const totalItemsElement = document.getElementById("total-items");
    const totalPriceElement = document.getElementById("total-price");
    const checkoutButton = document.getElementById("checkout-button");

    // Funktion zum Laden der Warenkorb-Produkte
    function loadCartItems() {
        const cartItems = JSON.parse(localStorage.getItem("cartItems")) || [];
        let totalItems = 0;
        let totalPrice = 0;

        // Entferne alte Einträge
        cartItemsContainer.innerHTML = "";

        // Füge jedes Produkt in die Liste ein
        cartItems.forEach(item => {
            const cartItemElement = document.createElement("div");
            cartItemElement.classList.add("cart-item", "mb-3", "p-3", "border", "rounded");

            cartItemElement.innerHTML = `
                <div class="d-flex align-items-center">
                    <img src="${item.imageURL}" alt="${item.name}" class="cart-item-image me-3" style="width: 60px; height: 60px; object-fit: cover;">
                    <div>
                        <h5 class="cart-item-title mb-1">${item.name}</h5>
                        <p class="cart-item-price mb-0">€${item.price.toFixed(2)} x ${item.quantity}</p>
                    </div>
                </div>
            `;

            cartItemsContainer.appendChild(cartItemElement);

            totalItems += item.quantity;
            totalPrice += item.price * item.quantity;
        });

        // Aktualisiere die Gesamtanzahl und den Gesamtpreis
        totalItemsElement.textContent = totalItems;
        totalPriceElement.textContent = `€${totalPrice.toFixed(2)}`;
    }

    // Event-Listener für den Checkout-Button
    checkoutButton.addEventListener("click", () => {
        if (confirm("Möchten Sie die Bestellung abschließen?")) {
            localStorage.removeItem("cartItems");
            alert("Vielen Dank für Ihre Bestellung!");
            window.location.href = "/products";
        }
    });

    // Initialisiere die Seite
    loadCartItems();
});