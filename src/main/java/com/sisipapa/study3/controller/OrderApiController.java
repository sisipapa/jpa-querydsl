package com.sisipapa.study3.controller;

import com.sisipapa.study3.domain.Address;
import com.sisipapa.study3.domain.Order;
import com.sisipapa.study3.domain.OrderItem;
import com.sisipapa.study3.domain.cenum.OrderStatus;
import com.sisipapa.study3.dto.OrderSearch;
import com.sisipapa.study3.repository.OrderQueryDslRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderQueryDslRepository orderRepository;

    /**
     * v1은 전체를 조회
     * 조회한 목록을 반복문을 돌면서 Lazy 터치를 해서 조회
     *
     * @return
     */
    @GetMapping("/api/v1/orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    /**
     * @return
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

        return result;

    }

    /**
     * 페치 조인으로 SQL이 1번만 실행된다.
     * distinct를 사용한 이유는 1대다 조인이 있으므로 데이터베이스 row가 증가한다. 그결과 같은 order 엔티티 의 조회 수도 증가하게 된다.
     * JPA의 distinct 는 SQL에 distinct를 추가하고, 더해서 같은 엔티티가 조회되면, 애플리케이션에서 중복을 걸러준다.
     *
     * @return
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());
        return result;
    }


    /**
     * 페이징 + 컬렉션 엔티티 조회방법
     * 1-1. ToOne(OneToOne,ManyToOne)관계는 모두 fetch조인을 한다(데이터 ROW의 증가가 없기 떄문에 페이징 쿼리에 영향을 주지 않는다.)
     * 1-2. 컬렉션은 지연 로딩으로 조회한다.
     * 1-3. 지연로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size, BatchSize를 적용한다.
     * hibernate.default_batch_fetch_size:글로벌 설정
     *
     * @return
     * @BatchSize: 개별최적화(Order.java에 OrderItem @ BatchSize ( size = 1000) 개별최적화)
     * 이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN쿼리로 조회한다.
     * 장점
     * 1. 쿼리 호출수가 1+N=>1+1
     * 2. 조인보다 DB 데이터 전송량이 최적화 된다.
     * 3. 페치 조인 방식과 비교해서 쿼리 호출수가 약간 증가하지만, DB 데이터 정송량이 감소한다.
     * 4. 컬렉션 페이 조인은 페이징이 불가능 하지만 이방법은 페이징이 가능하다.
     * <p>
     * 결론
     * ToOne 관계는 페치 조인해도 페이징에 영향을 주지 않는다. 따라서 ToOne 관계는 페치조인으로 쿼리수를
     * 줄여서 해결하고, 나머지는 hibernate.default_batch_fetch_size(100~1000) 로 최적화 하자!!!!!
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderDto> result = orders.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());
        return result;
    }

//    @GetMapping("/api/v4/orders")
//    public List<OrderQueryDto> orderV4() {
//        return orderRepository.findOrderQueryDtos();
//    }
//
//    @GetMapping("/api/v5/orders")
//    public List<OrderQueryDto> orderV5() {
//        return orderRepository.findAllByDto_optimization();
//    }

    /**
     * Query :1번
     * 어플리케이션 메모리 내에서 중복을 제거하고 컬렉션을 생성 후 매핑
     * 쿼리는 한번이지만 조인으로 인해 DB에서 애플리케이션에 전달하는 데이터에 중복 데이터가 추가되므로 상황에 따라 V5 보다 더 느릴수 있다.
     * 애플리케이션에서 추가 작업이 크다
     * 페이징이 불가능
     *
     * @return
     */
//    @GetMapping("/api/v6/orders")
//    public List<OrderFlatDto> orderV6() {
//        List<OrderFlatDto> flats = orderRepository.findAllByDto_flat();
//        return flats;
////		return flats.stream()
////				.collect(Collectors.groupingBy(o -> new OrderQueryDto(o.)))
//    }

    @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getOrderCount();
        }
    }
}
