package akrnote.core.order;

import akrnote.core.Order.Order;
import akrnote.core.Order.OrderService;
import akrnote.core.Order.OrderServiceImpl;
import akrnote.core.member.Grade;
import akrnote.core.member.Member;
import akrnote.core.service.MemberService;
import akrnote.core.service.MemberServiceImplements;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    MemberService memberService = new MemberServiceImplements();
    OrderService orderService = new OrderServiceImpl();

    @Test
    void createOrder(){
        Long memberID = 1L;
        Member member = new Member(memberID,"memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberID,"itemA",10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);

    }
}
