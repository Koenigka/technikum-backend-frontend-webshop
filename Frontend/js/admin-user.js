// Function to close the edit window
function closeEditWindow() {
  const addEditUser = $("#addEditUser");
  addEditUser.empty();
  $(".footer").addClass("fixed-bottom");
}

function validateUser(user, validAddresses) {
  return new Promise(function (resolve, reject) {
    // Clear error messages
    $(".input-error").text("");

    // Validate input values
    var isValid = true;

    // Helper regex to validate different format
    var name_regex = /^[a-zA-Z]+$/;
    var zip_regex = /^[0-9]+$/;
    var email_regex =
      /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    var password_regex =
      /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()])[a-z\d!@#$%^&*()]+$/;

    // Validation for all the editing inputs

    // Validation for the firstname
    if (user.firstname === "") {
      isValid = false;
      $("#firstNameError").text("Please enter the first name");
    } else if (user.firstname.length < 2) {
      isValid = false;
      $("#firstNameError").text(
        "The first name should have at least 2 letters"
      );
    } else if (!user.firstname.match(name_regex)) {
      isValid = false;
      $("#firstNameError").text("The first name should have only alphabets");
    }

    // Validation for the lastname
    if (user.lastname === "") {
      isValid = false;
      $("#lastNameError").text("Please enter the last name");
    } else if (user.lastname.length < 2) {
      isValid = false;
      $("#lastNameError").text("The last name should have at least 2 letters");
    } else if (!user.lastname.match(name_regex)) {
      isValid = false;
      $("#lastNameError").text("The last name should contain only letters");
    }

    // Validation for the address
    if (user.address === "") {
      isValid = false;
      $("#addressError").text("Please enter an address");
    } else if (!/^.*(?=.*\d)(?=.*[a-zA-Z]).{4,}$/.test(user.address)) {
      isValid = false;
      $("#addressError").text(
        "Address should contain at least 4 letters and numbers"
      );
    }

    // Validation for the state
    if (user.state === "") {
      isValid = false;
      $("#stateError").text("Please enter a state");
    }

    // Validation for the city
    if (user.city === "") {
      isValid = false;
      $("#cityError").text("Please enter a city");
    } else if (!user.city.match(name_regex)) {
      isValid = false;
      $("#cityError").text("The city should have only letters");
    }

    // Validation for the zip code
    if (user.zip === "") {
      isValid = false;
      $("#zipError").text("Please enter a ZIP code");
    } else if (!user.zip.match(zip_regex)) {
      isValid = false;
      $("#zipError").text("The ZIP code should contain only numbers");
    }

    // Validation for the username
    if (user.username === "") {
      isValid = false;
      $("#usernameError").text("Please enter a username");
    } else if (user.username.length < 3) {
      isValid = false;
      $("#usernameError").text(
        "The username should have at least 3 characters"
      );
    }

    // Validation for the email
    if (user.email === "") {
      isValid = false;
      $("#emailError").text("Please enter an email");
    } else if (!user.email.match(email_regex)) {
      isValid = false;
      $("#emailError").text("Please enter a valid email address");
    }

    // Validation for the password
    if (user.password != null) {
      if (user.password.length < 6) {
        isValid = false;
        $("#password1Error").text(
          "Password must be at least 6 characters long and must contain at least one lowercase letter, one number and one special character"
        );
      } else if (!password_regex.test(user.password)) {
        isValid = false;
        $("#password1Error").text(
          "Password must be at least 6 characters long and must contain at least one lowercase letter, one number and one special character"
        );
      }
    }

    // Check if the city, ZIP code, and state combination is valid
    //var validAddresses = [];
    var isValidAddress = false;

    for (var i = 0; i < validAddresses.length; i++) {
      var address = validAddresses[i];

      if (
        address.city.toLowerCase().trim() === user.city.toLowerCase() &&
        address.zip === user.zip &&
        address.state.toLowerCase().trim() === user.state.toLowerCase()
      ) {
        isValidAddress = true;
        break;
      }
    }

    // Invalid city, ZIP code, or state
    if (
      user.state !== "" &&
      user.city !== "" &&
      user.zip !== "" &&
      !isValidAddress
    ) {
      $("#stateError").text("Invalid city or ZIP code");
      isValid = false;
    } // Resolve the Promise with the validation result and isValidAddress
    resolve({ isValid: isValid, isValidAddress: isValidAddress });
    //return { isValid, isValidAddress };
  });
}

