/* // Laden der Benutzer aus der Datenbank
$.ajax({
  url: "http://localhost:8080/api/users/email",
  type: "GET",
  dataType: "json",
  contentType: "application/json",
  beforeSend: function (xhr) {
    var accessToken = sessionStorage.getItem("accessToken");
    xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
  },
  success: function (users) {
    addUsers(users);
  },
  error: function (error) {
    console.error(error);
  },
});
 */
// Suchfunktion (derzeit nur nach E-Mail möglich)
$(document).on("click", "#showSearchUser", function (event) {
  const search = $("#email").val();

  $.ajax({
    url: "http://localhost:8080/api/users/findByEmail/" + search,
    type: "GET",
    dataType: "json",
    contentType: "application/json",
    beforeSend: function (xhr) {
      var accessToken = sessionStorage.getItem("accessToken");
      xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
    },
    success: function (users) {
      addUsers(users);
    },
    error: function (error) {
      console.error(error);
    },
  });
  $(".footer").removeClass("fixed-bottom");
});

// Hinzufügen der gesuchten Benutzer aus der Datenbank
function addUsers(users) {
  const allSearchedUsers = $("#searchResult");
  allSearchedUsers.empty();

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

  const addEditUser = $("#addEditUser");
  addEditUser.empty();


  // Check if the user is an admin and set the role accordingly
  
  function editUser(user) {
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
                  <option value="Mr" ${user.title==="Mr" ? "selected" : "" }>Mr</option>
                  <option value="Ms" ${user.title==="Ms" ? "selected" : "" }>Ms</option>
                </select>
              </div>
            </div>
            <input id="user-id-edit" type="hidden" class="form-control" name="" value="${user.id}" required />
            <div class="col-md-4">
              <div class="form-group">
                <label for="first-name" class="fs-5">First Name</label>
                <input id="first-name-edit" type="text" class="form-control" name="first-name" value="${user.firstname}"
                  required />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label for="last-name" class="fs-5">Last Name</label>
                <input id="last-name-edit" type="text" class="form-control" name="last-name" value="${user.lastname}"
                  required />
              </div>
            </div>
          </div>
  
          <div class="row mb-3">
            <div class="col-md-4">
              <div class="form-group">
                <label for="address" class="fs-5">Address</label>
                <input id="address-edit" type="text" class="form-control" name="address" value="${user.address}"
                  required />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label for="zip" class="fs-5">Zip</label>
                <input id="zip-edit" type="text" class="form-control" name="zip" value="${user.zip}" required />
              </div>
            </div>
  
            <div class="col-md-4">
              <div class="form-group">
                <label for="city" class="fs-5">City</label>
                <input id="city-edit" type="text" class="form-control" name="city" value="${user.city}" required />
              </div>
            </div>
          </div>
  
          <div class="row mb-3">
            <div class="col-md-4">
              <div class="form-group">
                <label for="email" class="fs-5">E-mail</label>
                <input id="email-edit" type="email" class="form-control" name="email" value="${user.email}" required />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label for="username" class="fs-5">Username</label>
                <input id="username-edit" type="text" class="form-control" name="username" value="${user.username}"
                  required />
              </div>
            </div>
  
            <div class="col-md-4">
              <div class="form-group">
                <label for="password" class="fs-5">Password</label>
                <input id="password-edit" type="text" class="form-control" name="password" value="${user.password}"
                  required />
              </div>
            </div>
          </div>
  
          <div class="row">
          <div class="col-md-4">
              <div class="form-group">
                <label for="role" class="fs-5">Role</label>
                <input type="hidden" id="role-edit" name="role" value="${user.role}" />
                <select class="form-select fs-5" id="role-select-edit">
                  <option value="USER" ${user.role==="USER" ? "selected" : "" }>User</option>
                  <option value="ADMIN" ${user.role==="ADMIN" ? "selected" : "" }>Admin</option>
                </select>
              </div>
              </div>
            <div class="col-md-4">
              <div class="form-check mb-2">
                <div class="form-group">
                  <input type="checkbox" class="form-check-input" name="status" id="status" checked />
                  <label class="form-check-label fs-5" for="status">
                    active
                  </label>
                </div>
              </div>
            </div> 
              <div class="col-md-4">
                <button type="button" class="btn btn-warning text-white float-end mt-2 mb-2" id="saveEditUser">
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

// Bearbeiteten Benutzer speichern
$(document).on("click", "#saveEditUser", function (event) {
  const user = {
    id: $("#user-id-edit").val(),
    title: $("#title-edit").val(),
    firstname: $("#first-name-edit").val(),
    lastname: $("#last-name-edit").val(),
    address: $("#address-edit").val(),
    city: $("#city-edit").val(),
    zip: $("#zip-edit").val(),
    email: $("#email-edit").val(),
    username: $("#username-edit").val(),
    password: $("#password-edit").val(),
    active: $("#status").is(":checked").toString(),
    role: $("#role-select-edit").val(),
  };

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
    success: console.log,
    error: console.error,
  });
});

// Benutzer löschen
$(document).on("click", ".delete", function (event) {
  const deleteId = event.target.value;

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
      location.reload();
    },
    error: function (xhr, textStatus, error) {
      console.error("Error deleting:", error);
    },
  });
});
