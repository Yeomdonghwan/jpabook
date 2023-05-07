package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @GetMapping("/items/new")
    public String createForm(Model model){
        //빈 폼을 보냄으로써 유지보수하기 편해짐
        model.addAttribute("form",new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){
        //아래의 코드들은 Book.create() 같은 메소드를 만들어 사용하는것이 좋음. set은.. 쓰지못하게 하자.
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items"; //저장된 책 목록으로 리다이렉션
    }

    @GetMapping("/items")
    public String items(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList";

    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);//캐스팅하면 좋지 않으나 예제니까..

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", form);
        return "items/updateItemForm";

    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form, @PathVariable("itemId") Long itemId){

        //데이터베이스 식별자 id가 있는 준영속 객체
        //준영속 엔티티. 영속 엔티티는 JPA가 관리하기 때문에 변경을 감지하여 자동으로 업데이트하지만 준영속은 JPA가 관리하지 않아 업데이트 안됨
        //준영속 엔티티를 수정하는 방법 1.변경감지(dirty checking) 기능 사용/ 2.병합(merge) 사용
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setIsbn(form.getIsbn());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setAuthor(form.getAuthor());
//        book.setStockQuantity(form.getStockQuantity());

        //필요한 데이터만 사용하는 것이 간결하고 좋다. 인자가 많으면 Dto를 사용하는것도 좋음.
        itemService.updateItem(itemId,form.getName(),form.getPrice(),form.getStockQuantity());
        return "redirect:/items";

    }
}