// Load and parse the CSV data
var validAddresses = [];

$.ajax({
  url: "../ort-plz-bundesland.csv",
  dataType: "text",
  success: function (csvData) {
    var parsedData = Papa.parse(csvData, { header: true });
    validAddresses = parsedData.data;
  },
  error: function () {
    console.error("Failed to load the file valid_addresses.csv");
  },
});

$(document).on("click", "#showSearchUser", function (event) {
  const email = $("#search-email").val();
  const username = $("#search-username").val();
  const isActive = $("input[name='status']:checked").val();
  const filters = {};

  if (email) {
    filters["email"] = email;
  }

  if (username) {
    filters["username"] = username;
  }

  if (isActive !== undefined) {
    filters["active"] = isActive;
  }

  const filterJSON = JSON.stringify(filters);
  console.log(filterJSON);

  // Close the edit window if it's open
  closeEditWindow();

  $.ajax({
    url: "http://localhost:8080/api/users/search",
    type: "POST",
    dataType: "json",
    contentType: "application/json",
    data: filterJSON,
    beforeSend: function (xhr) {
      var accessToken = sessionStorage.getItem("accessToken");
      if (!accessToken) {
        // User is not logged in, redirect to login page with a message
        window.location.href =
          "login.html?message=You are not logged in! Please first log in and then you can continue your action.";
      } else {
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      }
    },
    success: function (users) {
      addUsers(users);
    },
    error: function (error) {
      console.error(error);
    },
  });
});

// Hinzufügen der gesuchten Benutzer aus der Datenbank
function addUsers(users) {
  const selectedStatus = $("input[name='status']:checked").val();
  const statusMessage = selectedStatus === "true" ? "active" : "inactive";
  const allSearchedUsers = $("#searchResult");
  allSearchedUsers.empty();

  if (users.length === 0) {
    isActive = $("#search-status").prop("checked");
    clearToasts();
    showErrorToast(
      `No ${statusMessage} users found with the given email or username.`
    );
    // Display a message when no users are found
    const toast = new bootstrap.Toast(
      document.getElementById("toastContainer")
    );
    toast.show();
    return;
  }

  for (let user of users) {
    allSearchedUsers.append(createUser(user));
  }
}

// Funktion zum Erstellen der Benutzertabelle
function createUser(user) {
  const searchedUser = $(`<tr>
    <td scope="col">${user.username}</td>
    <td scope="col">${user.email}</td>
    <td scope="col">${user.firstname}</td>
    <td scope="col">${user.lastname}</td>
    <td scope="col"><button class="btn btn-outline-warning editUser" value="${user.id}">edit</button></td>
    <td scope="col">
      <form class="delete" data-title="delete" data-body="delete?">
        <button type="button" class="btn btn-outline-danger delete" value="${user.id}" data-bs-toggle="modal"
          data-bs-target="#deleteModal">delete</button>
      </form>
    </td>
  </tr>`);
  return searchedUser;
}

