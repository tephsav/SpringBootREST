let apiRequestsPath = 'api/requests/';

function createRequestsTable(request) {
    let userRequestsList = '<tr id=' + request.id + '>';
    userRequestsList += '<td>' + request.id + '</td>';
    userRequestsList += '<td>' + request.user.name + '</td>';
    userRequestsList += '<td>' + request.user.lastName + '</td>';
    userRequestsList += '<td>' + request.user.email + '</td>';
    userRequestsList += '<td>' + request.user.roles[0].name.replace('ROLE_','') + ' ';
    userRequestsList += '</td>' +
        '<td>' + '<input id="btnAccept" value="Accept" type="button" ' +
        'class="btn btn-success accept-btn" data-toggle="modal" data-target="#acceptModal" ' +
        'data-id="' + request.id + '">' + '</td>' +
        '<td>' + '<input id="btnDecline" value="Decline" type="button" ' +
        'class="btn btn-danger decline-btn" data-toggle="modal" data-target="#declineModal" ' +
        'data-id="' + request.id + '">' + '</td>';
    userRequestsList += '</tr>';
    return userRequestsList;
}

function getAllRequests() {
    $.getJSON(apiRequestsPath, function (data) {
        let rows = '';
        $.each(data, function (key, request) {
            rows += createRequestsTable(request);
        });
        $('#listRequests').append(rows);

        $.ajax({
            url: apiRequestsPath,
            method: 'GET',
            dataType: 'json'
        });
    });
}

$(document).ready(function() {
    getAllRequests();
});

$("#becomeAdmin").click(function() {
    let idUser = $("#userTable").find("td").eq(0).text();

    $.ajax({
        url: apiRequestsPath + idUser,
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify({
            id: idUser
        }),

        success: function () {
            $('#listRequests').empty();
            getAllRequests();
            $('#nav-request-tab').tab('show');

        }
    });

    $(this).prop('disabled', true);
    alert("Заявка принята, ожидайте ответа");
});

$("#requestTable").on('click','#btnDecline', function(){
    let idRequest = $(this).closest('tr').find("td").eq(0).text();

    $.ajax({
        url: apiRequestsPath + "decline/" + idRequest,
        method: 'DELETE',
        success: function () {
            $('#listRequests').empty();
            getAllRequests();
            $('#nav-request-tab').tab('show');
        }
    });
});

$("#requestTable").on('click', '#btnAccept', function() {
    let currentRequestRow = $(this).closest("tr");
    let idRequest = currentRequestRow.find("td").eq(0).text();

    $.ajax({
        url: apiRequestsPath + "accept/" + idRequest,
        method: 'DELETE',
        success: function() {
            $('#listUsers').empty();
            getAllUsers();
            currentRequestRow.remove();
            $('#nav-request-tab').tab('show');
        }
    });
});