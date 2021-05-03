package com.sisipapa.study3.repository;

import com.sisipapa.study3.domain.otm.Member01;
import com.sisipapa.study3.domain.otm.Member02;
import com.sisipapa.study3.domain.otm.Team01;
import com.sisipapa.study3.domain.otm.Team02;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@AllArgsConstructor
public class OneToManyRepository {

    private EntityManager em;

    /**
     * @JoinTable을 사용한 단방향 연관관계
     */
    @Transactional
    public void joinTable() {
        Team01 team = new Team01("team01");

        team.addMember(new Member01("member01"));
        team.addMember(new Member01("member02"));
        team.addMember(new Member01("member03"));
        team.addMember(new Member01("member04"));

        em.persist(team);
    }

    @Transactional
    public void joinColumn() {
        Team02 team = new Team02("team01");

        team.addMember(new Member02("member01"));
        team.addMember(new Member02("member02"));
        team.addMember(new Member02("member03"));
        team.addMember(new Member02("member04"));

        em.persist(team);
    }

}
