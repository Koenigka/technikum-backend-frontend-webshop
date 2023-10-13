$(document).ready(function () {
  // Check if a JWT token is present in sessionStorage
  if (sessionStorage.getItem("accessToken")) {
    // User is logged in
    const userId = sessionStorage.getItem("userId");
    // Display the username from the session
    // $("#userName").text(userId);

    // Make an AJAX request to fetch user data
    $.ajax({
      url: `http://localhost:8080/api/users/${userId}`, // Update the URL to match your API endpoint
      type: "GET",
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`, // Include the JWT token
      },
      success: function (userData) {
        // Call the function to display user details
        displayUserDetails(userData);
        const debitCardRadio = document.getElementById("debitCardRadio");
        const creditCardRadio = document.getElementById("creditCardRadio");
        const paypalRadio = document.getElementById("paypalRadio");
        const debitCardDetails = document.getElementById("debitCardDetails");
        const creditCardDetails = document.getElementById("creditCardDetails");
        const paypalDetails = document.getElementById("paypalDetails");

        debitCardRadio.addEventListener("click", () => {
          debitCardDetails.style.display = "block";
          creditCardDetails.style.display = "none";
          paypalDetails.style.display = "none";
        });

        creditCardRadio.addEventListener("click", () => {
          debitCardDetails.style.display = "none";
          creditCardDetails.style.display = "block";
          paypalDetails.style.display = "none";
        });
        paypalRadio.addEventListener("click", () => {
          debitCardDetails.style.display = "none";
          creditCardDetails.style.display = "none";
          paypalDetails.style.display = "block";
        });
        saveDebitCard.addEventListener("click", () => {
          event.preventDefault();
          debitCardDetails.style.display = "none";
        });

        saveCreditCard.addEventListener("click", () => {
          event.preventDefault();
          creditCardDetails.style.display = "none";
        });

        savePayPal.addEventListener("click", () => {
          event.preventDefault();
          paypalDetails.style.display = "none";
        });
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
      <div class="container mt-5">
  <div class="row">
    <div class="col-md-6">
      <div class="container rounded border border-warning bg-light shadow-lg">
        <div class="row">
          <p class="fs-4 fw-bold pt-2">My Personal Details</p>
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
    </div>
    <div class="col-md-6">
      <div class="container rounded border border-warning bg-light shadow-lg">
        <div class="col">
          <p class="fs-4 fw-bold pt-2">Payment Methods</p>
           <form>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="paymentMethod" id="debitCardRadio">
              <label class="form-check-label" for="debitCardRadio">Debit Card</label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="paymentMethod" id="creditCardRadio">
              <label class="form-check-label" for="creditCardRadio">Credit Card</label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="paymentMethod" id="paypalRadio">
              <label class="form-check-label" for="paypalRadio">PayPal</label>
            </div>


            <!-- Add more payment method radio buttons here -->

            <!-- Debit Card Details -->
            <div class="accordion-content" id="debitCardDetails" style="display: none;">
              <div class="form-group mb-2">
                <label for="debitCardNumber" class="fs-5">Card Number</label>
                <input id="debitCardNumber" type="text" class="form-control" name="debitCardNumber">
                <div class="input-group">
              <img src="../img/mastercard.jpg" alt="mastercard" class="img-thumbnail" style="max-width: 10%;">
              <img src="../img/visa.jpg" alt="mastercard" class="img-thumbnail" style="max-width: 10%;">
              </div>
              </div>
             <div class="form-group mb-2">
                    <label for="debitCardExpYear" class="fs-5">Expiration Year</label>
                    <input id="debitCardExpYear" type="text" class="form-control" name="debitCardExpYear">
                  </div>
                   <button class="btn btn-warning mb-2 text-white" id="saveDebitCard">Save</button>
            </div>

            <!-- Credit Card Details -->
            <div class="accordion-content" id="creditCardDetails" style="display: none;">
              <div class="form-group mb-2">
                <label for="creditCardNumber" class="fs-5">Card Number</label>
                <input id="creditCardNumber" type="text" class="form-control" name="creditCardNumber">
                <div class="input-group">
              <img src="../img/mastercard.jpg" alt="mastercard" class="img-thumbnail" style="max-width: 10%;">
              <img src="../img/visa.jpg" alt="mastercard" class="img-thumbnail" style="max-width: 10%;">
              </div>
              </div>
              <div class="form-group mb-2">
                    <label for="creditCardExpYear" class="fs-5">Expiration Year</label>
                    <input id="creditCardExpYear" type="text" class="form-control" name="creditCardExpYear">
                  </div>
                  <div class="form-group mb-2">
                    <label for="creditCardSecurityCode" class="fs-5">Security Code</label>
                    <input id="creditCardSecurityCode" type="text" class="form-control" name="creditCardSecurityCode">
                  </div>
                   <button class="btn btn-warning mb-2 text-white" id="saveCreditCard">Save</button>
            </div>
            
            <!-- PayPal Details -->
            <div class="accordion-content" id="paypalDetails" style="display: none;">
              <div class="form-group mb-2">
                <label for="paypalUsername" style=" display: block;" class="fs-5">PayPal Username</label>
                <input id="paypalUsername" type="text" style="width:75%; display: inline;" class="form-control" name="paypalUsername">
                <img src="../img/paypal.jpg" alt="paypal" class="img-thumbnail" style="max-width: 10%;">
                 <button class="btn btn-warning mb-2 text-white" style = "display:block;" id="savePayPal">Save</button>
              </div>
              <!-- Add more PayPal details fields here -->
            </div>
          </form>
          </div>
        </div>
        </div>
      </div>
    `);

    // Append the user details HTML to the container
    $("#userDetailsContainer").html(userDetails);
  }
});
