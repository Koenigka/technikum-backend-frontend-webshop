$(document).ready(function () {
  // Function to clear login fields
  function clearLoginFields() {
    $("#email").val("");
    $("#password").val("");
  }

  // Get the message from the query parameter
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const message = urlParams.get("message");
  // Check if a message is present and display it
  if (message) {
    const messageElement = document.getElementById("message");
    if (messageElement) {
      messageElement.textContent = message;
      $("#message").text(message);
      $("#message").show();
      // Erfolgsmeldung nach einigen Sekunden ausblenden
      setTimeout(function () {
        $("#message").hide();
      }, 7000);
    }
  }

  // Erfolgsmeldung aus dem Web Storage abrufen
  var registrationMessage = sessionStorage.getItem("registrationMessage");

  // Check if login data is saved in Local Storage
  var savedEmail = localStorage.getItem("loginEmail");
  var rememberEmail = localStorage.getItem("rememberEmail") === "true";

  // Auto-complete password if "Remember Email" was checked before
  if (rememberEmail && savedEmail) {
    $("#email").val(savedEmail);
    $("#remember").prop("checked", true);
  }

  // Event handler for "Remember Password" checkbox
  $("#remember").on("change", function () {
    rememberEmail = this.checked;
    localStorage.setItem("rememberEmail", rememberEmail);
  });

  // Wenn eine Erfolgsmeldung vorhanden ist, anzeigen und aus dem Web Storage entfernen
  if (registrationMessage) {
    // Erfolgsmeldung anzeigen
    $("#registrationSuccessMessage").text(registrationMessage);
    $("#registrationSuccessMessage").show();

    // Erfolgsmeldung nach einigen Sekunden ausblenden
    setTimeout(function () {
      $("#registrationSuccessMessage").hide();
    }, 7000);
  }
  // Erfolgsmeldung aus dem Web Storage entfernen
  sessionStorage.removeItem("registrationMessage");

  // LOGIN
  $("#loginUserButton").on("click", function () {
    var email = $("#email").val();
    var password = $("#password").val();
    var rememberEmail = $("#remember").is(":checked");

    var loginData = {
      email: email,
      password: password,
    };

    $(".emErr").text("");
    $(".passErr").text("");

    // Validate email
    if (!email) {
      // Show email error message
      $(".emErr").text("Please enter your email");
    } else if (!isValidEmail(email)) {
      // Show email error message for invalid email format
      $(".emErr").text("Please enter a valid email address");
    }

    // Validate password
    if (!password) {
      // Show password error message
      $(".passErr").text("Please enter your password");
    } else if (password.length < 6) {
      // Show password error message for password length less than 6
      $(".passErr").text("Password must be at least 6 characters long");
    }

    // Proceed with login if no validation errors
    if (email && isValidEmail(email) && password && password.length >= 6) {
      $.ajax({
        url: "http://localhost:8080/api/auth/login",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(loginData),
        success: function (response) {
          // JWT-Token im sessionStorage speichern
          sessionStorage.setItem("accessToken", response.accessToken);

          // Token decodieren und Benutzerrolle extrahieren
          var token = response.accessToken;
          var decodedToken = jwt_decode(token);

          var userName = decodedToken.e;
          sessionStorage.setItem("userName", userName);

          // Extract and store additional user information
          var firstName = decodedToken.firstName; // Replace with the actual field name in your token
          var lastName = decodedToken.lastName; // Replace with the actual field name in your token
          var address = decodedToken.address; // Replace with the actual field name in your token

          sessionStorage.setItem("firstName", firstName);
          sessionStorage.setItem("lastName", lastName);
          sessionStorage.setItem("address", address);
          var userRole = decodedToken.a; // Das gesamte Array der Benutzerrollen

          // Benutzerrolle im Session Storage speichern
          sessionStorage.setItem("userRole", JSON.stringify(userRole));
          console.log("UserRoleLogIn:", userRole);

          //Zugriff auf den Token über: var accessToken = sessionStorage.getItem("accessToken");
          sessionStorage.setItem("loginMessage", "Login successfull!");
          if (rememberEmail) {
            // Save login data in Local Storage if "Remember Password" is checked
            localStorage.setItem("loginEmail", email);
          } else {
            // Clear login data from Local Storage if "Remember Password" is not checked
            localStorage.removeItem("loginEmail");
            localStorage.removeItem("loginPassword");
          }

          // Weiterleitung nach dem erfolgreichen Login
          window.location.href = "/pages/shop.html";
        },
        error: function (xhr, status, error) {
          // Fehlermeldung anzeigen
          //console.error("Fehler beim Login: " + error);
          $("#loginMessage").text(
            "The combination email address and password does not exist. Please, try again."
          ); // For this example, I'm just clearing the login fields after clicking "Login"
          clearLoginFields();
        },
      });
    }
  });

  // Helper function to validate email format
  function isValidEmail(email) {
    // Use a regular expression to validate the email format
    var emailRegex = /\S+@\S+\.\S+/;
    return emailRegex.test(email);
  }
});
