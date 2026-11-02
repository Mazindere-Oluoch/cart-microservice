package maz.cart_microservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
//    Since you don't typically manually create or delete a cart, this class is not simply CRUD n' done.
//    It is catered more towards what one actually does with a cart. You can get the cart, add/remove items,
//    and change the amount of an item currently in the cart.

    private final CartService cartService;

    @GetMapping("/{userId}") //linking a cart to a user
    public ResponseEntity<?> getCartByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(cartService.getCartByUserId(userId));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }

    }

    @PostMapping("/{userId}") //add item to cart by user
    public ResponseEntity<?> addNewCartItem(
            @RequestParam("item-id") Long itemId,
            @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(cartService.addCartItem(itemId, userId));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{userId}/{cartItemId}")
    public ResponseEntity<?> updateItemAmount(
            @PathVariable("userId") Long userId,
            @PathVariable("cartItemId") Long cartItemId,
            @RequestParam("amount") Integer amount) {

        try {
            return ResponseEntity.ok(cartService.updateAmount(userId, cartItemId, amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @DeleteMapping("/{userId}/{cartItemId}") //todo cartItem id- check this
    public ResponseEntity<?> removeCartItem(
            @PathVariable("cartItemId") Long cartItemId,
            @PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok(cartService.removeCartItem(
                    cartItemId, userId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}

