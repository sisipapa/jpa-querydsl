package com.sisipapa.study3.repository;

import com.sisipapa.study3.domain.oto.Address01;
import com.sisipapa.study3.domain.oto.Address02;
import com.sisipapa.study3.domain.oto.User01;
import com.sisipapa.study3.domain.oto.User02;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class OneToOneRepositoryTest {

    @Autowired
    private User01Repository user01Repository;
    @Autowired
    private User02Repository user02Repository;
    @Autowired
    private Address01Repository address01Repository;
    @Autowired
    private Address02Repository address02Repository;

    @BeforeEach
    void setUp() {
    }

    /**
     * 주테이블에 외래 키가 있는 단방향 연결
     * 주테이블 : User02
     * 대상테이블 : Address02
     * 주테이블 OneToOne 어노테이션 선언하고 JoinColumn으로 대상테이블과 연결
     * User01 객체를 통해 Address01를 조회할 수 있는 구조이다.
     */
    @Test
    public void oneToOneMainOneWay(){
        Address01 address = Address01.builder()
                .addr("서울")
                .build();
        address = address01Repository.save(address);

        User01 user = User01.builder()
                .name("유저1")
                .address(address)
                .build();
        user01Repository.save(user);

        Optional<User01> user01 = user01Repository.findById(1L);
        if(user01.isPresent()) System.out.println(user01.get().getName());
    }

    /**
     * 주테이블에 외래 키가 있는 양방향 연결
     * 주테이블 : User02
     * 대상테이블 : Address02
     * 주테이블과 대상테이블 사이에 양방향 연관관계를 걸고 주테이블 findById, 대상테이블 findById 조회
     * 일대일 관계에서 지연 로딩으로 설정을 해도 즉시 로딩이 되는 경우가 있다.
     * User02.address02 : 지연로딩
     * address02.user02 : 지연로딩 안됨
     * 프록시의 한계로 인해서 외래 키를 직접 관리하지 않는 일대일 관계에서는 지연 로딩으로 설정을 해도 즉시 로딩이 된다.
     * OneToOne 어노테이션의 기존 fetch 타입은 Eager이다.
     */
    @Test
    public void oneToOneMainTwoWay(){
        Address02 address = Address02.builder()
                .addr("서울")
                .build();
        address = address02Repository.save(address);

        User02 user = User02.builder()
                .name("유저2")
                .address(address)
                .build();
        address.setUser(user);
        user02Repository.save(user);

        System.out.println("####################################################### 주테이블 조회 시작 #######################################################");
        User02 findUser = user02Repository.findById(1L).get();
        System.out.println("####################################################### 주테이블 조회 종료 #######################################################");
        System.out.println("####################################################### 대상테이블 조회 시작 #######################################################");
        Address02 findAddress = address02Repository.findById(1L).get();
        System.out.println("####################################################### 대상테이블 조회 종료 #######################################################");
    }

}