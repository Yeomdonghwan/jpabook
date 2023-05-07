package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = false)
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    //변경감지(dirty checking)
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){

        Item findItem = itemRepository.findOne(itemId);//DB에 있는 실제 값

        //아래의 코드들은 findItem.change()같은 메소드를 따로 만들어 처리하는것이 좋다.
//        findItem.setPrice(price);
//        findItem.setStockQuantity(stockQuantity);
//        findItem.setName(name);
        findItem.change(name,price,stockQuantity);

        //JPA는 영속성 컨텍스트의 변경을 감지하고 업데이트쿼리를 자동으로 보내 처리함
    }
    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }

}
