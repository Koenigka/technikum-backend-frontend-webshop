const config = {
  baseUrl: "http://localhost:8080/api",
  file: {
    files: "/files",
  },
  product: {
    create: "/products/create",
    update: "/products/update",
    delete: "/products/delete/",
    findAll: "/products/findAll", 
    // findByActive: (active) => `/products/isActive/${active}`,
    findById: "/products/", 
    search: "/products/search",
    // findByCategory: (categoryId, active) =>
    //  `/products/byCategory/${categoryId}/${active}`, 
  },
category: {
    findByActive: "/categories/isActive/true",
},
user: {

},
cartitem: {

},
order: {

}, 
auth: {

}

};

export default config;
