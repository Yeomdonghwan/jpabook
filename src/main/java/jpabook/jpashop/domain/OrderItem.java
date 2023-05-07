package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본생성자 접근을 막음(create메서드 사용해라라는 뜻)
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
        orderItem.item=item;

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
