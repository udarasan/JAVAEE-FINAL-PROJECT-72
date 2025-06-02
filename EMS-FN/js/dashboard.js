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