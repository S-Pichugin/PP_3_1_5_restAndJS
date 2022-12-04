let editModal77 = new bootstrap.Modal(document.getElementById('modalEdit'));
let deleteModal77 = new bootstrap.Modal(document.getElementById('deleteModal'));

const url = 'http://localhost:8080/api/users/';
const renderTable = document.getElementById("all_users");


//Таблица всех пользователей

const renderPosts = (users) => {
    let temp = '';
    users.forEach((u) => {
        temp += `<tr>
                                <td>${u.id}</td>
                                <td id=${'name' + u.id}>${u.username}</td>
                                <td id=${'surname' + u.id}>${u.surname}</td>
                                <td id=${'age' + u.id}>${u.age}</td>
                                <td id=${'email' + u.id}>${u.email}</td>
                                <td id=${'role' + u.id}>${u.roles.map(role => role.name).join(' ').substring(5)}</td>
                                <td>
                                <button class="btn btn-info" type="button"
                                data-bs-toggle="modal"
                                onclick="editModal(${u.id})">Edit</button></td>
                                <td>
                                <button class="btn btn-danger" type="button"
                                data-toggle="modal"
                                onclick="deleteModal(${u.id})">Delete</button></td>
                                </tr>
                                `
    })
    renderTable.innerHTML = temp;
}

function getAllUsers() {
    fetch(url)
        .then(res => res.json())
        .then(data => {
            renderPosts(data)
        })
}

getAllUsers()


//EDITMODAL

function editModal(id) {
    fetch(url + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => res.json()).then(us => {
        console.log(us);
        document.getElementById('editId').value = us.id;
        document.getElementById('editName').value = us.username;
        document.getElementById('editSurname').value = us.surname;
        document.getElementById('editAge').value = us.age;
        document.getElementById('editEmail').value = us.email;
        document.getElementById('editPassword').value = us.password;
        editModal77.show()
    })
    document.getElementById('us').addEventListener('submit', submitFormEditUser);
}

function submitFormEditUser() {
    let idValue = document.getElementById("editId").value;
    let nameValue = document.getElementById("editName").value;
    let surnameValue = document.getElementById("editSurname").value;
    let ageValue = document.getElementById("editAge").value;
    let emailValue = document.getElementById("editEmail").value;
    let passwordValue = document.getElementById("editPassword").value;
    let roles = Array.from(document.getElementById("editRoles").selectedOptions).map(role => role.value);

    let user = {
        id: idValue,
        username: nameValue,
        surname: surnameValue,
        age: ageValue,
        email: emailValue,
        password: passwordValue,
        roles: roles
    }

    fetch('http://localhost:8080/api', {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(user)
    }).then(data => {
        const dataArr = [];
        dataArr.push(data);
        getAllUsers(data);
    }).then(() => {
        document.getElementById("allUsers77").click();
    })
    editModal77.hide();
}

//DELETE MODAL
function deleteModal(id) {
    fetch(url + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => res.json()).then(us => {
        console.log(us);
        document.getElementById('deleteId').value = us.id;
        document.getElementById('deleteUsername').value = us.username;
        document.getElementById('deleteSurname').value = us.surname;
        document.getElementById('deleteAge').value = us.age;
        document.getElementById('deleteEmail').value = us.email;
        if (us.roles.map(role => role.name).join(' ').includes("USER") && us.roles.map(role => role.name).join(' ').includes("ADMIN")) {
            document.getElementById('deleteRoles1').setAttribute('selected', 'true');
            document.getElementById('deleteRoles2').setAttribute('selected', 'true');
        } else if (us.roles.map(role => role.name).join(' ').includes("USER")) {
            document.getElementById('deleteRoles1').setAttribute('selected', 'true');
        } else if (us.roles.map(role => role.name).join(' ').includes("ADMIN")) {
            document.getElementById('deleteRoles2').setAttribute('selected', 'true');
        }
        deleteModal77.show()
    })
    document.getElementById('deleteForm').addEventListener('submit', submitFormDeleteUser);
}

function submitFormDeleteUser() {
    let idValue = document.getElementById("deleteId").value;
    let nameValue = document.getElementById("deleteUsername").value;
    let surnameValue = document.getElementById("deleteSurname").value;
    let ageValue = document.getElementById("deleteAge").value;
    let emailValue = document.getElementById("deleteEmail").value;
    let roles = Array.from(document.getElementById("deleteRoles").selectedOptions).map(role => role.value);

    let user = {
        id: idValue,
        username: nameValue,
        surname: surnameValue,
        age: ageValue,
        email: emailValue,
        roles: roles
    }

    fetch('http://localhost:8080/api/users', {
        method: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(user)
    }).then(data => {
        const dataArr = [];
        dataArr.push(data);
        getAllUsers(data);
    }).then(() => {
        document.getElementById("allUsers77").click();
    })
    deleteModal77.hide();
}

//TAB NEWUSER


document.getElementById("newUser").addEventListener('submit', (e) => {
    e.preventDefault();
    let nameValue = document.getElementById("name").value;
    let surnameValue = document.getElementById("surname").value;
    let ageValue = document.getElementById("age").value;
    let emailValue = document.getElementById("email").value;
    let passwordValue = document.getElementById("password").value;
    let roles = Array.from(document.getElementById("roleSelect").selectedOptions).map(role => role.value);
    let newUser = {
        username: nameValue,
        surname: surnameValue,
        age: ageValue,
        email: emailValue,
        password: passwordValue,
        roles: roles,

    }
    fetch(url, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(newUser)
    })
        .then(data => {
            const dataArr = [];
            dataArr.push(data);
            getAllUsers(data);
        }).then(() => {
        document.getElementById("allUsers77").click();
    })
})

function adminAuthInfo() {
    fetch('http://localhost:8080/api/current')
        .then((res) => res.json())
        .then((u) => {
            let temp = '';
            temp += `<tr>
            <td>${u.id}</td>
            <td>${u.username}</td>
            <td>${u.surname}</td>
            <td>${u.age}</td>
            <td>${u.email}</td>
            <td>${u.roles.map(role => role.name).join(' ').substring(5)}</td>
            </tr>`;
            document.getElementById("one_admin").innerHTML = temp;
            document.getElementById("user_panel").innerHTML = `<h5>${u.email} with roles: ${u.roles.map(role => role.name).join(' ').substring(5)}</h5>`
        });
}

adminAuthInfo()