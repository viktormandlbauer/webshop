// Load cart items from localStorage
function loadCartItems() {
    const cartItems = JSON.parse(localStorage.getItem("cartItems")) || [];
    const cartItemsContainer = document.getElementById("cart-items");
    const totalItemsElement = document.getElementById("total-items");
    const totalPriceElement = document.getElementById("total-price");

    let totalItems = 0;
    let totalPrice = 0;

    cartItemsContainer.innerHTML = "";

    cartItems.forEach(item => {
        const itemElement = document.createElement("div");
        itemElement.classList.add("cart-item", "mb-3", "p-3", "border", "rounded");

        itemElement.innerHTML = `
                <div class="d-flex align-items-center">
                    <img src="${item.imageURL}" alt="${item.name}" class="me-3" style="width: 60px; height: 60px; object-fit: cover;">
                    <div>
                        <h5 class="mb-1">${item.name}</h5>
                        <p class="mb-0">€${item.price.toFixed(2)} x ${item.quantity}</p>
                    </div>
                </div>
            `;

        cartItemsContainer.appendChild(itemElement);

        totalItems += item.quantity;
        totalPrice += item.price * item.quantity;
    });

    totalItemsElement.textContent = totalItems;
    totalPriceElement.textContent = `€${totalPrice.toFixed(2)}`;
}

// Handle checkout button click
async function openCheckoutModal() {
    try {
        const addressResponse = await fetch("/api/profile/addresses");
        const addresses = await addressResponse.json();

        const paymentResponse = await fetch("/api/profile/payment-methods");
        const paymentMethods = await paymentResponse.json();

        const addressSelect = document.getElementById("shippingAddress");
        const paymentSelect = document.getElementById("paymentMethod");

        addressSelect.innerHTML = '<option value="">Bitte wählen...</option>';
        addresses.data.forEach(address => {
            const option = document.createElement("option");
            option.value = address.id;
            option.textContent = `${address.streetAddress}, ${address.city}, ${address.postalCode}, ${address.country}`;
            addressSelect.appendChild(option);
        });

        paymentSelect.innerHTML = '<option value="">Bitte wählen...</option>';
        paymentMethods.data.forEach(method => {
            const option = document.createElement("option");
            option.value = method.id;
            option.textContent = `${method.cardHolderName} - **** ${method.cardNumber.slice(-4)}`;
            paymentSelect.appendChild(option);
        });

        const modal = new bootstrap.Modal(document.getElementById("checkoutModal"));
        modal.show();
    } catch (error) {
        console.error("Fehler beim Laden der Daten:", error);
        alert("Ein Fehler ist aufgetreten. Bitte später erneut versuchen.");
    }
}

async function handleCheckout(event) {
    event.preventDefault();

    const shippingAddressId = document.getElementById("shippingAddress").value;
    const paymentMethodId = document.getElementById("paymentMethod").value;

    if (!shippingAddressId || !paymentMethodId) {
        alert("Bitte Adresse und Zahlungsmethode auswählen.");
        return;
    }

    const cartItems = JSON.parse(localStorage.getItem("cartItems")) || [];

    if (cartItems.length === 0) {
        alert("Ihr Warenkorb ist leer.");
        return;
    }

    const orderItemDtoList = cartItems.map(item => ({
        productId: item.id,
        quantity: item.quantity
    }));

    const orderPayload = {
        shippingAddressId: parseInt(shippingAddressId),
        billingAddressId: parseInt(shippingAddressId), // use same address for billing
        paymentMethodId: parseInt(paymentMethodId),
        orderItemDtoList: orderItemDtoList
    };

    try {
        const response = await fetch("/api/orders/new", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(orderPayload)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error("Fehler bei der Bestellung: " + errorText);
        }

        localStorage.removeItem("cartItems");

        const orderResponse = await response.json();

        // Open receipt in a new tab or window
        window.open(`/api/orders/receipt?orderId=${orderResponse.orderId}`, "_blank");

        window.location.href = "/customer/orders";

    } catch (error) {
        console.error("Bestellfehler:", error);
        alert("Ein Fehler ist aufgetreten: " + error.message);
    }
}


// Initialize
document.addEventListener("DOMContentLoaded", () => {
    loadCartItems();
    document.getElementById("checkout-button").addEventListener("click", openCheckoutModal);
});