$(document).ready(function(){
    

    
    const params = new Proxy(new URLSearchParams(window.location.search), {
        get: (searchParams, prop) => searchParams.get(prop),
      });
      // Get the value of "some_key" in eg "https://example.com/?some_key=some_value"
      let value = params.product; // "some_value"
    console.log(value);


    $.ajax({
        url: "http://localhost:8080/api/products/" + value,
        type: "GET",
        cors: true,
        success: function(product) { addProduct(product) },
        error: function(error) { console.error(error) }
    })

    function addProduct(product){        
        const productd = $("#product");
        productd.empty();   
        
        
        const productdetail = $(`
        <div class="row pt-3">
        <div class="col-lg-6 col-md-6 col-sm-12">
          <img class="inner-img img-fluid rounded border border-warning" src="${product.img}" width="350px"
            height="250px" />
        </div>
        <div class="col-lg-6 col-md-6 col-sm-12">
          <h3 class="text-warning text-decoration-none fs-1">
            <strong>${product.title}</strong>
          </h3>
          <p class="bg-white border border-warning rounded px-5 py-3 fs-5">
          <b>${product.description}</b>
          </p>
          <p class="bg-white border border-warning rounded px-3 py-2 fs-5 mx-5">
            <em>
                inkl. 7 % MwSt. zzgl. Versandkosten <br />
                
                100% Handmade vom Bäckermeister</em>
          </p>
          <p class="fs-3 text-warning"><b>EUR ${product.price}</b></p>
          <div class="qty mt-5 mb-3 counter">
            <span class="minus bg-light fs-3">-</span>
            <input type="number" class="count" name="qty" value="1" />
            <span class="plus bg-light fs-3">+</span>
          </div>
          <a href="#" class="btn btn-warning text-white fs-4">Add to Basket</a>
        </div>
      </div>`)


        productd.append(productdetail);
    }






});