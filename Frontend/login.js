$("#loginUserButton").on("click", (_e) => {
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
});
