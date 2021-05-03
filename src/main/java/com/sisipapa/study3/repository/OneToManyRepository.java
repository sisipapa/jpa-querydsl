package com.sisipapa.study3.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study3.domain.otm.Member01;
import com.sisipapa.study3.domain.otm.QMember01;
import com.sisipapa.study3.domain.otm.QTeam01;
import com.sisipapa.study3.domain.otm.Team01;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@AllArgsConstructor
public class OneToManyRepository {

    private final JPAQueryFactory queryFactory;

    private EntityManager em;

    @Transactional
    public void team01AndMember01Save() {
        Member01 member = new Member01();
        member.setName("Member1");
        em.persist(member);
        System.out.println("### Member Save Complete");

        Team01 team = new Team01();
        team.setName("Team1");
        team.getMembers().add(member);
        em.persist(team);
        System.out.println("### Team Save Complete");
    }
}
