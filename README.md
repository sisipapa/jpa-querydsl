## IntelliJ QueryDsl QClass 생성
- Q 타입 클래스 생성  
1. Gradle → Tasks → build → clean  
2. Gradle → Tasks → others → compileQuerydsl  
  
- Q 타입 클래스 확인  
build.gradle 에 설정한 경로에 Q 타입 클래스가 생성되었는지 확인한다.  
  def querydslDir = "$buildDir/generated/querydsl"  

## JpaRepository 구현체 없이 JPAQueryFactory 사용하기  
이 방법은 우아한테크? 유튜브 영상을 보고 배운 방법이다.  
필요하지 않은 JpaRepository Interface를 생성하지 않고 JPAQueryFactory를 Bean으로 등록하고 Repository에서 바로 사용하는 방법이다.    
```java  
package com.sisipapa.study3.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
```  
```java  
@Repository
@AllArgsConstructor
public class OrderQueryDslRepository  {

    private final JPAQueryFactory queryFactory;
    
}
```

## 검색어로 테이블 조회  
파라미터로 받은 OrderSearch 객체의 searchWord를 조회조건으로 테이블 검색   
```java  
public List<Order> findAllByString(OrderSearch orderSearch) {

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(order.orderName.eq(orderSearch.getSearchWord()));

    JPQLQuery<Order> query = queryFactory
                                .selectFrom(order)
                                .where(builder);
    
    List<Order> list = query.fetch();
    return list;
}
```  
```sql  
select
    order0_.order_id as order_id1_11_,
    order0_.delivery_id as delivery5_11_,
    order0_.member_id as member_i6_11_,
    order0_.order_date as order_da2_11_,
    order0_.order_name as order_na3_11_,
    order0_.status as status4_11_ 
from
    orders order0_ 
where
    order0_.order_name=?
```
  
## 테이블 Join 및 Projections를 활용한 특정필드 조회 Alias 적용  
```java  
public List<OrderSimpleQueryDto> findOrderDto() {

//		return getEntityManager().createQuery("select new kr.sskm.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o"
//									 + " join o.member m"
//									 + " join o.delivery d", OrderSimpleQueryDto.class)
//				.getResultList();

    JPAQuery<Order> query = queryFactory.selectFrom(order)
                                .innerJoin(order.member, member)
                                .innerJoin(order.delivery, delivery);

    List<OrderSimpleQueryDto> list = query.select(Projections.fields(OrderSimpleQueryDto.class,
                                                                    ExpressionUtils.as(order.id, "orderId"),
                                                                    member.name,
                                                                    order.orderDate,
                                                                    ExpressionUtils.as(order.status, "orderStatus"),
                                                                    order.delivery.address)).fetch();
    return list;

}
```  
```sql  
select
        order0_.order_id as col_0_0_,
        member1_.name as col_1_0_,
        order0_.order_date as col_2_0_,
        order0_.status as col_3_0_,
        delivery2_.city as col_4_0_,
        delivery2_.street as col_4_1_,
        delivery2_.zipcode as col_4_2_ 
    from
        orders order0_ 
    inner join
        member member1_ 
            on order0_.member_id=member1_.member_id 
    inner join
        delivery delivery2_ 
            on order0_.delivery_id=delivery2_.delivery_id
```

## OnetoOne,ManyToOne 연관관계의 JOIN은 ROW수의 증가가 없기 때문에 fetch join을 사용  
distinct를 사용한 이유는 1대다 조인이 있으므로 데이터베이스 row가 증가한다. 그 결과 같은 order 엔티티 의 조회 수도 증가하게 된다. JPA의 distinct 는 SQL에 distinct를 추가하고, 더해서 같은 엔티티가 조회되면, 애플리케이션에서 중복을 걸러준다.    
```java    
public List<Order> findAllWithItem() {

//		return getEntityManager().createQuery("select distinct o from Order o"
//											+ " join fetch o.member m"
//											+ " join fetch o.delivery d"
//											+ " join fetch o.orderItems oi"
//											+ " join fetch oi.item i", Order.class)
//				.setFirstResult(1)
//				.setMaxResults(100)
//				.getResultList();

      JPQLQuery<Order> query =
              from(order)
                      .innerJoin(order.member, member).fetchJoin()
                      .innerJoin(order.delivery, delivery).fetchJoin()
                      .innerJoin(order.orderItems, orderItem).fetchJoin()
                      .innerJoin(orderItem.item, item).fetchJoin()
                      .distinct();

      List<Order> list = query.fetch();
      return list;
//		
  }
```  
```sql  
select
        distinct order0_.order_id as order_id1_11_0_,
        member1_.member_id as member_i1_6_1_,
        delivery2_.delivery_id as delivery1_4_2_,
        orderitems3_.order_item_id as order_it1_10_3_,
        item4_.item_id as item_id1_5_4_,
        order0_.delivery_id as delivery5_11_0_,
        order0_.member_id as member_i6_11_0_,
        order0_.order_date as order_da2_11_0_,
        order0_.order_name as order_na3_11_0_,
        order0_.status as status4_11_0_,
        member1_.city as city2_6_1_,
        member1_.street as street3_6_1_,
        member1_.zipcode as zipcode4_6_1_,
        member1_.name as name5_6_1_,
        delivery2_.city as city2_4_2_,
        delivery2_.street as street3_4_2_,
        delivery2_.zipcode as zipcode4_4_2_,
        delivery2_.status as status5_4_2_,
        orderitems3_.item_id as item_id4_10_3_,
        orderitems3_.order_id as order_id5_10_3_,
        orderitems3_.order_count as order_co2_10_3_,
        orderitems3_.order_price as order_pr3_10_3_,
        orderitems3_.order_id as order_id5_10_0__,
        orderitems3_.order_item_id as order_it1_10_0__,
        item4_.actor as actor2_5_4_,
        item4_.artist as artist3_5_4_,
        item4_.author as author4_5_4_,
        item4_.director as director5_5_4_,
        item4_.dtype as dtype6_5_4_,
        item4_.etc as etc7_5_4_,
        item4_.isbn as isbn8_5_4_,
        item4_.name as name9_5_4_,
        item4_.price as price10_5_4_,
        item4_.stock_quantity as stock_q11_5_4_ 
    from
        orders order0_ 
    inner join
        member member1_ 
            on order0_.member_id=member1_.member_id 
    inner join
        delivery delivery2_ 
            on order0_.delivery_id=delivery2_.delivery_id 
    inner join
        order_item orderitems3_ 
            on order0_.order_id=orderitems3_.order_id 
    inner join
        item item4_ 
            on orderitems3_.item_id=item4_.item_id
```  

