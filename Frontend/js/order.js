$(document).ready(function () {
  // Check if a JWT token is present in sessionStorage
  if (sessionStorage.getItem("accessToken")) {
    // User is logged in
    const userId = sessionStorage.getItem("userId");

    // Make an AJAX request to fetch user data
    $.ajax({
      url: `http://localhost:8080/api/users/myProfile`, 
      type: "GET",
      dataType: "json",
    contentType: "application/json",
    beforeSend: function (xhr) {
      var accessToken = sessionStorage.getItem("accessToken");
      xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
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
        // Fetch and display cart items
        fetchAndDisplayCartItems(userId);
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

  function fetchAndDisplayCartItems(userId) {
    var totalPrice = 0;
    const productQuantities = {}; // To keep track of product quantities
    // Make an AJAX request to fetch cart items
    $.ajax({
      url: "http://localhost:8080/api/cart/myCart",
      type: "GET",
      dataType: "json",
    contentType: "application/json",
    beforeSend: function (xhr) {
      var accessToken = sessionStorage.getItem("accessToken");
      xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
    },
      success: function (cartItems) {
        // Check if cartItems is not empty
        if (cartItems.length > 0) {
          // Display cart items in the cartItemsContainer
          const cartItemsContainer = $("#orderItemsContainer");
          var cartContentHtml = "";

          cartItems.forEach(function (cartItem) {
            var itemPrice = cartItem.price * cartItem.quantity;
            totalPrice += itemPrice;

            // Check if the product is already in the productQuantities object
            if (productQuantities[cartItem.title]) {
              productQuantities[cartItem.title] += cartItem.quantity;
            } else {
              productQuantities[cartItem.title] = cartItem.quantity;
            }

            // Create the order object using cartItem data
            const order = {
              userId: userId,
              cartItemId: cartItem.id,
              price: cartItem.price,
              quantity: cartItem.quantity,
            };
            console.log(order);

            // Generate HTML for each product
            cartContentHtml += `
            <div class="row border border-warning rounded m-5 shadow-sm mt-2 cart-item" data-id="${
              cartItem.id
            }" data-price="${cartItem.price}">
              <div class="col p-2">
                <img src="http://localhost:8080/api/files/${
                  cartItem.img
                }" alt="${cartItem.title}" class="cart-item-image" />
              </div>
              <div class="col p-4 fs-5 cart-item-info">
                <p>
                  <span class="cart-quantity">${cartItem.quantity} x</span> ${
              cartItem.title
            }
                </p>
                <p class="price">€ ${(
                  cartItem.price * cartItem.quantity
                ).toFixed(2)}</p>
              </div>
            </div>
          `;
          });

          // Append the subtotal at the bottom
          cartContentHtml += `
          <div class="row mx-5">
            <div class="col p-2">
              <p class="fs-3">Subtotal: </p>
            </div>
            <div class="col p-2">
               <p class="fs-3" id="totalPrice">€ ${totalPrice.toFixed(2)}</p>
            </div>
          </div>
        `;

          cartContentHtml += `
          <div class="row mx-5 mb-5">
            <div class="col">
              <button class="btn btn-warning text-white shadow fs-5 ">
                <a href="shop.html" class="text-white text-decoration-none">Back to Shop</a>
              </button>
            </div>
            <div class="col">
              <button class="btn btn-success text-white shadow fs-5 " id="orderNowButton">
               <a href="#" class="text-white text-decoration-none"> Order Now</a>
              </button>
            </div>
          </div>
        `;

          cartItemsContainer.append(cartContentHtml);

          // When the "Order Now" button is clicked
          $("#orderNowButton").on("click", function (event) {
            event.preventDefault();
            console.log("Order Now button clicked.");

            // Send an AJAX request to create the order
            $.ajax({
              url: "http://localhost:8080/api/order/create",
              type: "POST",
              dataType: "json",
          contentType: "application/json",
          beforeSend: function (xhr) {
            var accessToken = sessionStorage.getItem("accessToken");
            xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
          },
              success: function (order) {
                // Handle the success response, e.g., show a confirmation message
                console.log("Order created:", order);
                // You can show a confirmation message or redirect the user to an order confirmation page.
              },
              error: function (xhr, status, error) {
                // Handle errors, e.g., display an error message to the user
                console.error("Error creating order:", error);
                // You can show an error message to the user.
              },
            });
          });
        } else {
          // Handle the case where the cart is empty
          $("#cartItemsContainer").html("<p>Your cart is empty</p>");
        }
      },
      error: function (xhr, status, error) {
        // Handle errors, e.g., unauthorized or server error
        console.error("Error fetching cart items: " + error);
        // Optionally, display an error message to the user
      },
    });
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
    <div class="col-md-6 mt-4 ">
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
  function fetchAndDisplayCartItemImages(cartItems) {
    cartItems.forEach(function (cartItem) {
      const apiUrl = "http://localhost:8080/api/files/" + cartItem.img;

      fetch(apiUrl)
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.blob();
        })
        .then((blob) => {
          const blobUrl = window.URL.createObjectURL(blob);

          const cartItemElement = $(`.cart-item[data-id="${cartItem.id}"]`);
          const imgElement = cartItemElement.find(".cart-item-image");

          imgElement.attr("src", blobUrl);
        })
        .catch((error) => {
          console.error("Fetch error:", error);
        });
    });
  }
});
