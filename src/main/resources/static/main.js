function createTable(user) {
    let userList = '<tr id=' + user.id + '>';
    userList += '<td>' + user.id + '</td>';
    userList += '<td>' + user.name + '</td>';
    userList += '<td>' + user.lastName + '</td>';
    userList += '<td>' + user.age + '</td>';
    userList += '<td>' + user.email + '</td>';
    userList += '<td>';
    let roles = user.roles;
    for (let r of roles) {
        userList += r.role.replace('ROLE_','') + ' ';
    }
    userList += '</td>' +
        '<td>' + '<input id="btnEdit" value="Edit" type="button" ' +
        'class="btn-info btn edit-btn" data-toggle="modal" data-target="#updateModal" ' +
        'data-id="' + user.id + '">' + '</td>' +
        '<td>' + '<input id="btnDelete" value="Delete" type="button" class="btn btn-danger del-btn" ' +
        'data-toggle="modal" data-target="#deleteModal" data-id=" ' + user.id + ' ">' + '</td>';
    userList += '</tr>';
    return userList;
}

let roleSet = []

function getAllUsers() {
    $.getJSON("api/admin", function (data) {
        let rows = '';
        $.each(data, function (key, user) {
            rows += createTable(user);
        });
        $('#listUsers').append(rows);

        $.ajax({
            url: '/api/admin/roles',
            method: 'GET',
            dataType: 'json',
            success: function (roles) {
                roleSet = roles;
            }
        });
    });
}

getAllUsers()

$("#addUser").on('click', () => {
    let roles = []
    let selectedRoles = document.querySelector('#selectedRoles').options
    for (let i = 0; i < selectedRoles.length; i++) {
        if (selectedRoles[i].selected) {
            let role = {id: selectedRoles[i].value, role: selectedRoles[i].text}
            roles.push(role)
            console.log(roles)
        }
    }
    $.ajax({
        url: 'api/admin',
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify({
            name: $('#name').val(),
            lastName: $('#lastName').val(),
            age: $('#age').val(),
            email: $('#email').val(),
            password: $('#password').val(),
            roles: roles
        }),

        success: function () {
            $('#listUsers').empty();
            getAllUsers();
            $('#nav-home-tab').tab('show');
        }
    });
    document.forms.reset();
});

function editUser(id) {
    fetch("/api/admin/" + id, {method: "GET", dataType: 'json'})
        .then((response) => {
            response.json().then((user) => {
                $('#editId').val(user.id);
                $('#editName').val(user.name);
                $('#editLastName').val(user.lastName);
                $('#editAge').val(user.age);
                $('#editEmail').val(user.email);
                $('#editPassword').empty();
                $('#editRole').empty();
                roleSet.map(r => {
                    let selected = user.role.find(item => item.id === r.id) ? 'selected' : '';
                    $('#editRole').append('<option id="' + r.id + '" ' + selected + ' name="' + r.role + '" >' +
                        r.role + '</option>');
                })
            })
        })
}

$(document).on('click', '.edit-btn', function () {
    editUser($(this).attr('data-id'));
})

$('#update').on('click', (event) => {
    event.preventDefault()
    let roles = []
    $.each($("select[name='editRoles'] option:selected"), function () {
        let role = {};
        role.id = $(this).attr('id');
        role.role = $(this).attr('name');
        roles.push(role);
    });
    $.ajax({
        url: '/api/admin',
        method: 'PUT',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify({
            id: $("input[name='id']").val(),
            name: $("input[name='name']").val(),
            lastName: $("input[name='lastName']").val(),
            age: $("input[name='age']").val(),
            email: $("input[name='email']").val(),
            password: $("input[name='password']").val(),
            roles: roles
        }),
        success: (response) => {
            let newRow = createTable(response);
            $('#listUsers').find('#' + $('#editId').val()).replaceWith(newRow);
        }
    });
});

$(document).on('click', '.del-btn', function () {
    fetch("/api/admin/" + $(this).attr('data-id'), {method: "GET", dataType: 'json'})
        .then((response) => {
            response.json().then((user) => {
                $('#delId').val(user.id);
                $('#delName').val(user.name);
                $('#delLastName').val(user.lastName);
                $('#delAge').val(user.age);
                $('#delEmail').val(user.email);
                $('#delPassword').val(user.password);
                $('#delRole').empty();
                roleSet.map(r => {
                    let selected = user.role.find(item => item.id === r.id) ? 'selected' : '';
                    $('#delRole').append('<option id="' + r.id + '" ' + selected + ' name="' + r.role + '" >' +
                        r.role + '</option>')
                })
            })
        })
})

$('#delete').on('click', (event) => {
    event.preventDefault()
    $.ajax({
        url: '/api/admin/' + $('#delId').val(),
        method: 'DELETE',
        success: function () {
            $('#' + $('#delId').val()).remove();
        }
    });
});