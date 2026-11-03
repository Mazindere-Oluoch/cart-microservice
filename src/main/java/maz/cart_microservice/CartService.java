package maz.cart_microservice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId); //shld first verify user in user MS-NOT IMPLEMENTED
        if (cart == null) {  //create cart for user if it doesn't exist
            cart = Cart.builder()
                    .userId(userId)
                    .build();
            cart = cartRepository.save(cart);
        }
        return cart;

    }

    @Transactional
    public Cart addCartItem(Long itemId, Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        for (CartItem item : cart.getItems()) { //cart item objs in a cart
            if (item.getItemId().equals(itemId)) { // checks whether the item being added already exists in the cart
                item.setAmount(item.getAmount() + 1); //if it exists, increment its qty //todo impl this in my ecommerce
                return cartRepository.save(cart);
            }
        }
        //add item in cart if it doesn't already exist
        cart.addCartItem(itemId);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateAmount(Long userId, Long cartItemId, Integer amount) { //updates qty of an existing cart item
        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        //todo impl CartItem actually belongs to the specified user's cart.
        Cart cart = cartRepository.findByUserId(userId);
        cart.getItems().stream()
                .filter(i -> i.getId().compareTo(cartItemId) == 0) //todo revisit these collections methods
                .findFirst()
                .ifPresent(cartItem -> cartItem.setAmount(amount));
        return cart;
    }

    @Transactional
    public Cart removeCartItem(Long cartItemId, Long userId) { //delete cart item from a cart
        Cart cart = cartRepository.findByUserId(userId);
        cart.removeCartItem(cartItemId);
        return cartRepository.save(cart);
    }
}
