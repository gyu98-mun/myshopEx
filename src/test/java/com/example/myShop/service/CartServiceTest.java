package com.example.myShop.service;

import com.example.myShop.constant.ItemSellStatus;
import com.example.myShop.dto.CartItemDto;
import com.example.myShop.entity.CartItem;
import com.example.myShop.entity.Item;
import com.example.myShop.entity.Member;
import com.example.myShop.repository.CartItemRepository;
import com.example.myShop.repository.ItemRespository;
import com.example.myShop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional // 테스트 후 롤백 처리
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartServiceTest {
    @Autowired
    ItemRespository itemRespository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){
        Item item =new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRespository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }
    @Test
    @Deprecated
    public void addCart(){
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());

    }
}
