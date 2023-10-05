// Function to close the edit window
function closeEditWindow() {
  const addEditCategory = $("#addEditCategory");
  addEditCategory.empty();
  $(".footer").addClass("fixed-bottom");
}

$(document).ready(function () {
  //TOGGLE FOR CREATE CATEGORY FORM
  $("#showNewCategory").click(function () {
    var accessToken = sessionStorage.getItem("accessToken");
    if (!accessToken) {
      // User is not logged in, redirect to login page with a message
      window.location.href =
        "login.html?message=You are not logged in! Please first log in and then you can continue your action.";
    } else {
      $("#createNewCategory").toggle();
    }
  });

  function validateCategory(category, createForm) {
    $(".input-error").text("");

    //Initialize isValid as true
    let isValid = true;

    if (!category.title || category.title.trim() === "") {
      //Validate input values
      isValid = false;
      $(`#titleError${createForm}`).text("Title is required.");
      $("#titleError-edit").text("Title is required.");
    }
    if (!category.description || category.description.trim() === "") {
      isValid = false;
      $(`#descriptionError${createForm}`).text("Description is required.");
      $("#descriptionError-edit").text("Description is required.");
    }
    const imageUrlPattern = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
    if (!category.imgUrl) {
      isValid = false;
      $(`#imgError${createForm}`).text("Image URL is required.");
      $("#imgError-edit").text("Image URL is required.");
    } else if (!imageUrlPattern.test(category.imgUrl)) {
      isValid = false;
      $(`#imgError${createForm}`).text("Invalid image URL format.");
      $("#imgError-edit").text("Invalid image URL format.");
    }
    return isValid;
  }

  //CREATE NEW CATEGORY
  $("#createCategoryButton").on("click", (_e) => {
    event.preventDefault();
    isActive = $("#isActive").is(":checked") ? true : false;

    const category = {
      title: $("#category-title").val(),
      description: $("#category-description").val(),
      imgUrl: $("#category-img-url").val(),
      //Check if is checked --> value = true/false
      active: isActive,
    };

    // Validate the product and get the result
    const isValid = validateCategory(category, "");

    if (isValid) {
      $.ajax({
        url: "http://localhost:8080/api/categories/create",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        beforeSend: function (xhr) {
          var accessToken = sessionStorage.getItem("accessToken");
          xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
        },
        data: JSON.stringify(category),
        success: function (response) {
          clearToasts();
          showSuccessToast("New category successfully created!");
          // Display the toast message
          const toast = new bootstrap.Toast(
            document.getElementById("toastContainer")
          );
          toast.show();
          $("#createNewCategory").hide();
        },
        error: console.error,
      });
    }
  });

  //SEARCH FUNCTION
  $(document).on("click", "#showSearchCategory", function (event) {
    const searchTitle = $("#category-name-search").val();
    const isActive = $("input[name='status']:checked").val();

    const filters = {};

    if (searchTitle) {
      filters["filter[title]"] = searchTitle;
    }

    if (isActive !== undefined) {
      filters["filter[active]"] = isActive;
    }

    const filterJSON = JSON.stringify(filters);
    console.log(filterJSON);

    // Close the edit window if it's open
    closeEditWindow();

    $.ajax({
      url: "http://localhost:8080/api/categories/search",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        if (!accessToken) {
          // User is not logged in, redirect to login page with a message
          window.location.href =
            "login.html?message=You are not logged in! Please first log in and then you can continue your action.";
        } else {
          xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
        }
      },
      data: filterJSON,
      success: function (categories) {
        addCategories(categories);
      },
      error: function (error) {
        console.error(error);
      },
    });

    //  $(".footer").removeClass("fixed-bottom");
  });

  //ADD SEARCHED CATEGORIES FROM DATABASE TO LIST
  function addCategories(categories) {
    const selectedStatus = $("input[name='status']:checked").val();
    const statusMessage = selectedStatus === "true" ? "active" : "inactive";
    const allSearchedCategories = $("#searchResult");
    allSearchedCategories.empty();

    if (categories.length === 0) {
      isActive = $("#search-status").prop("checked");
      clearToasts();
      showErrorToast(`No ${statusMessage} categories found.`);
      // Display a message when no users are found
      const toast = new bootstrap.Toast(
        document.getElementById("toastContainer")
      );
      toast.show();
      return;
    }

    for (let category of categories) {
      allSearchedCategories.append(createCategory(category));
    }
  }

  function createCategory(category) {
    const searchedCategory = $(`<tr>
    <td scope="col">${category.id}</td>
    <td scope="col">${category.title}</td>    
    <td scope="col"><button class="btn btn-outline-warning editCategory" value="${category.id}">edit</button></td>
    <td scope="col">
       <button class="btn btn-outline-danger delete" value="${category.id}">delete</button>
     
    </td>
  </tr>`);
    $(".footer").removeClass("fixed-bottom");
    return searchedCategory;
  }

  //LOAD CATEGORY TO EDIT FORM
  $(document).on("click", ".editCategory", function (event) {
    const id = event.target.value;

    // Scroll to the top of the edit window
    $("html, body").animate(
      {
        scrollTop: $("#addEditCategory").offset().top,
      },
      500
    ); // You can adjust the scroll speed (500ms in this example)

    $.ajax({
      url: "http://localhost:8080/api/categories/" + id,
      type: "GET",
      cors: true,
      success: function (category) {
        editCategories(category);
      },
      error: function (error) {
        console.error(error);
      },
    });

    const addEditCategory = $("#addEditCategory");
    addEditCategory.empty();

    function editCategories(category) {
      if (category.active == true) {
        categoryedit = "checked";
      } else {
        categoryedit = "";
      }

      const editCategory =
        $(`  <div class="container rounded mt-5 border border-warning bg-light shadow-lg">
      <p class="fs-4 fw-bold pt-2">Edit Category</p>
      <div class="row">
        <div class="col">
          <form> 
            <div class="row mt-3 mb-3 ">
              <div class="col-md-3">
                <div class="form-group">
                  <label for="category-id" class=" fs-5">Category Id</label>
                  <input id="category-id-edit" type="text" class="form-control " name="category-id-edit" value="${category.id}" required disabled>
                </div>
              </div>
              <div class="col-md-9">
                <div class="form-group">
                  <label for="category-name" class=" fs-5">Category Name</label>
                  <input id="category-name" type="text" class="form-control " name="category-name" value="${category.title}" required>
                <p class="input-error" id="titleError-edit" style="color: red"></p>
                  </div>
              </div>
            </div>
  
  
            <div class="row mb-3">
              <div class="col-md-12">
                <div class="form-group">
                  <label for="product-description" class=" fs-5">Category Description</label>
                  <textarea class="form-control" id="category-description-edit" rows="3">${category.description}</textarea>
                <p class="input-error" id="descriptionError-edit" style="color: red"></p>
                  </div>
              </div>
            </div>
  
  
            <div class="row mb-3">
              <div class="col-md-9">
              <div class="form-group">
                <label for="category-img" class="fs-5">Category Image Url</label>
                <div class="input-group mb-2">
                  <input class="form-control" type="text" id="category-img" name="category-img" value="${category.imgUrl}"required>
                   <p class="input-error" id="imgError-edit" style="color: red"></p>
                </div>
              </div>
            </div>
            
            <div class="col-md-3 d-flex align-items-end">
              <div class="form-check mb-2">
              <div class="form-group">
                <input type="checkbox" class="form-check-input status" name="status"  ${categoryedit}>
                <label class="form-check-label  fs-5" for="status">
                  active
                </label>
              </div>
            </div>
            </div>
            </div>


            <button type="button" class="btn btn-warning text-white float-end mt-2 mb-2" id="saveEditCategory"> save</button>
  
          </form>
        </div>
      </div>
    </div>`);

      addEditCategory.append(editCategory);
    }
  });

  //EDIT CATEGORY

  $(document).on("click", "#saveEditCategory", function (event) {
    isActive = $(".status").is(":checked") ? true : false;

    //console.log(id);
    const category = {
      id: $("#category-id-edit").val(),
      title: $("#category-name").val(),
      description: $("#category-description-edit").val(),
      imgUrl: $("#category-img").val(),
      active: isActive,
    };

    // Validate the product and get the result
    const isValid = validateCategory(category, "-edit");

    if (isValid) {
      $.ajax({
        url: "http://localhost:8080/api/categories/update",
        type: "PUT",
        dataType: "json",
        contentType: "application/json",
        beforeSend: function (xhr) {
          var accessToken = sessionStorage.getItem("accessToken");
          xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
        },
        data: JSON.stringify(category),
        success: function (response) {
          clearToasts();
          showSuccessToast("Updated successfully!");
          // Display the toast message
          const toast = new bootstrap.Toast(
            document.getElementById("toastContainer")
          );
          toast.show();
          $("#addEditCategory").empty(); // Clear the edit product form
        },
        error: console.error,
      });
    }
  });

  //DELETE CATEGORY
  $(document).on("click", ".delete", function (event) {
    const deleteId = event.target.value;

    // Show the delete confirmation modal
    $("#deleteCategoryModal").modal("show");

    // When the delete button in the modal is clicked, perform the deletion
    $("#confirmDelete").click(function () {
      $.ajax({
        url: "http://localhost:8080/api/categories/delete/" + deleteId,
        type: "DELETE",
        dataType: "json",
        contentType: "application/json",
        beforeSend: function (xhr) {
          var accessToken = sessionStorage.getItem("accessToken");
          xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
        },
        success: function (response) {
          console.log("Successfully deleted:", response);
          // Show a toast message
          showDeleteToast("Category deleted successfully.");
          const toast = new bootstrap.Toast(
            document.getElementById("toastContainer")
          );
          toast.show();
          // Hide the modal after deletion
          $("#deleteCategoryModal").modal("hide");
          // Reload the page after a delay (e.g., 2 seconds)
          setTimeout(function () {
            location.reload();
          }, 2000);
        },
        error: function (xhr, textStatus, error) {
          console.error("Error deleting:", error);
        },
      });
    });
  });
});
