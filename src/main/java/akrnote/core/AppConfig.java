package akrnote.core;

import akrnote.core.Order.OrderService;
import akrnote.core.Order.OrderServiceImpl;
import akrnote.core.Repository.MemoryMemberRepository;
import akrnote.core.discount.FixDiscountPolicy;
import akrnote.core.service.MemberService;
import akrnote.core.service.MemberServiceImplements;

public class AppConfig {

    public MemberService memberService(){
        return new MemberServiceImplements(new MemoryMemberRepository());
    }

    public OrderService orderService(){
        return new OrderServiceImpl(new MemoryMemberRepository(),new FixDiscountPolicy());
    }
}
