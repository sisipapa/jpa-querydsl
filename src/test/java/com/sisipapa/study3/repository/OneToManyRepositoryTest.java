package com.sisipapa.study3.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OneToManyRepositoryTest {

    @Autowired
    OneToManyRepository repository;

    @BeforeEach
    void setUp() {
        repository.joinTable();
    }

    @Test
    public void joinTable(){
        repository.joinTable();
    }

    @Test
    public void joinColumn(){
        repository.joinColumn();
    }

    @Test
    public void twoWay(){
        repository.twoWay();
    }
}