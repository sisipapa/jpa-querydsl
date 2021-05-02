package com.sisipapa.study3.repository;

import com.sisipapa.study3.dto.OrderSimpleQueryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class OrderQueryDslRepositoryTest {

    @Autowired
    private OrderQueryDslRepository repository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void findOrderDto(){
        List<OrderSimpleQueryDto> list = repository.findOrderDto();
        System.out.println(list.size());
    }
}