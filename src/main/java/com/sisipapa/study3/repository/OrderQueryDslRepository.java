package com.sisipapa.study3.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study3.domain.Order;
import com.sisipapa.study3.dto.OrderSimpleQueryDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sisipapa.study3.domain.QDelivery.delivery;
import static com.sisipapa.study3.domain.QMember.member;
import static com.sisipapa.study3.domain.QOrder.order;

@Repository
@AllArgsConstructor
public class OrderQueryDslRepository  {

    private final JPAQueryFactory queryFactory;

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
                order.delivery.address
        )).fetch();

        return list;

    }
}
