$('#sign-up-btn').on('click', function() {
   var name = $('#name').val();
   var email = $('#email').val();
    var password = $('#password').val();
    
    $.ajax({
        method: 'POST',
        url: 'http://localhost:8080/EMS_Web_exploded/signup',
        contentType: 'application/json',
        data: JSON.stringify({
            uname: name,
            uemail: email,
            upassword: password
        }),
        success: function(response) {
            if (response.status === '200') {
                alert('Sign up successful!');
                window.location.href = 'signin';
            } else {
                alert('Error: ' + response.message);
            }
        },
    })
});