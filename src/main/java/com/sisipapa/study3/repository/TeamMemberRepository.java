package com.sisipapa.study3.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study3.entity.Team;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sisipapa.study3.entity.QMember1.member1;
import static com.sisipapa.study3.entity.QTeam.team;

@Repository
@AllArgsConstructor
public class TeamMemberRepository {

    private final JPAQueryFactory queryFactory;

    public List<Team> findAllWithMemberUsingJoin() {
        JPQLQuery<Team> query = queryFactory
                                    .selectFrom(team)
                                    .join(member1);

        List<Team> list = query.fetch();
        return list;
    }
}
