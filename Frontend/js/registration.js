$(document).ready(function () {
  //USER REGISTRATION
  $("#registerUserButton").on("click", (_e) => {
    const user = {
      title: $("#title").val(),
      firstname: $("#first-name").val(),
      lastname: $("#last-name").val(),
      address: $("#inputAddress").val(),
      state: $("#inputState").val(),
      city: $("#inputCity").val(),
      zip: $("#inputZip").val(),
      username: $("#un").val(),
      email: $("#email").val(),
      password: $("#password1").val(),
      password2: $("#password2").val(),
      active: "true",
      admin: "false",
    };
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
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[a-zA-Z\d!@#$%^&*()]+$/;

    // Validation for all the registration inputs

    // Validation for the title
    if (user.title === "") {
      isValid = false;
      $("#titleError").text("Please select a title");
    }

    // Validation for the firstname
    if (user.firstname === "") {
      isValid = false;
      $("#firstNameError").text("Please enter your first name");
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
      $("#lastNameError").text("Please enter your last name");
    } else if (user.lastname.length < 2) {
      isValid = false;
      $("#lastNameError").text("The last name should have at least 2 letters");
    } else if (!user.lastname.match(name_regex)) {
      isValid = false;
      $("#lastNameError").text("The first name should contain only letters");
    }

    // Validation for the address
    if (user.address === "") {
      isValid = false;
      $("#addressError").text("Please enter your address");
    } else if (!/^.*(?=.*\d)(?=.*[a-zA-Z]).{4,}$/.test(user.address)) {
      isValid = false;
      $("#addressError").text(
        "Address should contain at least 4 letters and numbers"
      );
    }

    // Validation for the state
    if (user.state === "") {
      isValid = false;
      $("#stateError").text("Please enter your state");
    }

    // Validation for the city
    if (user.city === "") {
      isValid = false;
      $("#cityError").text("Please enter your city");
    } else if (!user.city.match(name_regex)) {
      isValid = false;
      $("#cityError").text("The city should have only letters");
    }

    // Validation for the zip code
    if (user.zip === "") {
      isValid = false;
      $("#zipError").text("Please enter your ZIP code");
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
      $("#emailError").text("Please enter your email");
    } else if (!user.email.match(email_regex)) {
      isValid = false;
      $("#emailError").text("Please enter a valid email address");
    }

    // Validation for the password
    if (user.password === "") {
      isValid = false;
      $("#password1Error").text("Please enter a password");
    } else if (user.password.length < 6) {
      isValid = false;
      $("#password1Error").text("Password must be at least 6 characters long");
    } else if (!password_regex.test(user.password)) {
      isValid = false;
      $("#password1Error").text(
        "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
      );
    }

    // Validation for the repetition of the password
    if (user.password2 === "") {
      isValid = false;
      $("#password2Error").text("Please repeat the password");
    } else if (user.password !== user.password2) {
      isValid = false;
      $("#password2Error").text("Passwords do not match");
    }

    // Load and parse the CSV data
    var validAddresses = [];

    $.ajax({
      url: "../ort-plz-bundesland.csv",
      dataType: "text",
    })
      .done(function (csvData) {
        var parsedData = Papa.parse(csvData, { header: true });
        validAddresses = parsedData.data;

        // Check if the city, ZIP code, and state combination is valid
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
        }

        // Proceed with registration if no validation errors
        if (
          isValid &&
          (!user.state || !user.city || !user.zip || isValidAddress)
        ) {
          // Proceed with registration if no validation errors

          $.ajax({
            url: "http://localhost:8080/api/users/register",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(user),
            success: function (response) {
              // Erfolgsmeldung im Web Storage speichern
              sessionStorage.setItem(
                "registrationMessage",
                "Registrierung erfolgreich. Bitte loggen Sie sich ein."
              );
              // Weiterleitung zur Login-Seite
              window.location.href = "login.html";
            },
            error: function (xhr, status, error) {
              // Fehlermeldung anzeigen
              $("#registrationMessage").text(
                "Fehler bei der Registrierung: " + error
              );
            },
          });
        }
      })
      .fail(function () {
        console.error("Failed to load valid_addresses.csv");
      });
  });
});
