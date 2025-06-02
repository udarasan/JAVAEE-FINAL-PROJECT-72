//window open
$(document).ready(function() {
    // Check if user is logged in
    var email = localStorage.getItem('email');
    if (!email) {
        window.location.href = 'signin.html';
    } else {
        $('#welcome-message').text('Welcome, ' + email);
    }
});

$('#save-employee').on('click', function() {
    var ename = $('#ename').val();
    var enumber = $('#enumber').val();
    var edepartment = $('#edepartment').val();
    var estatus= $('#estatus').val();
    var eaddress = $('#eaddress').val();
    
    $.ajax({
        method: 'POST',
        url: 'http://localhost:8080/EMS_Web_exploded/employee',
        contentType: 'application/json',
        data: JSON.stringify({
            ename: ename,
            enumber: enumber,
            eaddress: eaddress,
            edepartment: edepartment,
            estatus: estatus
        }),
        success: function(response) {
            console.log(response);
            if (response.code === '200') {
                alert('Employee saved successfully!');
                window.location.reload(); // Reload the page to see the updated list
            } else {
                alert('Error: ' + response.message);
            }
        },
    });
});