## OneToMany 관계 ROW수 증가 처리
ToOne 관계는 페치 조인해도 페이징에 영향을 주지 않는다.  
따라서 ToOne 관계는 페치조인으로 쿼리수를 줄여서 해결하고,나머지는 hibernate.default_batch_fetch_size(100~1000) 로 최적화 한다.  
  
#### application.yml - default_batch_fetch_size추가  
```yml  
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 100
```  

#### controller - ToOne관계 fetch조인으로 조회한 목록을 어플리케이션에서 반복문을 수행하면 N+1 쿼리가 아닌 IN쿼리로 한번에 조회 
```java  
@GetMapping("/api/v3.1/orders")
public List<OrderDto> orderV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                   @RequestParam(value = "limit", defaultValue = "100") int limit) {
    List<Order> orders = orderRepository.findAllWithMemberDelivery();
    List<OrderDto> result = orders.stream()
            .map(order -> new OrderDto(order))
            .collect(Collectors.toList());
    return result;
}
```  
```sql  
/* 1번쨰 쿼리수행 - fetch join 쿼리 */  
select
    order0_.order_id as order_id1_11_0_,
    member1_.member_id as member_i1_6_1_,
    delivery2_.delivery_id as delivery1_4_2_,
    order0_.delivery_id as delivery5_11_0_,
    order0_.member_id as member_i6_11_0_,
    order0_.order_date as order_da2_11_0_,
    order0_.order_name as order_na3_11_0_,
    order0_.status as status4_11_0_,
    member1_.city as city2_6_1_,
    member1_.street as street3_6_1_,
    member1_.zipcode as zipcode4_6_1_,
    member1_.name as name5_6_1_,
    delivery2_.city as city2_4_2_,
    delivery2_.street as street3_4_2_,
    delivery2_.zipcode as zipcode4_4_2_,
    delivery2_.status as status5_4_2_ 
from
    orders order0_ 
inner join
    member member1_ 
        on order0_.member_id=member1_.member_id 
inner join
    delivery delivery2_ 
        on order0_.delivery_id=delivery2_.delivery_id

/* 2번쨰 쿼리수행 - order:orderitem 1대N 관계 IN쿼리 */        
select
    orderitems0_.order_id as order_id5_10_1_,
    orderitems0_.order_item_id as order_it1_10_1_,
    orderitems0_.order_item_id as order_it1_10_0_,
    orderitems0_.item_id as item_id4_10_0_,
    orderitems0_.order_id as order_id5_10_0_,
    orderitems0_.order_count as order_co2_10_0_,
    orderitems0_.order_price as order_pr3_10_0_ 
from
    order_item orderitems0_ 
where
    orderitems0_.order_id in (
        ?, ?
    )

/* 3번쨰 쿼리수행 - orderitem:item 1대N 관계 IN쿼리 */    
select
    item0_.item_id as item_id1_5_0_,
    item0_.actor as actor2_5_0_,
    item0_.artist as artist3_5_0_,
    item0_.author as author4_5_0_,
    item0_.director as director5_5_0_,
    item0_.dtype as dtype6_5_0_,
    item0_.etc as etc7_5_0_,
    item0_.isbn as isbn8_5_0_,
    item0_.name as name9_5_0_,
    item0_.price as price10_5_0_,
    item0_.stock_quantity as stock_q11_5_0_ 
from
    item item0_ 
where
    item0_.item_id in (
        ?, ?, ?, ?
    )

```    

## 참고
[인프런 강의 - 실전! 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-API%EA%B0%9C%EB%B0%9C-%EC%84%B1%EB%8A%A5%EC%B5%9C%EC%A0%81%ED%99%94/dashboard)  

[SISIPAPA 노트연결](https://sisipapa.github.io/blog/2021/04/12/Spring-Data-JPA+QueryDSL-%EC%A0%95%EB%A6%AC/)