// Laden des Benutzers zum Bearbeiten in das Formular
$(document).on("click", ".editUser", function (event) {
  const id = event.target.value;

  $.ajax({
    url: "http://localhost:8080/api/users/" + id,
    type: "GET",
    dataType: "json",
    contentType: "application/json",
    beforeSend: function (xhr) {
      var accessToken = sessionStorage.getItem("accessToken");
      xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
    },
    success: function (user) {
      editUser(user);
    },
    error: function (error) {
      console.error(error);
    },
  });
  $(".footer").removeClass("fixed-bottom");

  const addEditUser = $("#addEditUser");
  addEditUser.empty();

  function editUser(user) {
    console.log("User active: " + user.isActive);
    console.log("User pw: " + user.password);

    const editUser = $(`
    <div class="container rounded my-5 border border-warning bg-light shadow-lg">
    <p class="fs-4 fw-bold pt-2">Edit User</p>
    <div class="row">
      <div class="col">
        <form method="PUT" action="">
          <div class="row mt-3 mb-3">
            <div class="col-md-4">
              <div class="form-group">
                <label for="title" class="fs-5">Title</label>
  
                <select name="Title" class="form-select fs-5 user-title" id="title-edit">
                  <option value="Mr" ${
                    user.title === "Mr" ? "selected" : ""
                  }>Mr</option>
                  <option value="Ms" ${
                    user.title === "Ms" ? "selected" : ""
                  }>Ms</option>
                </select>
              </div>
            </div>
            <input id="user-id-edit" type="hidden" class="form-control" name="" value="${
              user.id
            }" required />
            <div class="col-md-4">
              <div class="form-group">
                <label for="first-name" class="fs-5">First Name</label>
                <input id="first-name-edit" type="text" class="form-control" name="first-name" value="${
                  user.firstname
                }"
                  required />
                  <p class="input-error" id="firstNameError" style="color: red"></p>
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label for="last-name" class="fs-5">Last Name</label>
                <input id="last-name-edit" type="text" class="form-control" name="last-name" value="${
                  user.lastname
                }"
                  required />
                  <p class="input-error" id="lastNameError" style="color: red"></p>
              </div>
            </div>
          </div>
  
          <div class="row mb-3">
            <div class="col-md-4">
              <div class="form-group">
                <label for="address" class="fs-5">Address</label>
                <input id="address-edit" type="text" class="form-control" name="address" value="${
                  user.address
                }"
                  required />
                  <p class="input-error" id="addressError" style="color: red"></p>
              </div>
            </div>
            <div class="col-md-3">
            <div class="form-group">
            <label for="inputState" class="fs-5">State</label>
            <select id="state-select-edit" class="form-select">
             <option value="Burgenland" ${
               user.state === "Burgenland" ? "selected" : ""
             }>Burgenland</option>
              <option value="Carinthia"${
                user.state === "Carinthia" ? "selected" : ""
              }>Carinthia</option>
              <option value="Lower Austria"${
                user.state === "Lower Austria" ? "selected" : ""
              }>Lower Austria</option>
              <option value="Upper Austria"${
                user.state === "Upper Austria" ? "selected" : ""
              }>Upper Austria</option>
              <option value="Salzburg"${
                user.state === "Salzburg" ? "selected" : ""
              }>Salzburg</option>
              <option value="Styria"${
                user.state === "Styria" ? "selected" : ""
              }>Styria</option>
              <option value="Tyrol"${
                user.state === "Tyrol" ? "selected" : ""
              }>Tyrol</option>
              <option value="Vorarlberg"${
                user.state === "Vorarlberg" ? "selected" : ""
              }>Vorarlberg</option>
              <option value="Vienna"${
                user.state === "Vienna" ? "selected" : ""
              }>Vienna</option>
            </select>

            <p class="input-error" id="stateError" style="color: red"></p>
          </div>
          </div>
            <div class="col-md-2">
              <div class="form-group">
                <label for="zip" class="fs-5">Zip</label>
                <input id="zip-edit" type="text" class="form-control" name="zip" value="${
                  user.zip
                }" required />
                <p
              class="input-error"
              id="zipError"
              style="color: red; font-size: x-small"
            ></p>
              </div>
            </div>
  
            <div class="col-md-3">
              <div class="form-group">
                <label for="city" class="fs-5">City</label>
                <input id="city-edit" type="text" class="form-control" name="city" value="${
                  user.city
                }" required />
                <p
              class="input-error"
              id="cityError"
              style="color: red; font-size: small"
            ></p>
              </div>
            </div>
          </div>
  
          <div class="row mb-3">
            <div class="col-md-4">
              <div class="form-group">
                <label for="email" class="fs-5">E-mail</label>
                <input id="email-edit" type="email" class="form-control" name="email" value="${
                  user.email
                }" required />
                <p class="input-error" id="emailError" style="color: red"></p>
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label for="username" class="fs-5">Username</label>
                <input id="username-edit" type="text" class="form-control" name="username" value="${
                  user.username
                }"
                  required />
                  <p class="input-error" id="usernameError" style="color: red"></p>
              </div>
            </div>
  
            <div class="col-md-4">
              <div class="form-group">
                <label for="password" class="fs-5">Password</label>
                <input id="password-edit" type="text" class="form-control" name="password" value="${
                  user.password != null ? user.password : ""
                }"
                  />
                  <p class="input-error" id="password1Error" style="color: red"></p>
              </div>
            </div>
          </div>
  
          <div class="row">
          <div class="col-md-4">
              <div class="form-group">
                <label for="role" class="fs-5">Role</label>
                <input type="hidden" id="role-edit" name="role" value="${
                  user.role
                }" />
                <select class="form-select fs-5" id="role-select-edit">
                  <option value="USER" ${
                    user.role === "USER" ? "selected" : ""
                  }>User</option>
                  <option value="ADMIN" ${
                    user.role === "ADMIN" ? "selected" : ""
                  }>Admin</option>
                </select>
              </div>
              </div>
            <div class="col-md-4">
              <div class="form-check mb-2" style= "margin-top: 10%;">
                <div class="form-group">
                  <input type="checkbox" class="form-check-input" name="status" id="status" 
                  ${user.isActive ? "checked" : ""}
                  />
                  <label class="form-check-label fs-5" for="status">
                    active
                  </label>
                </div>
              </div>
            </div> 
              <div class="col-md-4"style= "margin-top: 2%;">
                <button type="button" class="btn btn-warning text-white float-end mt-2 mb-2 px-4 py-2"  id="saveEditUser">
                  save
                </button>
              </div>
            </div>
        </form>
      </div>
    </div>
  </div>`);

    addEditUser.append(editUser);
  }

  //WHEN EDIT USER OPENS, THE FOOTER IS NOT STICKY ANYMORE
  $(".footer").removeClass("fixed-bottom");
});

