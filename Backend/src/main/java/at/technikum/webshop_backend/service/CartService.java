package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.model.Cart;
import at.technikum.webshop_backend.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private CartRepository cartRepository;


    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

;
    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    public Cart findByUserId(Long userId){
        return cartRepository.findByUserId(userId);
    }

}
