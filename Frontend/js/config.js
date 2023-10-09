const config = {
  baseUrl: "http://localhost:8080/api",
  file: {
    files: "/files",
    delete: "/files/"
  },
  product: {
    create: "/products/create",
    update: "/products/update",
    delete: "/products/delete/",
    findAll: "/products/findAll", 
    findById: "/products/", 
    search: "/products/search",
    findByActive: "/products/isActive/true",
    findByCategory: (categoryId, active) =>
     `/products/byCategory/${categoryId}/${active}`, 
  },
category: {
    findByActive: "/categories/isActive/true",
    create: "/categories/create",
    search: "/categories/search",
    findById: "/categories/", 
    update: "/categories/update",
    delete: "/categories/delete/",

},
user: {
  search: "/users/search",
  findById: "/users/", 
  update: "/users/update",
  delete: "/users/delete/",

},

cartItem: {
  addToCart: "/cart/add",
  myCart: "/cart/myCart",

},
order: {

}, 
auth: {

}

};

export default config;
