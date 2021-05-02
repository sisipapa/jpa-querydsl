package com.sisipapa.study3.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OneToManyRepositoryTest {

    @Autowired
    OneToManyRepository repository;

    @Test
    @DisplayName("1대N 단방향 테스트")
    public void team01AndMember01Save(){
        repository.team01AndMember01Save();
    }
}