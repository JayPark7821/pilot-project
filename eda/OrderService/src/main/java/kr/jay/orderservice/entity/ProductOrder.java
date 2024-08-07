package kr.jay.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.jay.orderservice.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ProductOrder
 *
 * @author jaypark
 * @version 1.0.0
 * @since 4/5/24
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long productId;
    private Long count;
    private OrderStatus orderStatus;
    private Long paymentId;
    private Long deliveryId;
    private String deliveryAddress;

    public ProductOrder(
        Long userId,
        Long productId,
        Long count,
        OrderStatus orderStatus,
        Long paymentId,
        Long deliveryId
    ) {
        this.userId = userId;
        this.productId = productId;
        this.count = count;
        this.orderStatus = orderStatus;
        this.paymentId = paymentId;
        this.deliveryId = deliveryId;
    }

    public void finishOrder(
        Long paymentId,
        Long deliveryId
    ){
        this.paymentId = paymentId;
        this.deliveryId = deliveryId;
        this.orderStatus = OrderStatus.DELIVERY_REQUESTED;
    }

    public void updateStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void updateDeliveryAddress(String deliveryAddress){
        this.deliveryAddress = deliveryAddress;
    }
}