let isValid; // Declare isValid in a wider scope
let isValidAddress;
// Bearbeiteten Benutzer speichern
$(document).on("click", "#saveEditUser", function (event) {
  isActive = $("#status").is(":checked") ? true : false;

  // Passwort aus dem Eingabefeld abrufen
  const passwordInput = $("#password-edit").val();
  // Überprüfen, ob das Passwort einen Wert hat
  const password = passwordInput.trim() !== "" ? passwordInput : null;

  console.log("passwort: " + password);
  const user = {
    id: $("#user-id-edit").val(),
    title: $("#title-edit").val(),
    firstname: $("#first-name-edit").val(),
    lastname: $("#last-name-edit").val(),
    address: $("#address-edit").val(),
    city: $("#city-edit").val(),
    zip: $("#zip-edit").val(),
    state: $("#state-select-edit").val(),
    email: $("#email-edit").val(),
    username: $("#username-edit").val(),
    isActive: isActive,
    roles: $("#role-select-edit").val(),
  };

  // Das Passwort im user-Objekt nur setzen, wenn es nicht null ist
  if (password !== null) {
    user.password = password;
  }

  console.log("user passwort: " + user.password);

  // Call the validation function
  validateUser(user, validAddresses)
    .then(function (validationResult) {
      // Retrieve validation results
      var isValid = validationResult.isValid;
      var isValidAddress = validationResult.isValidAddress;

      if (
        isValid &&
        (!user.state || !user.city || !user.zip || isValidAddress)
      ) {
        // Proceed with editing if no validation errors

        $.ajax({
          url: "http://localhost:8080/api/users/update",
          type: "PUT",
          dataType: "json",
          contentType: "application/json",
          beforeSend: function (xhr) {
            var accessToken = sessionStorage.getItem("accessToken");
            xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
          },
          data: JSON.stringify(user),
          success: function (response) {
            clearToasts();
            showSuccessToast("Updated successfully!");
            // Display the toast message
            const toast = new bootstrap.Toast(
              document.getElementById("toastContainer")
            );
            toast.show();
            $("#addEditUser").empty(); // Clear the edit user form
            $(".footer").addClass("fixed-bottom");
          },

          error: console.error,
        });
      }
    })
    .catch(function (error) {
      console.error(error);
    });
});

// Benutzer löschen
$(document).on("click", ".delete", function (event) {
  const deleteId = event.target.value;

  // Show the delete confirmation modal
  $("#deleteUserModal").modal("show");

  // When the delete button in the modal is clicked, perform the deletion
  $("#confirmDelete").click(function () {
    $.ajax({
      url: "http://localhost:8080/api/users/delete/" + deleteId,
      type: "DELETE",
      dataType: "text",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      success: function (response) {
        console.log("Successfully deleted:", response);

        // Show a toast message
        showDeleteToast("User deleted successfully.");
        const toast = new bootstrap.Toast(
          document.getElementById("toastContainer")
        );
        toast.show();
        // Hide the modal after deletion
        $("#deleteUserModal").modal("hide");
        // Reload the page after a delay (e.g., 2 seconds)
        setTimeout(function () {
          location.reload();
        }, 2000);
      },
      error: function (xhr, textStatus, error) {
        console.error("Error deleting:", error);
      },
    });
  });
});
