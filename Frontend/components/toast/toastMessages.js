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

// Function to clear all toasts
function clearToasts() {
  $("#toastContainer").empty();
}
