let profile = {};

function loadProfile() {
    fetch("/api/profile")
        .then(response => response.json())
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


            const paymentSelect = document.getElementById("paymentMethods");
            paymentSelect.innerHTML = "";
            profile.paymentMethods.forEach((method, index) => {
                const opt = document.createElement("option");
                opt.value = index;
                opt.textContent = `${method.cardHolderName} - ${method.cardNumber}`;
                paymentSelect.appendChild(opt);
            });

            const addressSelect = document.getElementById("addresses");
            addressSelect.innerHTML = "";
            profile.addresses.forEach((address, index) => {
                const opt = document.createElement("option");
                opt.value = index;
                opt.textContent = `${address.streetAddress}, ${address.city}`;
                addressSelect.appendChild(opt);
            });

            paymentSelect.dispatchEvent(new Event("change"));
            addressSelect.dispatchEvent(new Event("change"));
        });
}

document.addEventListener("DOMContentLoaded", () => {
    loadProfile();

    document.getElementById("paymentMethods").addEventListener("change", () => prefillPaymentForm());
    document.getElementById("addresses").addEventListener("change", () => prefillAddressForm());
});

function prefillPaymentForm() {
    const index = document.getElementById("paymentMethods").value;
    const selected = profile.paymentMethods[index];
    document.getElementById("cardHolderName").textContent = selected.cardHolderName;
    document.getElementById("cardNumber").textContent = selected.cardNumber;
    document.getElementById("expiryDate").textContent = selected.expiryDate;
    document.getElementById("cvv").textContent = selected.cvv;
    document.getElementById("editCardHolder").value = selected.cardHolderName;
    document.getElementById("editCardNumber").value = selected.cardNumber;
    document.getElementById("editExpiry").value = selected.expiryDate;
    document.getElementById("editCVV").value = selected.cvv;
}

function prefillAddressForm() {
    const index = document.getElementById("addresses").value;
    const selected = profile.addresses[index];
    document.getElementById("streetAddress").textContent = selected.streetAddress;
    document.getElementById("cityDetails").textContent = selected.city;
    document.getElementById("postalCodeDetails").textContent = selected.postalCode;
    document.getElementById("countryDetails").textContent = selected.country;
    document.getElementById("editStreet").value = selected.streetAddress;
    document.getElementById("editCity").value = selected.city;
    document.getElementById("editPostal").value = selected.postalCode;
    document.getElementById("editCountry").value = selected.country;
}

function validatePaymentForm() {
    return document.getElementById("editCardHolder").value &&
        document.getElementById("editCardNumber").value &&
        document.getElementById("editExpiry").value &&
        document.getElementById("editCVV").value;
}

function validateAddressForm() {
    return document.getElementById("editStreet").value &&
        document.getElementById("editCity").value &&
        document.getElementById("editPostal").value &&
        document.getElementById("editCountry").value;
}

function savePaymentMethod() {
    if (!validatePaymentForm()) return alert("Bitte alle Felder ausfüllen.");
    const index = document.getElementById("paymentMethods").value;
    const id = profile.paymentMethods[index].id;
    const method = {
        cardHolderName: document.getElementById("editCardHolder").value,
        cardNumber: document.getElementById("editCardNumber").value,
        expiryDate: document.getElementById("editExpiry").value,
        cvv: document.getElementById("editCVV").value
    };
    fetch(`/api/profile/payment-method/update?id=${id}`, {
        method: "PUT",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(method)
    }).then(loadProfile);
    bootstrap.Modal.getInstance(document.getElementById("editPaymentModal")).hide();
}

function addPaymentMethod() {
    if (!validatePaymentForm()) return alert("Bitte alle Felder ausfüllen.");
    const method = {
        cardHolderName: document.getElementById("editCardHolder").value,
        cardNumber: document.getElementById("editCardNumber").value,
        expiryDate: document.getElementById("editExpiry").value,
        cvv: document.getElementById("editCVV").value
    };
    fetch(`/api/profile/payment-method/add`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(method)
    }).then(loadProfile);
    bootstrap.Modal.getInstance(document.getElementById("editPaymentModal")).hide();
}

function deletePaymentMethod() {
    if (!confirm("Diese Zahlungsmethode wirklich löschen?")) return;
    const index = document.getElementById("paymentMethods").value;
    const id = profile.paymentMethods[index].id;
    fetch(`/api/profile/payment-method/delete?paymentMethodId=${id}`, {
        method: "DELETE"
    }).then(loadProfile);
}

function saveAddress() {
    if (!validateAddressForm()) return alert("Bitte alle Felder ausfüllen.");
    const index = document.getElementById("addresses").value;
    const id = profile.addresses[index].id;
    const address = {
        streetAddress: document.getElementById("editStreet").value,
        city: document.getElementById("editCity").value,
        postalCode: document.getElementById("editPostal").value,
        country: document.getElementById("editCountry").value
    };
    fetch(`/api/profile/address/update?id=${id}`, {
        method: "PUT",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(address)
    }).then(loadProfile);
    bootstrap.Modal.getInstance(document.getElementById("editAddressModal")).hide();
}

