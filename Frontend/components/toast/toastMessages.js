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
function showProductAddedToast(product) {
  fetchAndDisplayToastMessageImage(product.img)
    .then(blobUrl => {
      const toast = `
        <div class="toast bg-success" role="alert" aria-live="assertive" aria-atomic="true">
          <div class="toast-header" style="font-size: large">
            <p class="me-auto">Das Produkt <strong>${product.title}</strong> wurde erfolgreich in den Warenkorb gelegt</p>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
          </div>
          <div class="toast-body text-white" style="font-size: large">
            <img src="${blobUrl}" alt="${product.title}" class="mr-2" style="max-width: 50px; max-height: 50px">
            <p>Menge: ${product.quantity} x Preis: €${product.price.toFixed(2)}</p>
          </div>
        </div>`;

      $("#toastContainer").append(toast);
      $(".toast").toast({ autohide: true, delay: 3000 }).toast("show");
    })
    .catch(error => {
      console.error("Fetch error:", error);
    });
}

// Function to clear all toasts
function clearToasts() {
  $("#toastContainer").empty();
}
