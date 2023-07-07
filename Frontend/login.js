$("#loginUserButton").on("click", (_e) => {
  var email = $("#email").val();
  var pass = $("#password").val();

  if (email == "") {
    $(".emErr").html(
      "Enter email" + " " + '<i class="fa fa-times" aria-hidden="true"></i>'
    );
  } else if (email.length < 6) {
    $(".emErr").html(
      "Email is too short" +
        " " +
        '<i class="fa fa-times" aria-hidden="true"></i>'
    );
  } else if (IsEmail(email) == false) {
    $(".emErr").html(
      "Email is not valid" +
        " " +
        '<i class="fa fa-times" aria-hidden="true"></i>'
    );
  } else if (email) {
    $(".emErr").html("");
  }

  if (pass == "") {
    $(".passErr").html(
      "Enter password" + " " + '<i class="fa fa-times" aria-hidden="true"></i>'
    );
  } else if (pass.length < 8) {
    $(".passErr").html(
      "Password is too short" +
        " " +
        '<i class="fa fa-times" aria-hidden="true"></i>'
    );
  } else if (pass) {
    $(".passErr").html("");
  }
});

function IsEmail(email) {
  var regex =
    /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  if (!regex.test(email)) {
    return false;
  } else {
    return true;
  }
}
authenticate(email, pass);

function authenticate(data) {
  $.post({
    url: "http://localhost:8080/login",
    contentType: "application/json",
    data: JSON.stringify({
      email: $("#email").val(),
      password: $("#password").val(),
    }),
    success: (data) => sessionStorage.setItem("token", data),
    error: console.error,
  });
}
/*$("#loginUserButton").on("click", (_e) => {
  $.post({
    url: "http://localhost:8080/login",
    contentType: "application/json",
    data: JSON.stringify({
      email: $("#email").val(),
      password: $("#password").val(),
    }),
    success: (data) => sessionStorage.setItem("token", data),
    error: console.error,
  });
});*/
