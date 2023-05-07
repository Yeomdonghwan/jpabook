package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());


        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice(),count);

        //주문 생성
        Order order= Order.createOrder(member,delivery,orderItem);

        orderRepository.save(order); //cascade 옵션으로 인해 delivery,orderItem도 자동으로 persist 됨.
        //이 예제에서는 Order에서만 orderItem, delivery를 참조하기 때문에 cascade로 관리해도 괜찮음.
        //하지만 예를들어 delivery를 다른데서도 많이 사용한다면 cascade로 하지 말고 별도로 리포지토리를 사용하는것이 좋음.

        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
        /**
        * 더티체킹: 변경내역이 감지되어 데이터베이스에 업데이트 쿼리가 자동으로 나감
        * */
    }

    /**
     * 엔티티에 핵심 비즈니스 로직을 몰아넣는 스타일을 "도메인 모델 패턴"이라고 한다. 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할.
     * 상황에 따라 다르지만 JPA 사용할 때는 도메인 모델 패턴을 추천..?(객체지향형)
     * 반대경우는 트랜잭션 스크립트 패턴이라고 하는데, 상황에 따라 맞게 사용하는 것이 좋다.
     * */
    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
