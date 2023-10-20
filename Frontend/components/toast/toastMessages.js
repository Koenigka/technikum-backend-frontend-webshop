// Function to show a success toast
function showSuccessToast(message) {
  const toast = `<div class="toast bg-success position-fixed bottom-0 start-0 mb-5" role="alert" aria-live="assertive" aria-atomic="true">
                  <div class="toast-header" style="font-size: large">
                    <strong class="me-auto">Success</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                  </div>
                  <div class="toast-body text-white" style="font-size: large">
                    ${message}
                  </div>
                </div>`;

  $("#toastContainer").append(toast);
  $(".toast").toast({ autohide: true, delay: 3000 }).toast("show");
}

// Function to show an error toast
function showErrorToast(message) {
  const toast = `<div
      class="toast position-fixed bottom-0 start-0 mb-5"
      id="noUsersFoundToast"
      role="alert"
      aria-live="assertive"
      aria-atomic="true"
    >
  <div class="toast-header" style="color: red; font-size: large">
        <strong class="me-auto">Error</strong>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="toast"
          aria-label="Close"
        ></button>
      </div>
      <div class="toast-body" style="font-size: large"> ${message}
      
      </div>
      </div>`;

  $("#toastContainer").append(toast);
  $(".toast").toast({ autohide: true, delay: 3000 }).toast("show");
}

function showDeleteToast(message) {
  const toast = `<div class="toast bg-success position-fixed bottom-0 start-0 mb-5" role="alert" aria-live="assertive" aria-atomic="true">
                  <div class="toast-header" style="font-size: large">
                    <strong class="me-auto">Success</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                  </div>
                  <div class="toast-body text-white" style="font-size: large">
                    ${message}
                  </div>
                </div>`;

  $("#toastContainer").append(toast);
  $(".toast").toast({ autohide: true, delay: 3000 }).toast("show");
}
// Function to fetch and return a Blob URL for an image
function fetchAndDisplayToastMessageImage(imageReference) {
  return new Promise((resolve, reject) => {
    const apiUrl = `http://localhost:8080/api/files/${imageReference}`;

    fetch(apiUrl)
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.blob();
      })
      .then(blob => {
        resolve(window.URL.createObjectURL(blob)); 
      })
      .catch(error => {
        console.error("Fetch error:", error);
        reject(error); // Rückgabe des Fehlers im Fehlerfall
      });
  });
}

// Function to show a success toast for adding a product to the cart
function showProductAddedToast(product, clickEvent) {
  const toast = `
    <div class="toast bg-warning position-fixed" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="toast-header" style="font-size: large">
        <p class="me-auto">The product <strong>${
          product.title
        }</strong> is added successfully into the basket</p>
        <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
      <div class="toast-body text-white" style="font-size: large">
        <img src="${product.img}" alt="${
    product.title
  }" class="mr-2" style="max-width: 50px; max-height: 50px">
        <p>Quantity: ${product.quantity} x Price: €${product.price.toFixed(
    2
  )}</p>
      </div>
    </div>`;

  // Append the toast to the body
  $("body").append(toast);

  // Calculate the position based on the click event
  const toastContainer = $(".toast.position-fixed");
  toastContainer.css("top", clickEvent.clientY - 180); // You can adjust the position as needed

  // Find the img element within the newly added toast
  const toastImageElement = $(".toast img");

  // Fetch and display the product image using the fetchAndDisplayToastMessageImage function
  fetchAndDisplayToastMessageImage(product.img, toastImageElement);

  // Show the toast
  $(".toast").toast({ autohide: true, delay: 3000 }).toast("show");

}

function showInsufficientQuantityToast(products) {
  const toast = `
    <div class="toast position-fixed bottom-0 start-0 mb-5" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="toast-header" style="color: red; font-size: x-large">
        <strong class="me-auto">Insufficient Quantity</strong>
        <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
      <div class="toast-body" style="font-size: x-large;">
        <ul>
          ${products
            .map(
              (product) =>
                `<li style= " list-style-type: none;"><i class='fas fa-cookie-bite fa-bounce'></i><strong> ${product.name}: Only ${product.availableQuantity}</strong> items available</li>
                
       `
            )
            .join("")}
        </ul>
        <p>   Please reduce the quantity of this product or choose something else.<br>
        Sorry for the inconvenience!</p>
         <button class="btn btn-warning mt-3 text-white fs-4" id="backToCartButton">Back to Cart</button>
      </div>
    </div>`;

  $("#toastContainer").append(toast);
  $(".toast").toast({ autohide: true, delay: 7000 }).toast("show");

  // Handle the "Back to Cart" button click
  $("#backToCartButton").on("click", function () {
    // Redirect the user back to the cart page
    window.location.href = "/pages/cart.html"; // Replace with the actual URL of your cart page
  });
}

// Function to clear all toasts
function clearToasts() {
  $("#toastContainer").empty();
}
