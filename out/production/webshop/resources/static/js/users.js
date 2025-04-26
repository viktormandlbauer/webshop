async function fetchJson(url) {
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        const data = await response.json();
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }
}

function insertUsers() {

    var fetchedUsers = fetchJson('/users')
    const userTable = document.getElementById('userTable');

    userTable.innerHTML = '';

    fetchedUsers.forEach((user) => {
        const row = document.createElement('tr');

        const idCell = document.createElement('td');
        idCell.textContent = user.id;
        row.appendChild(idCell);

        const usernameCell = document.createElement('td');
        usernameCell.textContent = user.username;
        row.appendChild(usernameCell);
    })
}

document.addEventListener('DOMContentLoaded', () => {

});
