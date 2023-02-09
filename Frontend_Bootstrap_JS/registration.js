$(document).ready(function () {
  //USER REGISTRATION
  $("#registerUserButton").on("click", (_e) => {
    const user = {
      title: $("#title").val(),
      firstname: $("#first-name").val(),
      lastname: $("#last-name").val(),
      address: $("#inputAddress").val(),
      city: $("#inputCity").val(),
      zip: $("#inputZip").val(),
      username: $("#un").val(),
      email: $("#email").val(),
      password: $("#password1").val(),
      active: "true",
      admin: "false",
    };
    $.ajax({
      url: "http://localhost:8080/users",
      type: "POST",
      cors: true,
      contentType: "application/json",
      data: JSON.stringify(user),
      success: console.log,
      error: console.error,
    });
  });
});
