package maz.cart_microservice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = Cart.builder()
                    .userId(userId)
                    .build(); //todo understand this
            cart = cartRepository.save(cart);
        }
        return cart;

    }

    @Transactional
    public Cart addCartItem(Long itemId, Long userId) {
        Cart cart = cartRepository.findByUserId(userId); //todo diff btwn Cart & CartItem
        for (CartItem item : cart.getItems()) {
            if (item.getItemId().equals(itemId)){
                item.setAmount(item.getAmount() + 1); //todo - +1 is amount or the item?
                return cartRepository.save(cart);
            }
        }
        cart.addCartItem(itemId);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateAmount(Long userId, Long cartItemId, Integer amount) {
        Cart cart = cartRepository.findByUserId(userId);
        cart.getItems().stream()
                .filter(i -> i.getId().compareTo(cartItemId) == 0)
                .findFirst()
                .ifPresent(cartItem -> cartItem.setAmount(amount));
        return cart;
    }

    @Transactional
    public Cart removeCartItem(Long cartItemId, Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        cart.removeCartItem(cartItemId);
        return cartRepository.save(cart);
    }
}
