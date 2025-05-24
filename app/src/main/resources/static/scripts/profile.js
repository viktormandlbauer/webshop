let profile = {};

function loadProfile() {
    fetch("/api/profile")
        .then(res => res.json())
        .then(data => {
            profile = data.data;
            document.getElementById("username").textContent = profile.username;
            document.getElementById("email").textContent = profile.email;
            document.getElementById("salutation").textContent = profile.salutation;
            document.getElementById("firstName").textContent = profile.firstName;
            document.getElementById("lastName").textContent = profile.lastName;
            document.getElementById("dateOfBirth").textContent = profile.dateOfBirth;
            document.getElementById("country").textContent = profile.country;
            document.getElementById("address").textContent = profile.address;
            document.getElementById("postalCode").textContent = profile.postalCode;
            document.getElementById("city").textContent = profile.city;

            populateDropdown("paymentMethods", profile.paymentMethods, m => `${m.cardHolderName} - ${m.cardNumber}`);
            populateDropdown("addresses", profile.addresses, a => `${a.streetAddress}, ${a.city}`);
        });
}

function populateDropdown(id, items, labelFn) {
    const dropdown = document.getElementById(id);
    dropdown.innerHTML = '';
    items.forEach((item, index) => {
        const option = document.createElement("option");
        option.value = index;
        option.textContent = labelFn(item);
        dropdown.appendChild(option);
    });
    dropdown.dispatchEvent(new Event("change"));
}

document.getElementById("paymentMethods").addEventListener("change", e => {
    const m = profile.paymentMethods[e.target.value];
    document.getElementById("cardHolderName").textContent = m.cardHolderName;
    document.getElementById("cardNumber").textContent = m.cardNumber;
    document.getElementById("expiryDate").textContent = m.expiryDate;
    document.getElementById("cvv").textContent = m.cvv;
});

document.getElementById("addresses").addEventListener("change", e => {
    const a = profile.addresses[e.target.value];
    document.getElementById("streetAddress").textContent = a.streetAddress;
    document.getElementById("cityDetails").textContent = a.city;
    document.getElementById("postalCodeDetails").textContent = a.postalCode;
    document.getElementById("countryDetails").textContent = a.country;
});

function openPaymentModal(mode) {
    document.getElementById("paymentMode").value = mode;
    if (mode === "edit") {
        const m = profile.paymentMethods[document.getElementById("paymentMethods").value];
        document.getElementById("editCardHolder").value = m.cardHolderName;
        document.getElementById("editCardNumber").value = m.cardNumber;
        document.getElementById("editExpiry").value = m.expiryDate;
        document.getElementById("editCVV").value = m.cvv;
    } else {
        document.getElementById("editCardHolder").value = '';
        document.getElementById("editCardNumber").value = '';
        document.getElementById("editExpiry").value = '';
        document.getElementById("editCVV").value = '';
    }
    new bootstrap.Modal(document.getElementById("editPaymentModal")).show();
}

function savePaymentMethod() {
    const mode = document.getElementById("paymentMode").value;
    const newMethod = {
        cardHolderName: document.getElementById("editCardHolder").value,
        cardNumber: document.getElementById("editCardNumber").value,
        expiryDate: document.getElementById("editExpiry").value,
        cvv: document.getElementById("editCVV").value
    };
    const url = mode === "edit"
        ? `/api/profile/payment-method/update?id=${profile.paymentMethods[document.getElementById("paymentMethods").value].id}`
        : "/api/profile/payment-method/add";
    fetch(url, {
        method: mode === "edit" ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newMethod)
    }).then(() => loadProfile())
        .finally(() => bootstrap.Modal.getInstance(document.getElementById("editPaymentModal")).hide());
}

function deletePaymentMethod() {
    const id = profile.paymentMethods[document.getElementById("paymentMethods").value].id;
    fetch(`/api/profile/payment-method/delete?paymentMethodId=${id}`, {
        method: "DELETE"
    }).then(() => loadProfile());
}

function openAddressModal(mode) {
    document.getElementById("addressMode").value = mode;
    if (mode === "edit") {
        const a = profile.addresses[document.getElementById("addresses").value];
        document.getElementById("editStreet").value = a.streetAddress;
        document.getElementById("editCity").value = a.city;
        document.getElementById("editPostal").value = a.postalCode;
        document.getElementById("editCountry").value = a.country;
    } else {
        document.getElementById("editStreet").value = '';
        document.getElementById("editCity").value = '';
        document.getElementById("editPostal").value = '';
        document.getElementById("editCountry").value = '';
    }
    new bootstrap.Modal(document.getElementById("editAddressModal")).show();
}

function saveAddress() {
    const mode = document.getElementById("addressMode").value;
    const newAddress = {
        streetAddress: document.getElementById("editStreet").value,
        city: document.getElementById("editCity").value,
        postalCode: document.getElementById("editPostal").value,
        country: document.getElementById("editCountry").value
    };
    const url = mode === "edit"
        ? `/api/profile/address/update?id=${profile.addresses[document.getElementById("addresses").value].id}`
        : "/api/profile/address/add";
    fetch(url, {
        method: mode === "edit" ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newAddress)
    }).then(() => loadProfile())
        .finally(() => bootstrap.Modal.getInstance(document.getElementById("editAddressModal")).hide());
}

function deleteAddress() {
    const id = profile.addresses[document.getElementById("addresses").value].id;
    fetch(`/api/profile/address/delete?id=${id}`, {
        method: "DELETE"
    }).then(() => loadProfile());
}

document.addEventListener("DOMContentLoaded", loadProfile);
