package maz.cart_microservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart { // entire shopping basket

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Builder.Default // so that @Builder doesn't set "items" back to null
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id") //FK connects each cart item to its cart
    private List<CartItem> items = new ArrayList<>();  /* changed from just List<CartItem> items to avoid NPE-
    initializes the field with an actual empty list object instead of leaving it as null- mostly used in 1:M */


    public void addCartItem(Long itemId) {
        items.add(CartItem.builder()
                .itemId(itemId)
                .amount(1)
                .build());
    }

    public void removeCartItem(Long cartItemId) {
        items.removeIf(ci -> ci.getId().equals(cartItemId)); //todo revisit java collections
    }
}

