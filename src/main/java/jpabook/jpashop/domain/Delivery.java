package jpabook.jpashop.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Delivery {

    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY) //연관관계의 주인을 delivery로 줘도 되지만 액세스를 많이하게되는 order을 주인으로 하였음. 둘 중 무엇을 선택해도 됨..
    private Order order;

    @Embedded //내장타입
    private Address address;

    @Enumerated(EnumType.STRING) //Ord로 하면 각 enum이 숫자 0,1,...로 들어가는데, 이런 경우 enum이 수정되면..모든 정보가 다 영향 감!!
    private DeliveryStatus status;//READY, COMP
}
