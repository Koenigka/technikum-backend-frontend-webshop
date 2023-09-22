package at.technikum.webshop_backend.service;


import at.technikum.webshop_backend.model.Cart;
import at.technikum.webshop_backend.model.Position;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.PositionRepository;
import at.technikum.webshop_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final UserRepository userRepository;

    private final CartService cartService;

    private final ProductService productService;

    public PositionService(PositionRepository positionRepository,
                           UserRepository userRepository,
                           CartService cartService,
                           ProductService productService) {
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.productService = productService;
    }
    public Optional<Position> findById(Long id){
            return positionRepository.findById(id);
    }

    public Position save(Position position, Long userId, Long ProductId){
        Cart cart = cartService.getCartByUserId(userId);

        if(cart == null){
            Optional<User> user = userRepository.findById(userId);

            if (user.isPresent()){
                //cart = cartService.addProductToCart(new Cart(user.get()));

            }
            else {
                throw new RuntimeException("User does not exist");
            }
        }

        Optional<Product> product = productService.findById(ProductId);
        if (product.isEmpty()){
            throw new RuntimeException("Product does not exist");
        }

        position.setCart(cart);
        position.setProduct(product.get());

        return positionRepository.save(position);

    }

}
