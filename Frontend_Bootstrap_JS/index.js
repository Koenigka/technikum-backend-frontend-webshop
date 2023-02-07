$(document).ready(function(){
    
$.ajax({
    url: "http://localhost:8080/categories/" + true,
    type: "GET",
    cors: true,
    success: function(categories) { addCategoriesToPage(categories) },
    error: function(error) { console.error(error) }
})



function addCategoriesToPage(categories) {
const categoriesContainer = $("#categoriesContainer");
categoriesContainer.empty();

for (let category of categories) {
    categoriesContainer.append(createCategory(category));
}
}

function createCategory(category) {

const img = $(`<a href="shop.html?category=${category.id}"><img src="${category.imgUrl}" class="card-img-top" alt="..."></a>`);
const title = $(`<h5 class="card-title">${category.title}</h5>`);
const description = $(`<p class="card-text">${category.description}</p>`);
const button = $(`<a href="shop.html?category=${category.id}" class="btn btn-warning text-white mt-auto">Buy ${category.title}</a>`);

const wrapper = $(`<div class="col-12 col-md-6 col-lg-3 mb-4 ">`);
const card = $(`<div class="card h-100" style="width: 100%;">`);
wrapper.append(card);
card.append(img);
const cardbody = $(`<div class="card-body d-flex flex-column">`);
card.append(cardbody);
cardbody.append(title);
cardbody.append(description);
cardbody.append(button);
cardbody.append(`</div>`);
wrapper.append(`</div>`);


return wrapper;
}

});
