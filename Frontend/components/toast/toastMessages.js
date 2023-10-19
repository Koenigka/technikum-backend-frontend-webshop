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
function fetchAndDisplayToastMessageImage(imageReference, targetElement) {
  const apiUrl = `http://localhost:8080/api/files/${imageReference}`;

  fetch(apiUrl)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.blob();
    })
    .then((blob) => {
      const blobUrl = window.URL.createObjectURL(blob);

      // Set the image source for the target element (e.g., an <img> element in your toast)
      targetElement.attr("src", blobUrl);
    })
    .catch((error) => {
      console.error("Fetch error:", error);
    });
}

// Function to show a success toast for adding a product to the cart
function showProductAddedToast(product) {
  const toast = `
    <div class="toast bg-success" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="toast-header" style="font-size: large">
       <p  class="me-auto">The product<strong> ${
         product.title
       } </strong>is added successfully into the basket</p>
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

  $("#toastContainer").append(toast);
  // Find the img element within the newly added toast
  const toastImageElement = $("#toastContainer .toast img");

  // TODO - here is 403 error????
  // Fetch and display the product image using the fetchAndDisplayToastMessageImage function
  fetchAndDisplayToastMessageImage(product.img, toastImageElement);
  $(".toast").toast({ autohide: true, delay: 3000 }).toast("show");
}

// Function to clear all toasts
function clearToasts() {
  $("#toastContainer").empty();
}
