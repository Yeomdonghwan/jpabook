package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @Column
    private int orderPrice; //주문 가격

    @Column
    private int count; //주문 수량

    //==생성메서드==
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.orderPrice=orderPrice;
        orderItem.count=count;

        item.removeStock(count); //아이템의 재고 감소
        return orderItem;

    }

    //==비즈니스로직==
    public void cancel() {
        getItem().addStock(count); //재고수량 원복
    }

    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
