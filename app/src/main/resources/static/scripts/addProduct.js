function showAlert(text, type = 'success') {
    const box = document.getElementById('alert');
    box.textContent = text;
    box.className = `alert alert-${type} mt-4`;
    box.classList.remove('d-none');
}

function loadCategories() {
    fetch('/api/categories')
        .then(r => r.json())
        .then(list => {
            const select = document.getElementById('category');
            const seen = new Set();
            list.forEach(c => {
                if (seen.has(c.name)) return;
                seen.add(c.name);
                const opt = document.createElement('option');
                opt.value = c.name;
                opt.textContent = c.name;
                select.appendChild(opt);
            });
        })
        .catch(e => showAlert('Kategorien konnten nicht geladen werden', 'danger'));
}

document.getElementById('imageFile').addEventListener('change', ev => {
    const file = ev.target.files[0];
    const img  = document.getElementById('preview');
    if (!file) {
        img.classList.add('d-none');
        return;
    }
    img.src = URL.createObjectURL(file);
    img.classList.remove('d-none');
});

document.getElementById('product-form').addEventListener('submit', ev => {
    ev.preventDefault();

    const formData = new FormData();
    formData.append('name', document.getElementById('name').value);
    formData.append('description', document.getElementById('description').value);
    formData.append('categoryName', document.getElementById('category').value);
    formData.append('stock', document.getElementById('stock').value);
    formData.append('price', document.getElementById('price').value);
    const imgFile = document.getElementById('imageFile').files[0];
    if (imgFile) formData.append('imageFile', imgFile);

    fetch('/api/products', {
        method: 'POST',
        body:   formData
    })
    .then(r => {
        if (!r.ok) throw new Error('Fehler beim Speichern');
        return r.json();
    })
    .then(prod => {
        showAlert(`Produkt "${prod.name}" wurde gespeichert.`);
        setTimeout(() => location.href = '/products/list', 1500);
    })
    .catch(err => showAlert(err.message || 'Unbekannter Fehler', 'danger'));
});


document.addEventListener('DOMContentLoaded', loadCategories);
