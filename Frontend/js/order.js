$(document).ready(function () {
  // Check if a JWT token is present in sessionStorage
  if (sessionStorage.getItem("accessToken")) {
    // User is logged in
    const userEmail = sessionStorage.getItem("userName");
    // Display the username from the session
    $("#userName").text(userEmail);

    // Make an AJAX request to fetch user data
    $.ajax({
      url: `http://localhost:8080/api/users/email/${userEmail}`, // Update the URL to match your API endpoint
      type: "GET",
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`, // Include the JWT token
      },
      success: function (userData) {
        // Call the function to display user details
        displayUserDetails(userData);
      },
      error: function (xhr, status, error) {
        // Handle errors, such as user not found
        console.error("Error fetching user data: " + error);
        // Optionally, display an error message to the user
      },
    });
  } else {
    // User is not logged in
    // Handle the case where user data is not available
    console.log("User data not found.");
  }
  function displayUserDetails(user) {
    // Create an HTML structure to display user details
    const userDetails = $(`
      <div class="container rounded mt-5 border border-warning bg-light shadow-lg">
        <p class="fs-4 fw-bold pt-2">My personal Details</p>
        <div class="row">
          <div class="col">
            <form>
              <div class="row mt-3 mb-3">
                <div class="col-md-5">
                  <div class="form-group">
                    <label for="user-firstname" class="fs-5">First Name</label>
                    <input id="userFirstName" type="text" class="form-control" name="user-firstname" value="${user.firstname}" disabled>
                  </div>
                </div>
                <div class="col-md-5">
                  <div class="form-group">
                    <label for="user-lastname" class="fs-5">Last Name</label>
                    <input id="user-lastname" type="text" class="form-control" name="user-lastname" value="${user.lastname}" disabled>
                  </div>
                </div>
              </div>
              <div class="row mt-3 mb-3">
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="user-address" class="fs-5">My Address</label>
                    <input id="user-address" type="text" class="form-control" name="user-address" value="${user.address}" required disabled>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="user-city" class="fs-5">My City</label>
                    <input id="user-city" type="text" class="form-control" name="user-city" value="${user.city}" required disabled>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="user-zip" class="fs-5">My Zip</label>
                    <input id="user-zip" type="text" class="form-control" name="user-zip" value="${user.zip}" required disabled>
                  </div>
                </div>
                </div>
              <!-- Add more user details fields here -->
            </form>
          </div>
        </div>
      </div>
    `);

    // Append the user details HTML to the container
    $("#userDetailsContainer").html(userDetails);
  }
});
