package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();

        Item item = createItem("JPABook", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId= orderService.order(member.getId(),item.getId(),orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(),"주문시 상태는 ORDER");
        assertEquals(1,getOrder.getOrderItems().size(),"주문 상품 종류");
        assertEquals(10000 * 2 , getOrder.getTotalPrice(),"총 가격");
        assertEquals(8, item.getStockQuantity());
    }

    private Item createItem(String name, int price, int stockQuantity) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        em.persist(item);
        return item;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("mem1");
        member.setAddress(new Address("cit","st","zip"));
        em.persist(member);
        return member;
    }

    @Test
    public void 상품주문_수량초과() throws Exception{
        //given
        Member member = createMember();
        Item item = createItem("JPABook", 10000, 10);
        int orderCount = 11;
        //when

        //then

        assertThrows(NotEnoughStockException.class,()->{
            orderService.order(member.getId(),item.getId(),orderCount);
        });
    }

    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Item item = createItem("Book",10000,10);
        int orderCount = 10;
        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        OrderStatus result = getOrder.getStatus();

        assertEquals(OrderStatus.CANCEL,result,"status");
        assertEquals(10, getOrder.getOrderItems().get(0).getItem().getStockQuantity(),"stock");
    }

}