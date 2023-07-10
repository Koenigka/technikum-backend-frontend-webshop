$(document).ready(function () {
  // Erfolgsmeldung aus dem Web Storage abrufen
  var registrationMessage = sessionStorage.getItem("registrationMessage");

  // Wenn eine Erfolgsmeldung vorhanden ist, anzeigen und aus dem Web Storage entfernen
  if (registrationMessage) {
    // Erfolgsmeldung anzeigen
    $("#registrationSuccessMessage").text(registrationMessage);
    $("#registrationSuccessMessage").show();

    // Erfolgsmeldung nach einigen Sekunden ausblenden
    setTimeout(function () {
      $("#registrationSuccessMessage").hide();
    }, 15000);
  }
  // Erfolgsmeldung aus dem Web Storage entfernen
  sessionStorage.removeItem("registrationMessage");
  // LOGIN
  $("#loginUserButton").on("click", function () {
    var email = $("#email").val();
    var password = $("#password").val();

    var loginData = {
      email: email,
      password: password,
    };

    $(".emErr").text("");
    $(".passErr").text("");

    // Get input values
    var email = $("#email").val();
    var password = $("#password").val();

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
      var loginData = {
        email: email,
        password: password,
      };

      $.ajax({
        url: "http://localhost:8080/api/auth/login",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(loginData),
        success: function (response) {
          // Erfolgsmeldung anzeigen und weitere Aktionen durchführen
          console.log("Login erfolgreich");
          console.log(response.accessToken);

          // JWT-Token im sessionStorage speichern
          sessionStorage.setItem("accessToken", response.accessToken);

          // Token decodieren und Benutzerrolle extrahieren
          var token = response.accessToken;
          var decodedToken = jwt_decode(token);

          console.log(decodedToken);

          var userName = decodedToken.e;
          sessionStorage.setItem("userName", userName);

          var userRole = decodedToken.a; // Das gesamte Array der Benutzerrollen

          // Benutzerrolle im Session Storage speichern
          sessionStorage.setItem("userRole", JSON.stringify(userRole));
          console.log("UserRoleLogIn:", userRole);

          //Zugriff auf den Token über: var accessToken = sessionStorage.getItem("accessToken");
          sessionStorage.setItem("loginMessage", "Login successfull!");
          // Weiterleitung nach dem erfolgreichen Login
          window.location.href = "/pages/shop.html";
        },
        error: function (xhr, status, error) {
          // Fehlermeldung anzeigen
          //console.error("Fehler beim Login: " + error);
          $("#loginMessage").text("Something went wrong. Try again.");
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
