//function downloadExcel() {
//    console.log("Download Excel button clicked");
//    fetch('/3g/downloadUsersExcel')
//        .then(response => {
//            if (!response.ok) {
//                throw new Error(`HTTP error! status: ${response.status}`);
//            }
//            return response.blob();
//        })
//        .then(blob => {
//            const url = window.URL.createObjectURL(blob);
//            const a = document.createElement('a');
//            a.style.display = 'none';
//            a.href = url;
//            a.download = 'users.xlsx';
//            document.body.appendChild(a);
//            a.click();
//            window.URL.revokeObjectURL(url);
//        })
//        .catch(error => console.error('Fetch error:', error));
//}
document.addEventListener('DOMContentLoaded', function() {
    // Function to fetch council details
    fetch('/3g/getCouncilDetails')
        .then(response => response.json())
        .then(data => {
            // Check if data is an array and has at least one element
            if (Array.isArray(data) && data.length > 0 && data[0].standardName) {
                // Set the council name in the navbar if available
                document.getElementById('councilName').textContent = data[0].standardName;
            } else {
                console.error('Council details not found or response is invalid.');
                // Set a default name if there's an issue with fetching
                document.getElementById('councilName').textContent = '3G Associates';
            }
        })
        .catch(error => {
            console.error('Error fetching council details:', error);
            // Set a default name in case of an error
            document.getElementById('councilName').textContent = '3G Associates';
        });
});
async function fetchUsers() {
    try {
        const response = await fetch('/3g/getAllUsers'); // Use the full URL if needed, like http://localhost:8080/api/users
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const users = await response.json();

        const tableBody = document.getElementById('userTable').getElementsByTagName('tbody')[0];
        tableBody.innerHTML = ''; // Clear existing rows

        users.forEach(user => {
            let row = tableBody.insertRow();
            if (user.status === 'Inactive') {
                row.classList.add('inactive-user');
            }else if(user.status === 'Active'){
                row.classList.add('active-user');
            }
            row.innerHTML = `
                <td><input type="text" name="username" value="${user.username}" readonly disabled></td>
                <td><input type="text" name="password" value="${user.password}" readonly disabled></td>
                <td><input type="text" name="firstname" value="${user.firstname}" disabled></td>
                <td><input type="text" name="lastname" value="${user.lastname}" disabled></td>
                <td><input type="text" name="email" value="${user.email}" disabled></td>
                <td><input type="text" name="phone" value="${user.phone}" disabled></td>
                <td>
                    <select name="profile" disabled>
                        <option value="ITA" ${user.profile === 'ITA' ? 'selected' : ''}>ITA</option>
                        <option value="ITL" ${user.profile === 'ITL' ? 'selected' : ''}>ITL</option>
                        <option value="IT" ${user.profile === 'IT' ? 'selected' : ''}>IT</option>
                        <option value="ACTL" ${user.profile === 'ACTL' ? 'selected' : ''}>ACTL</option>
                        <option value="ACL" ${user.profile === 'ACL' ? 'selected' : ''}>ACL</option>
                        <option value="DETL" ${user.profile === 'DETL' ? 'selected' : ''}>DETL</option>
                        <option value="DEL" ${user.profile === 'DEL' ? 'selected' : ''}>DEL</option>
                    </select>
                </td>
                <td>
                    <select name="status" disabled>
                        <option value="Active" ${user.status === 'Active' ? 'selected' : ''}>Active</option>
                        <option value="Inactive" ${user.status === 'Inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </td>
                <td>
                    <button class="edit-btn" onclick="editUser(this)">Edit</button>
                    <button class="delete-btn" onclick="deleteUserByUsername('${user.username}')">Delete</button>
                </td>
            `;
        });
    } catch (error) {
        console.error('Fetch error: ', error);
    }
}

function editUser(buttonElement) {
        const row = buttonElement.closest('tr');
        const editableFields = row.querySelectorAll('input, select');

        // Check if we are in edit mode or save mode
        if (buttonElement.textContent === 'Edit') {
            editableFields.forEach(field => {
                if (field.name !== 'username' && field.name !== 'password') {
                    field.removeAttribute('disabled');
                }
            });

            // Change the button to "Save"
            buttonElement.textContent = 'Save';
            buttonElement.setAttribute('onclick', `saveUser(this)`);

            // Focus the first editable field
            if (editableFields.length > 0) {
                editableFields[0].focus();
            }
        }
    }

    function saveUser(buttonElement) {
        const row = buttonElement.closest('tr');
        const editableFields = row.querySelectorAll('input, select');

        // Collect the data to save
        const userData = {
            username: row.querySelector('[name="username"]').value,
            firstname: row.querySelector('[name="firstname"]').value,
            lastname: row.querySelector('[name="lastname"]').value,
            email: row.querySelector('[name="email"]').value,
            phone: row.querySelector('[name="phone"]').value,
            profile: row.querySelector('[name="profile"]').value,
            status: row.querySelector('[name="status"]').value,
        };

        // Disable fields after editing
        editableFields.forEach(field => field.setAttribute('disabled', true));

        // Change the button back to "Edit" and reattach the edit event
        buttonElement.textContent = 'Edit';
        buttonElement.onclick = () => editUser(buttonElement);

        // Implement the save functionality or AJAX call here
        console.log('Saving user data:', userData);

        // Hit the API endpoint to update user data
        fetch('/3g/updateUser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok.');
            }
            return response.json();
        })
        .then(data => {
            console.log('Success:', data);
            // After saving, you might want to confirm the action with the user, e.g., via an alert or updating the UI.
            alert('User updated successfully!');
            window.location.reload();
            editableFields.forEach(field => field.setAttribute('disabled', true));
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('Error updating user.');
        })
        .finally(() => {
            // Re-enable fields or finalize UI updates as needed after the save attempt
        });
    }


  function deleteUserByUsername(username) {
    const confirmDelete = confirm(`Are you sure you want to delete the user with username '${username}'?`);
    if (confirmDelete) {
        fetch(`/3g/deleteUser/${username}`, { // Adjust the URL as needed
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert(`User with username '${username}' deleted successfully.`);
                window.location.reload();
                fetchUsers();
                // Optionally, refresh the list of users or redirect as needed
            } else {
                alert(`Failed to delete the user with username '${username}'.`);
            }
        })
        .catch(error => console.error('Error:', error));
    }
}


 function searchUsers() {
    const input = document.getElementById('searchBox');
    const filter = input.value.toUpperCase();
    const table = document.getElementById("userTable");
    const tbody = table.querySelector("thead");
    const tr = table.getElementsByTagName("tr");

    for (let i = 0; i < tr.length; i++) {
        let tdArray = tr[i].getElementsByTagName("td");
        let match = false;
        for (let j = 0; j < tdArray.length; j++) {
            let td = tdArray[j];
            if (td) {
                if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
                    match = true;
                    break; // Stop searching through the rest of the cells in this row
                }
            }
        }
        if (match) {
            tr[i].style.display = "";
        } else {
            tr[i].style.display = "none";
        }
    }
}

document.getElementById('searchBox').addEventListener('keyup', searchUsers);
document.addEventListener('DOMContentLoaded', fetchUsers);