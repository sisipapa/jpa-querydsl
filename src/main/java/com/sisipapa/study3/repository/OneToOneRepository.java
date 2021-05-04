package com.sisipapa.study3.repository;

import com.sisipapa.study3.domain.oto.Address01;
import com.sisipapa.study3.domain.oto.Address02;
import com.sisipapa.study3.domain.oto.User01;
import com.sisipapa.study3.domain.oto.User02;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Repository
@AllArgsConstructor
public class OneToOneRepository {

    private EntityManager em;

    /**
     * 주 테이블 : User01
     * 대상테이블 : Address01
     * 일대일 단방향 - 주테이블에 외래키를 둔 경우
     * User객체를 통해 Address 정보를 조회할 수 있는 구조이다.
     */
    @Transactional
    public void oneToOneMainOneWay() {
        Address01 address = Address01.builder()
                            .addr("서울")
                            .build();
        em.persist(address);

        User01 user = User01.builder()
                        .name("유저1")
                        .address(address)
                        .build();
        em.persist(user);

        String jpql = "SELECT u FROM User01 u WHERE u.id = ?1";
        TypedQuery<User01> query = em.createQuery(jpql, User01.class);
        query.setParameter(1, 1L);
        User01 findUser = query.getSingleResult();
        Address01 userAddress = findUser.getAddress();

    }

    @Transactional
    public void oneToOneMainTwoWay() {
        Address02 address = Address02.builder()
                .addr("서울")
                .build();

        User02 user = User02.builder()
                .name("유저2")
                .address(address)
                .build();
        address.setUser(user);

        em.persist(user);
        em.persist(address);

        String jpql = "SELECT u FROM User02 u WHERE u.id = ?1";
        TypedQuery<User02> query = em.createQuery(jpql, User02.class);
        query.setParameter(1, 1L);
        User02 findUser = query.getSingleResult();
        Address02 userAddress = findUser.getAddress();

        String jpql2 = "SELECT a FROM Address02 a WHERE a.id = ?1";
        TypedQuery<Address02> query2 = em.createQuery(jpql2, Address02.class);
        query2.setParameter(1, 1L);
        Address02 findAdress = query2.getSingleResult();
        User02 addressUser = findAdress.getUser();
    }
}
