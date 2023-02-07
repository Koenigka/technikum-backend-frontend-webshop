$(document).ready(function(){
    

    const params = new Proxy(new URLSearchParams(window.location.search), {
        get: (searchParams, prop) => searchParams.get(prop),
      });
      // Get the value of "some_key" in eg "https://example.com/?some_key=some_value"
      let value = params.category; // "some_value"
    //console.log(value);




    //Buttons mit Kategorien laden
    $.ajax({
        
        url: "http://localhost:8080/categories/" + true,
        type: "GET",
        cors: true,
        success: function(categories) { addCategories(categories) },
        error: function(error) { console.error(error) }
    })


    function addCategories(categories) {
        const allCategories = $("#categories");
        allCategories.empty();
        
        for (let category of categories) {
            allCategories.append(createCategory(category));
        }
    }
        
    function createCategory(category) {        
            const button = $(`<button class="col-md-4 me-2 mb-2  btn btn-warning text-white fs-4 getProductsById" value="${category.id}">${category.title}</button>`);
        return button;
    }


    if (value == null){

    //Alle Produkte laden
    $.ajax({
        url: "http://localhost:8080/products/isActive/" + true,
        type: "GET",
        cors: true,
        success: function(products) { addProducts(products) },
        error: function(error) { console.error(error) }
    })
    }

    if (value != null){
         
        $.ajax({
            url: "http://localhost:8080/products/byCategory/" + value + "/" + true ,            
            type: "GET",
            cors: true,
            success: function(products) { addProducts(products) },
            error: function(error) { console.error(error) }
        })
    }
    
    function addProducts(products){        
        const allProducts = $("#products");
        allProducts.empty();        
        for (let product of products) {
            allProducts.append(createProduct(product));
        }
    }

 
    
    function createProduct(product) {

        const img = $(`<a  href="productdetail.html?product=${product.id}"><img src="${product.img}" class="card-img-top img-fluid" alt="..."></a>`);
        
        const title = $(`<a href="productdetail.html?product=${product.id}" class="text-decoration-none text-warning"><h5 class="card-title text-warning">${product.title}</h5></a>`);
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

    //Clickfunction Button by value (id) 
    $(document).on("click", ".getProductsById", function(event){
        console.log('clicked');
        id = $(this).attr('value');  
        $.ajax({
            url: "http://localhost:8080/products/byCategory/" + id + "/" + true ,            
            type: "GET",
            cors: true,
            success: function(products) { addProducts(products) },
            error: function(error) { console.error(error) }
        })
    });

    

    
    //Clickfunction allCategories

    $("#allCategories").click(function(e) {
        console.log("allCategories clicked");
        $.ajax({
            url: "http://localhost:8080/products/isActive/" + true,
            type: "GET",
            cors: true,
            success: function(products) { addProducts(products) },
            error: function(error) { console.error(error) }
        })
    });


        
});