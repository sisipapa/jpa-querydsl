package com.sisipapa.study3.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study3.domain.Order;
import com.sisipapa.study3.dto.OrderSearch;
import com.sisipapa.study3.dto.OrderSimpleQueryDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sisipapa.study3.domain.QDelivery.delivery;
import static com.sisipapa.study3.domain.QMember.member;
import static com.sisipapa.study3.domain.QOrder.order;
import static com.sisipapa.study3.domain.QOrderItem.orderItem;
import static com.sisipapa.study3.domain.QItem.item;

@Repository
@AllArgsConstructor
public class OrderQueryDslRepository  {

    private final JPAQueryFactory queryFactory;

    /**
     * Order 테이블 단건 조회
     * @param orderSearch
     * @return
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(order.orderName.eq(orderSearch.getSearchWord()));

        JPQLQuery<Order> query = queryFactory
                                    .selectFrom(order)
                                    .where(builder);
        
        List<Order> list = query.fetch();
        return list;
    }

    /**
     * Order, Member, Delivery 테이블 Join 조회
     * @return
     */
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

    /**
     * OneToOne, ManyToOne 등 Row수 증가가 없을 경우 fetch JOIN을 사용해서 조회
     * @return
     */
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
                        queryFactory.selectFrom(order)
                        .innerJoin(order.member, member).fetchJoin()
                        .innerJoin(order.delivery, delivery).fetchJoin()
                        .innerJoin(order.orderItems, orderItem).fetchJoin()
                        .innerJoin(orderItem.item, item).fetchJoin()
                        .distinct();

        List<Order> list = query.fetch();
        return list;
//
    }

    public List<Order> findAllWithMemberDelivery() {
        JPQLQuery<Order> query = queryFactory.selectFrom(order)
                .innerJoin(order.member, member).fetchJoin()
                .innerJoin(order.delivery, delivery).fetchJoin();
        List list = query.fetch();
        return list;

//		return getEntityManager().createQuery("select o from Order o"
//											+ " join fetch o.member m"
//											+ " join fetch o.delivery d", Order.class)
//				.getResultList();

    }

    private BooleanBuilder getCommonWhere(OrderSearch orderSearch){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(order.orderName.eq(orderSearch.getSearchWord()));
        return builder;
    }
}
