function handleErrors(errors) {
  errors.forEach((error) => {
    const message = error.field + ": " + error.defaultMessage;
    const errorDisplay = new ErrorDisplay(message);
    errorDisplay.display();
  });
}

function ErrorDisplay(errorMessage) {
  this.toastTemplate = `
        <div class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <div class="rounded me-2" style="background-color: red; width: 15px; height: 15px;"></div>
                <strong class="ml-5">Error</strong>
            </div>
            <div class="toast-body">
                <p>$MESSAGE</p>
            </div>
        </div>
    `;

  this.errorMessage = errorMessage;

  this.display = function () {
    const toast = $(this.toastTemplate.replace("$MESSAGE", this.errorMessage));
    $(".toast-container").append(toast);
    bootstrap.Toast.getOrCreateInstance(toast).show();
  };
}
