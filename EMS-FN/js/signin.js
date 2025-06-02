$('#sign-in-btn').on('click', function() {
    var email = $('#email').val();
    var password = $('#password').val();
    
    $.ajax({
        method: 'POST',
        url: 'http://localhost:8080/EMS_Web_exploded/signin',
        contentType: 'application/json',
        data: JSON.stringify({
            uemail: email,
            upassword: password
        }),
        success: function(response) {
            console.log(response);
            if (response.code === '200') {
                localStorage.setItem('email', email);
                window.location.href = 'dashboard.html';
            } else {
                alert('Error: ' + response.message);
            }
        },
    });
});