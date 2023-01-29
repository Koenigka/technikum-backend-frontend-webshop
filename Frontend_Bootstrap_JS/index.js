$(document).ready(function(){
    
$.ajax({
    url: "http://localhost:8080/categories",
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

const img = $(`<img src="${category.img}" class="card-img-top" alt="...">`);
const title = $(`<h5 class="card-title">${category.title}</h5>`);
const description = $(`<p class="card-text">${category.description}</p>`);
const button = $(`<a href="#" class="btn btn-warning text-white mt-auto">Buy ${category.title}</a>`);

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
