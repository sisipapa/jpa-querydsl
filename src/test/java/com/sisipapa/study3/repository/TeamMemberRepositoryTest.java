package com.sisipapa.study3.repository;

import com.sisipapa.study3.domain.Order;
import com.sisipapa.study3.dto.OrderSearch;
import com.sisipapa.study3.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
class TeamMemberRepositoryTest {

    @Autowired
    private TeamMemberRepository repository;

    @Test
    public void findAllWithMemberUsingJoin(){
        List<Team> list = repository.findAllWithMemberUsingJoin();
        list.forEach(System.out::println);
    }

}
