package com.example.myShop.entity;

import com.example.myShop.constant.OrderStatus;
import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.SpringVersion;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_order")
@Getter @Setter
public class Order extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem); // orderItem에는 주문 상품 정보들을 담아주고, orderItem 객체를 order객체의 orderItems에 추가
        orderItem.setOrder(this);// Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계 이므로, orderItem 객체에도 order 객체를 세팅
    }
    public  static Order createOrder(Member member, List<OrderItem> orderItemList){
     Order order =new Order();
     order.setMember(member);
     for (OrderItem orderItem : orderItemList){
         order.addOrderItem(orderItem);
     }
     order.setOrderStatus(OrderStatus.ORDER);
     order.setOrderDate(LocalDateTime.now());
     return order;
    }
    public int getTotalPrice(){
        int totalPrice =0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotaPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }
}
