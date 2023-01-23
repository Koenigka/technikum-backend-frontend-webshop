$(document).ready(function(){
$.ajax({
    url: "http://localhost:8080/categories",
    type: "GET",
    cors: true,
    success: function(categories) { addCategoriesToPage(categories) },
    error: function(error) { console.error(error) }
})
});


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
const description = $(`<p class="${category.description}"</p>`);
const button = $(`<a href="#" class="btn btn-warning text-white">Buy ${category.title}</a>`);

const wrapper = $(`<div class="col-12 col-md-6 col-lg-3 mb-4 ">`);
const card = wrapper.append(`<div class="card" style="width: 100%;">`);
card.append(img);
const cardbody = card.append(`<div class="card-body">`);
cardbody.append(title);
cardbody.append(description);
cardbody.append(button);
cardbody.append(`</div>`);
wrapper.append(`</div>`);


return wrapper;
}

