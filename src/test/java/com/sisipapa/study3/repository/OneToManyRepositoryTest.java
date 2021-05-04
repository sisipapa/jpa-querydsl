package com.sisipapa.study3.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OneToManyRepositoryTest {

    @Autowired
    OneToManyRepository repository;

    /**
     * JoinTable 단방향 연관관계
     */
    @Test
    public void joinTable(){
        repository.joinTable();
    }

    /**
     * JoinColumn 단방향 연관관계
     */
    @Test
    public void joinColumn(){
        repository.joinColumn();
    }

    /**
     * 양방향 연관관계(mappedBy)
     */
    @Test
    public void twoWay(){
        repository.twoWay();
    }
}