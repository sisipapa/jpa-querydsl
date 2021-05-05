package com.sisipapa.study3.repository;

import com.sisipapa.study3.domain.*;
import com.sisipapa.study3.dto.OrderSearch;
import com.sisipapa.study3.dto.OrderSimpleQueryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;


@SpringBootTest
class OrderQueryDslRepositoryTest {

    @Autowired
    private OrderQueryDslRepository repository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void findAllByString(){
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setSearchWord("111");
        List<Order> list = repository.findAllByString(orderSearch);
        Assert.isNull(list, "orderName 필드에 111인 데이터가 없습니다.");
    }

    @Test
    public void findOrderDto(){
        List<OrderSimpleQueryDto> list = repository.findOrderDto();
        System.out.println(list.size());
    }

    @Test
    public void findAllWithItem(){
        List<Order> list = repository.findAllWithItem();
        System.out.println(list.size());
    }

//    @Test
//    public void findAllWithMemberDelivery(){
//        List<Order> orders = repository.findAllWithMemberDelivery();
//        List<OrderDto> result = orders.stream()
//                .map(order -> new OrderDto(order))
//                .collect(Collectors.toList());
//        System.out.println(result.size());
//    }

}