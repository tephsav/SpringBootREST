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

function createRequestsTable(user) {
    let userRequestsList = '<tr id=' + user.id + '>';
    userRequestsList += '<td>' + user.id + '</td>';
    userRequestsList += '<td>' + user.name + '</td>';
    userRequestsList += '<td>' + user.lastName + '</td>';
    userRequestsList += '<td>' + user.email + '</td>';
    userRequestsList += '<td>';
    let roles = user.roles;
    for (let r of roles) {
        userRequestsList += r.role.replace('ROLE_','') + ' ';
    }
    userRequestsList += '</td>' +
        '<td>' + '<input id="btnAccept" value="Accept" type="button" ' +
        'class="btn-success btn accept-btn" data-toggle="modal" data-target="#acceptModal" ' +
        'data-id="' + user.id + '">' + '</td>' +
        '<td>' + '<input id="btnDecline" value="Decline" type="button" ' +
        'class="btn btn-danger decline-btn" data-toggle="modal" data-target="#declineModal" ' +
        'data-id=" ' + user.id + ' ">' + '</td>';
    userRequestsList += '</tr>';
    return userRequestsList;
}

let roleSet = []
let apiUsersPath = 'api/users/'

function getAllUsers() {
    $.getJSON(apiUsersPath, function (data) {
        let rows = '';
        $.each(data, function (key, user) {
            rows += createTable(user);
        });
        $('#listUsers').append(rows);

        $.ajax({
            url: apiUsersPath + 'roles',
            method: 'GET',
            dataType: 'json',
            success: function (roles) {
                roleSet = roles;
            }
        });
    });
}

function getAllRequests() {
    $.getJSON(apiUsersPath + 'only_users', function (data) {
        let rows = '';
        $.each(data, function (key, user) {
            rows += createRequestsTable(user);
        });
        $('#listRequests').append(rows);

        $.ajax({
            url: apiUsersPath + 'roles',
            method: 'GET',
            dataType: 'json',
            success: function (roles) {
                roleSet = roles;
            }
        });
    });
}

getAllUsers()
getAllRequests()

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
        url: apiUsersPath,
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
    fetch(apiUsersPath + id, {method: "GET", dataType: 'json'})
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
        url: apiUsersPath,
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
    fetch(apiUsersPath + $(this).attr('data-id'), {method: "GET", dataType: 'json'})
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
        url: apiUsersPath + $('#delId').val(),
        method: 'DELETE',
        success: function () {
            $('#' + $('#delId').val()).remove();
        }
    });
});

$('#btnDecline').on('click', (event) => {
    event.preventDefault()
    $.ajax({
        url: apiUsersPath + "decline/" + $(this).parent("listRequests").index();
        method: 'DELETE',
        success: function () {
            $("#listRequests").on("click", function() {
               $(this).closest("tr").remove();
            });
        }
    });
});

$("#btnAccept").on('click', (event) => {
    event.preventDefault()
    $.ajax({
        url: apiUsersPath + "accept/" + $(this).parent("listRequests").index();
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
            $('#listRequests').empty();
            getAllRequests();
            $('#nav-request-tab').tab('show');
        }
    });
    document.forms.reset();
});