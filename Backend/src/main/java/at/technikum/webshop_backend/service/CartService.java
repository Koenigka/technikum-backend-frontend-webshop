package at.technikum.webshop_backend.service;

import ErrorHandling.ProductNotFoundException;
import ErrorHandling.UserNotFoundException;
import at.technikum.webshop_backend.model.Cart;
import at.technikum.webshop_backend.model.Position;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.CartRepository;
import at.technikum.webshop_backend.repository.ProductRepository;
import at.technikum.webshop_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public void addProductToCart(Long userId, Long productId, Integer quantity) {
        // Finden Sie das Produkt basierend auf der Produkt-ID
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            throw new ProductNotFoundException("Product with ID " + productId + " not found");
        }

        // Finden Sie den Benutzer basierend auf der Benutzer-ID
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }

        // Finden Sie den Warenkorb des Benutzers oder erstellen Sie einen neuen
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }

        // Suchen oder erstellen Sie die Position im Warenkorb
        Position position = cart.getPositions()
                .stream()
                .filter(p -> p.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (position == null) {
            position = new Position();
            position.setProduct(product);
            position.setQuantity(quantity);
            position.setCart(cart);
            cart.getPositions().add(position);
        } else {
            position.setQuantity(position.getQuantity() + quantity);
        }

        // Speichern Sie den aktualisierten Warenkorb
        cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId) {
        // Finden Sie den Benutzer basierend auf der Benutzer-ID
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }

        // Finden Sie den Warenkorb des Benutzers oder geben Sie null zurück, wenn kein Warenkorb gefunden wurde
        return cartRepository.findByUser(user);
    }
}


    /* vorher:
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    public Cart save(Cart cart){
        return null;
    }

    public Cart findByUserId(Long userId){
        return cartRepository.findByUserId(userId);
    }
     */





