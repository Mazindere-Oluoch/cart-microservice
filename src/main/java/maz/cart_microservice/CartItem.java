package maz.cart_microservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem { //-> individual item inside a cart
/* *
   These two objects (Cart & CartItem) allow for more primitive/non-automatic entity rships,
    where the IDs are stored locally, but the info. needs to be requested from
    a diff service.*/

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long itemId;  //only store item_id not item obj bcz item info is in item micro service

    @Column(nullable = false)
    private Integer amount; // or quantity, not price
}

