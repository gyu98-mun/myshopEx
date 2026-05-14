package com.example.myShop.repository;

import com.example.myShop.entity.Item;
import com.example.myShop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    List<ItemImg> findByItemIdOrderByIdAsc(Long ItemId);
    ItemImg findByItemIdAndRepimgYn(Long itemid, String repimgYn);
    Long item(Item item);
}
