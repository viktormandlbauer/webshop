let currentPage = 0;
const pageSize = 6;

function fetchProducts(page) {
    const productGrid = document.getElementById('product-grid');
    const paginationControls = document.getElementById('pagination-controls');
    productGrid.innerHTML = '<p>Loading products...</p>';

    fetch(`/api/products/all?page=${page}&size=${pageSize}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Clear loading message
            productGrid.innerHTML = '';

            // Render products
            data.content.forEach(product => {
                const card = document.createElement('div');
                card.className = 'col-md-4 mb-4';
                card.innerHTML = `
                            <div class="card h-100">
                                <img src="${product.imageURL}" class="card-img-top" alt="${product.name}" style="height: 200px; object-fit: cover;">
                                <div class="card-body">
                                    <h5 class="card-title">${product.name}</h5>
                                    <p class="card-text">${product.description}</p>
                                    <p class="card-text"><strong>Category:</strong> ${product.categoryID.name}</p>
                                    <p class="card-text"><strong>Price:</strong> $${product.price.toFixed(2)}</p>
                                    <p class="card-text"><strong>Stock:</strong> ${product.stock}</p>
                                    <p class="card-text"><strong>Rating:</strong> ${product.avgRating.toFixed(2)} / 5</p>
                                    <a href="/products/${product.id}" class="btn btn-primary">View Details</a>
                                </div>
                            </div>
                        `;
                productGrid.appendChild(card);
            });

            // Render pagination controls
            paginationControls.innerHTML = `
                        <nav aria-label="Product pagination">
                            <ul class="pagination">
                                <li class="page-item ${data.first ? 'disabled' : ''}">
                                    <button class="page-link" onclick="fetchProducts(${data.number - 1})" ${data.first ? 'disabled' : ''}>Previous</button>
                                </li>
                                <li class="page-item disabled">
                                    <span class="page-link">Page ${data.number + 1} of ${data.totalPages}</span>
                                </li>
                                <li class="page-item ${data.last ? 'disabled' : ''}">
                                    <button class="page-link" onclick="fetchProducts(${data.number + 1})" ${data.last ? 'disabled' : ''}>Next</button>
                                </li>
                            </ul>
                        </nav>
                    `;
            currentPage = data.number;
        })
        .catch(error => {
            console.error('Error fetching products:', error);
            productGrid.innerHTML = '<p class="text-danger">Failed to load products. Please try again later.</p>';
            paginationControls.innerHTML = '';
        });
}

// Fetch the first page on load
document.addEventListener('DOMContentLoaded', () => {
    fetchProducts(currentPage);
});