package maz.cart_microservice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import maz.cart_microservice.entity.Cart;
import maz.cart_microservice.entity.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;

    //communicating w other MS
    private void validateUser(Long userId){
        restTemplate.getForEntity(
                "http://USER-MICROSERVICE/user/{id}", // MS name & endpoint
                Object.class, // expected res type
                userId
        );
    }

    private void validateItem(Long itemId){
        restTemplate.getForEntity(
                "http://ITEM-MICROSERVICE/item/{id}",
                Object.class, // we use obj.class- only want to verify the user/item exists. We don't need their full details yet.
                itemId
        );
    }


    public Cart getCartByUserId(Long userId) {
//        check whether user exists in user MS
        validateUser(userId);

        Cart cart = cartRepository.findByUserId(userId);
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
        validateItem(itemId);

        //get user's cart
        Cart cart = getCartByUserId(userId);

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
