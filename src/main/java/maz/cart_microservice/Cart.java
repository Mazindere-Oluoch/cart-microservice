package maz.cart_microservice;

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
    private Long userId; //find how user id got in Cart

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id") //FK connects each cart item to its cart
    private List<CartItem> items;  //can result in NPE if items is null


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

