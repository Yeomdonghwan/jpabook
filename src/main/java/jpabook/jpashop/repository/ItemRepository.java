package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public Long save(Item item){

        if(item.getId()==null)//새로 생성한 객체인 경우
            em.persist(item);
        else{//이미 DB에 등록되어있는 객체
            em.merge(item);
        }
        return item.getId();
    }

    public Item findOne(Long itemId){
        return em.find(Item.class,itemId);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i ",Item.class).getResultList();
    }

    public List<Item> findByName(String name){
        return em.createQuery("select i from Item i where i.name = :name",Item.class).setParameter("name",name).getResultList();
    }


}