function addAddress() {
    if (!validateAddressForm()) return alert("Bitte alle Felder ausfüllen.");
    const address = {
        streetAddress: document.getElementById("editStreet").value,
        city: document.getElementById("editCity").value,
        postalCode: document.getElementById("editPostal").value,
        country: document.getElementById("editCountry").value
    };
    fetch(`/api/profile/address/add`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(address)
    }).then(loadProfile);
    bootstrap.Modal.getInstance(document.getElementById("editAddressModal")).hide();
}

function deleteAddress() {
    if (!confirm("Diese Adresse wirklich löschen?")) return;
    const index = document.getElementById("addresses").value;
    const id = profile.addresses[index].id;
    fetch(`/api/profile/address/delete?id=${id}`, {
        method: "DELETE"
    }).then(loadProfile);
}

function clearPaymentForm() {
    document.getElementById("editCardHolder").value = "";
    document.getElementById("editCardNumber").value = "";
    document.getElementById("editExpiry").value = "";
    document.getElementById("editCVV").value = "";
}

function clearAddressForm() {
    document.getElementById("editStreet").value = "";
    document.getElementById("editCity").value = "";
    document.getElementById("editPostal").value = "";
    document.getElementById("editCountry").value = "";
}
function openUserEditModal() {
    document.getElementById("editEmail").value = profile.email;
    document.getElementById("editFirstName").value = profile.firstName;
    document.getElementById("editLastName").value = profile.lastName;
    document.getElementById("editDateOfBirth").value = profile.dateOfBirth;

    new bootstrap.Modal(document.getElementById("editUserModal")).show();
}

function saveUserData() {
    const updatedUser = {
        email: document.getElementById("editEmail").value,
        firstName: document.getElementById("editFirstName").value,
        lastName: document.getElementById("editLastName").value,
        dateOfBirth: document.getElementById("editDateOfBirth").value
    };

    fetch("/api/profile/update", {
        method: "PUT",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedUser)
    })
        .then(response => {
            if (!response.ok) throw new Error("Fehler beim Speichern");
            return response.json();
        })
        .then(() => {
            alert("Benutzerdaten gespeichert.");
            bootstrap.Modal.getInstance(document.getElementById("editUserModal")).hide();
            loadProfile();
        })
        .catch(err => alert("Fehler: " + err.message));
}
function openChangePasswordModal() {
    document.getElementById("currentPassword").value = "";
    document.getElementById("newPassword").value = "";
    document.getElementById("repeatPassword").value = "";
    new bootstrap.Modal(document.getElementById("changePasswordModal")).show();
}

function changePassword() {
    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const repeatPassword = document.getElementById("repeatPassword").value;

    if (!currentPassword || !newPassword || !repeatPassword) {
        return alert("Bitte alle Felder ausfüllen.");
    }
    if (newPassword !== repeatPassword) {
        return alert("Die neuen Passwörter stimmen nicht überein.");
    }

    fetch("/api/profile/change-password", {
        method: "PUT",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ currentPassword, newPassword })
    })
        .then(response => {
            if (!response.ok) throw new Error("Fehler beim Ändern des Passworts");
            return response.json();
        })
        .then(() => {
            alert("Passwort erfolgreich geändert.");
            bootstrap.Modal.getInstance(document.getElementById("changePasswordModal")).hide();
        })
        .catch(err => alert("Fehler: " + err.message));
}


function openBillingEditModal() {
    document.getElementById("billingCountry").value = profile.country || "";
    document.getElementById("billingAddress").value = profile.address || "";
    document.getElementById("billingPostalCode").value = profile.postalCode || "";
    document.getElementById("billingCity").value = profile.city || "";

    new bootstrap.Modal(document.getElementById("editBillingModal")).show();
}

function saveBillingAddress() {

        const updatedBilling = {
            billingCountry: document.getElementById("billingCountry").value,
            billingAddress: document.getElementById("billingAddress").value,
            billingPostalCode: document.getElementById("billingPostalCode").value,
            billingCity: document.getElementById("billingCity").value
        };



    fetch("/api/profile/update-billing", {
        method: "PUT",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedBilling)
    })
        .then(response => {
            if (!response.ok) throw new Error("Fehler beim Speichern der Adresse");
            return response.json();
        })
        .then(() => {
            alert("Rechnungsadresse aktualisiert.");
            bootstrap.Modal.getInstance(document.getElementById("editBillingModal")).hide();
            loadProfile();
        })
        .catch(err => alert("Fehler: " + err.message));
}


