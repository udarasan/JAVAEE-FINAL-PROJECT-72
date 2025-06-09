// On document ready
$(document).ready(function () {
    const email = localStorage.getItem('email');
    if (!email) {
        window.location.href = 'signin.html';
    } else {
        $('#welcome-message').text('Welcome, ' + email);
        fetchEmployees();
    }
});

// Save employee with image upload
$('#save-employee').on('click', function () {
    const formData = new FormData();
    formData.append('ename', $('#ename').val());
    formData.append('enumber', $('#enumber').val());
    formData.append('eaddress', $('#eaddress').val());
    formData.append('edepartment', $('#edepartment').val());
    formData.append('estatus', $('#estatus').val());

    // Add the selected file
    const fileInput = $('#eimage')[0];
    if (fileInput.files.length > 0) {
        formData.append('eimage', fileInput.files[0]);
    } else {
        alert('Please select an image to upload.');
        return;
    }

    $.ajax({
        method: 'POST',
        url: 'http://localhost:8080/EMS_Web_exploded/employee',
        processData: false,
        contentType: false,
        data: formData,
        success: function (response) {
            if (response.code === '200') {
                alert('Employee saved successfully!');
                window.location.reload();
            } else {
                alert('Error: ' + response.message);
            }
        },
        error: function () {
            alert('Failed to save employee.');
        }
    });
});

// Fetch and render employee data
function fetchEmployees() {
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/EMS_Web_exploded/employee',
        success: function (response) {
            if (response.code === '200') {
                const employees = response.data;
                const employeeTable = $('#employee-table tbody');
                employeeTable.empty();

                employees.forEach(function (employee) {
                    employeeTable.append(`
                        <tr>
                            <td>
                                <button class="btn btn-primary" onclick="editEmployee('${employee.eid}')">Edit</button>
                                <button class="btn btn-danger" onclick="deleteEmployee('${employee.eid}')">Delete</button>
                            </td>
                            <td>${employee.ename}</td>
                            <td>${employee.enumber}</td>
                            <td>${employee.eaddress}</td>
                            <td>${employee.edepartment}</td>
                            <td>${employee.estatus}</td>
                            <td>
                                <img src="/assets/${employee.eimage}" alt="Employee Image" width="60" height="60" />
                            </td>
                        </tr>
                    `);
                });
            } else {
                alert('Error fetching employees: ' + response.message);
            }
        },
        error: function () {
            alert('Failed to fetch employees.');
        }
    });
}
