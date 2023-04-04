package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //싱글테이블 전략 사용
@DiscriminatorColumn(name="dtype") //book,movie,album을 구분하기 위한 컬럼
public abstract class Item {

    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //엔티티 내부 비즈니스 로직
    //데이터를 가지고있는 쪽에 비즈니스 로직이 있는것이 관리 용이. setter대신 addStock과 같은 비즈니스 로직 메소드를 만들어 사용
    //재고수량 증가
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }

    //재고수량 감소
    public void removeStock(int quantity){
        int restStock = this.stockQuantity-quantity;
        if(restStock<0){
            throw new NotEnoughStockException("need more stock");

        }
        this.stockQuantity=restStock;
    }
}
