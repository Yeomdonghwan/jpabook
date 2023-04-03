package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;


    @ManyToMany//실무에선 사용하지 않는다고 함
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    //아래는 카테고리 계층구조를 위해 category끼리 관계를 나타냄
    @ManyToOne
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy="parent")
    private List<Category> children= new ArrayList<>();
}
