package akrnote.core.Order;

import akrnote.core.member.Grade;
import akrnote.core.member.Member;
import akrnote.core.service.MemberService;
import akrnote.core.service.MemberServiceImplements;

public class OrderApp {

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImplements();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);
        System.out.println("order = "+order);
    }
}
