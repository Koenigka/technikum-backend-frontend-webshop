//LOAD USERS FROM DATABASE
$.ajax({
  url: "http://localhost:8080/users/email",
  type: "GET",
  cors: true,
  success: function (users) {
    addUsers(users);
  },
  error: function (error) {
    console.error(error);
  },
});

//SEARCH FUNCTION (dzt nur nach title möglich)
$(document).on("click", "#showSearchUser", function (event) {
  const search = $("#email").val();

  $.ajax({
    url: "http://localhost:8080/users/email/" + search,
    type: "GET",
    cors: true,
    success: function (users) {
      addUsers(users);
    },
    error: function (error) {
      console.error(error);
    },
  });
});

//ADD SEARCHED USER FROM DATABASE
function addUsers(users) {
  const allSearchedUsers = $("#searchResult");
  allSearchedUsers.empty();

  for (let user of users) {
    allSearchedUsers.append(createUser(user));
  }
}

function createUser(user) {
  const searchedUser = $(`<tr>
    <td scope="col">${user.username}</td>
    <td scope="col">${user.email}</td>
    <td scope="col">${user.firstname}</td>
    <td scope="col">${user.lastname}</td>
    <td scope="col"><button class="btn btn-outline-warning editUser" value="${user.id}">edit</button></td>
    <td scope="col">
      <form class="delete" data-title="delete" data-body="delete?">
        <button type="button" class="btn btn-outline-danger" data-bs-toggle="modal"
          data-bs-target="#deleteModal">delete</button>
      </form>
    </td>
  </tr>`);
  return searchedUser;
}

//LOAD PRODUCT TO EDIT FORM
$(document).on("click", ".editUser", function (event) {
  const id = event.target.value;
  //console.log(id);

  $.ajax({
    url: "http://localhost:8080/users/" + id,
    type: "GET",
    cors: true,
    success: function (user) {
      editUsers(user);
    },
    error: function (error) {
      console.error(error);
    },
  });

  const addEditUser = $("#addEditUser");
  addEditUser.empty();

  function editUsers(user) {
    const editUser = $(`
      <div
      class="container rounded mt-5 border border-warning bg-light shadow-lg"
    >
      <p class="fs-4 fw-bold pt-2">Edit User</p>
      <div class="row">
        <div class="col">
          <form method="PUT" action="">
            <div class="row mt-3 mb-3">
              <div class="col-md-4">
                <div class="form-group">
                  <label for="title" class="fs-5">Title</label>

                  <select name="Title" class="form-select fs-5 user-title" id="title-edit" >
                    <option value="">${user.title}</option>
                  </select>
                </div>
              </div>
              <input
                    id="user-id-edit"
                    type="hidden"
                    class="form-control"
                    name=""
                    value="${user.id}"
                    required
                  />
              <div class="col-md-4">
                <div class="form-group">
                  <label for="first-name" class="fs-5">First Name</label>
                  <input
                    id="first-name-edit"
                    type="text"
                    class="form-control"
                    name="first-name"
                    value="${user.firstname}"
                    required
                  />
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label for="last-name" class="fs-5">Last Name</label>
                  <input
                    id="last-name-edit"
                    type="text"
                    class="form-control"
                    name="last-name"
                    value="${user.lastname}"
                    required
                  />
                </div>
              </div>
            </div>

            <div class="row mb-3">
              <div class="col-md-4">
                <div class="form-group">
                  <label for="address" class="fs-5">Address</label>
                  <input
                    id="address-edit"
                    type="text"
                    class="form-control"
                    name="address"
                    value="${user.address}"
                    required
                  />
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label for="zip" class="fs-5">Zip</label>
                  <input
                    id="zip-edit"
                    type="text"
                    class="form-control"
                    name="zip"
                    value="${user.zip}"
                    required
                  />
                </div>
              </div>

              <div class="col-md-4">
                <div class="form-group">
                  <label for="city" class="fs-5">City</label>
                  <input
                    id="city-edit"
                    type="text"
                    class="form-control"
                    name="city"
                    value="${user.city}"
                    required
                  />
                </div>
              </div>
            </div>

            <div class="row mb-3">
              <div class="col-md-4">
                <div class="form-group">
                  <label for="email" class="fs-5">E-mail</label>
                  <input
                    id="email-edit"
                    type="email"
                    class="form-control"
                    name="email"
                    value="${user.email}"
                    required
                  />
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label for="username" class="fs-5">Username</label>
                  <input
                    id="username-edit"
                    type="text"
                    class="form-control"
                    name="username"
                    value="${user.username}"
                    required
                  />
                </div>
              </div>

              <div class="col-md-4">
                <div class="form-group">
                  <label for="password" class="fs-5">Password</label>
                  <input
                    id="password-edit"
                    type="text"
                    class="form-control"
                    name="password"
                    value="${user.password}"
                    required
                  />
                </div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-4">
                <div class="form-check mb-2">
                  <input
                    type="checkbox"
                    class="form-check-input"
                    name="status"
                    id="status"
                  />
                  <label class="form-check-label fs-5" for="status">
                    active
                  </label>
                </div>
              </div>

              <div class="col-md-4">
                <div class="form-check mb-2">
                  <input
                    type="checkbox"
                    class="form-check-input"
                    name="is-admin"
                    id="is-admin"
                  />
                  <label class="form-check-label fs-5" for="is-admin">
                    User is Admin
                  </label>
                </div>
              </div>
            </div>

            <button
              type="button"
              class="btn btn-warning text-white float-end mt-2 mb-2"
              id="saveEditUser"
            >
              save
            </button>
          </form>
        </div>
      </div>
    </div>`);

    addEditUser.append(editUser);
  }
});

//EDIT USER

$(document).on("click", "#saveEditUser", function (event) {
  const id = $("#user-id-edit").val();

  const user = {
    title: $("#title-edit").val(),
    firstname: $("#first-name-edit").val(),
    lastname: $("#last-name-edit").val(),
    address: $("#address-edit").val(),
    city: $("#city-edit").val(),
    zip: $("#zip-edit").val(),
    username: $("#username-edit").val(),
    email: $("#email-edit").val(),
    password: $("#password-edit").val(),
    isActive: "true",
    isAdmin: "false",
  };

  console.log(user);
  $.ajax({
    url: "http://localhost:8080/users/" + id,
    type: "PUT",
    cors: true,
    contentType: "application/json",
    data: JSON.stringify(user),
    success: console.log,
    error: console.error,
  });
});
