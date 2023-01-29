$(document).ready(function(){
    
    //Dropdown mit Kategorien laden
    $.ajax({
        url: "http://localhost:8080/categories",
        type: "GET",
        cors: true,
        success: function(categories) { addCategoriesToDropdown(categories) },
        error: function(error) { console.error(error) }
    })


    function addCategoriesToDropdown(categories) {
        const allCategories = $("#categories");
        allCategories.empty();
        
        for (let category of categories) {
            allCategories.append(createCategory(category));
        }
    }
        
    function createCategory(category) {        
            const list = $(`<li class="dropdown-item dropdown-item-warning fs-5" id="getProductsById" value="${category.id}">${category.title}</li>`);
        return list;
    }


    //Alle Produkte laden
    $.ajax({
        url: "http://localhost:8080/products",
        type: "GET",
        cors: true,
        success: function(products) { addProducts(products) },
        error: function(error) { console.error(error) }
    })
    
    function addProducts(products){        
        const allProducts = $("#products");
        allProducts.empty();        
        for (let product of products) {
            allProducts.append(createProduct(product));
        }
    }
    
    function createProduct(product) {

        const img = $(`<img src="${product.img}" class="card-img-top img-fluid" alt="...">`);
        const title = $(`<h5 class="card-title text-warning">${product.title}</h5>`);
        const description = $(` <p class="card-text">${product.description}</p>`);
        const button = $(`<button class="btn btn-warning mt-auto text-white">Add to Basket</button>`)
        
        const wrapper = $(`<div class="col-12 col-md-6 col-lg-3 mb-4 ">`);
        const card = $(`<div class="card h-100">`)
        wrapper.append(card);
        card.append(img);
        const cardbody = $(`<div class="card-body d-flex flex-column">`);
        card.append(cardbody);
        cardbody.append(title);
        cardbody.append(description);
        const veganoption = $(` <div class="form-check text-warning">
        <input class="form-check-input text-warning" type="checkbox" value="" id="flexCheckDefault">
        <label class="form-check-label text-warning" for="flexCheckDefault">
          Vegan Option
        </label>`)
        cardbody.append(veganoption);
        cardbody.append(button);
        cardbody.append(`</div>`);
        wrapper.append(`</div>`);
    
    return wrapper;
    }

    //Clickfunction Dropdown by ID --> list auf Buttons umschreiben ....
    $("getProductsById").click(function(e)  {     
        id = $(this).attr('value');  
        $.ajax({
            url: "http://localhost:8080/products/" + id ,            
            type: "GET",
            cors: true,
            success: function(products) { addProducts(products) },
            error: function(error) { console.error(error) }
        })
    });

    
    //Clickfunction Dropdown allCategories

    $("allCategories").click(function(e) {
        alert("allCategories clicked");
        $.ajax({
            url: "http://localhost:8080/products",
            type: "GET",
            cors: true,
            success: function(products) { addProducts(products) },
            error: function(error) { console.error(error) }
        })
    });
        
});