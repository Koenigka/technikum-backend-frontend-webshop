// Function to show a success toast
function showSuccessToast(message) {
  const toast = `<div class="toast bg-success text-white" role="alert" aria-live="assertive" aria-atomic="true">
                  <div class="toast-header">
                    <strong class="me-auto">Success</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                  </div>
                  <div class="toast-body">
                    ${message}
                  </div>
                </div>`;

  $("#toastContainer").append(toast);
  $(".toast").toast({ autohide: true, delay: 3000 }).toast("show");
}

// Function to show an error toast
function showErrorToast(message) {
  const toast = `<div
      class="toast position-absolute bottom-0 start-0 mb-5"
      id="noUsersFoundToast"
      role="alert"
      aria-live="assertive"
      aria-atomic="true"
    >
  <div class="toast-header" style="color: red; font-size: large">
        <strong class="me-auto">No Users Found</strong>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="toast"
          aria-label="Close"
        ></button>
      </div>
      <div class="toast-body" style="font-size: large">
        No users found with the given email or username.
      </div>
      </div>`;

  $("#toastContainer").append(toast);
  $(".toast").toast({ autohide: true, delay: 3000 }).toast("show");
}

// Function to clear all toasts
function clearToasts() {
  $("#toastContainer").empty();
}
