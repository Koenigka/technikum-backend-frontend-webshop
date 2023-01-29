package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findAll();

    List<Product> findAllById(int category_id);


    /*
    @GetMapping("")
    public List<Product> readAll(){

        return this.products;
    }


    @GetMapping ("/{id}")
    public Product read(@PathVariable Long id){

        for (Product product : this.products) {
            if(id.equals( product.getId())){
                return product;
            }
        }
        //404 question not found
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public Product update(){
        return null;
    }

    @DeleteMapping("/{id}")
    public Product delete(){
        return null;
    }*/

}
